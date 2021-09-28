package com.ohayoo.whitebird.gmadmin

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.ohayoo.whitebird.boot.GlobalContext
import com.ohayoo.whitebird.compoent.JsonUtil.obj2Str
import com.ohayoo.whitebird.compoent.TimeUtil
import com.ohayoo.whitebird.data.model.GameData
import com.ohayoo.whitebird.gmadmin.model.ResultData
import com.ohayoo.whitebird.gmadmin.model.ServerData
import com.ohayoo.util.HttpUtil
import groovy.lang.Tuple4
import io.vertx.codegen.annotations.Nullable
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.Cookie
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jose4j.jws.JsonWebSignature
import org.jose4j.keys.HmacKey
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.StandardCharsets


class GmRouteService {
    private val log = LoggerFactory.getLogger(GmRouteService::class.java)
    var thymeleafTemplateEngine: ThymeleafTemplateEngine? = null
    var authProvider : JWTAuth? =null
    var secret = "heroheroheroheroheroheroherohero"
    fun initRoute(router: Router, vertx: Vertx) {
        thymeleafTemplateEngine = ThymeleafTemplateEngine.create(vertx)
        val authConfig: JWTAuthOptions = JWTAuthOptions().addPubSecKey(PubSecKeyOptions().setAlgorithm("HS256").setBuffer(secret))
        authProvider  = JWTAuth.create(vertx, authConfig)
        heartCheck(router)
        gmAll(router,vertx)
    }

    private fun gmAll(router: Router, vertx: Vertx) {
        staticResource(router)
        gmLogin(router)
        gmLoginApi(router,authProvider!! )
        gmIndex(router)
        gmGameDataHtml(router)
        gmGameDataApi(router)
        gmHotFixHtml(router)
        gmHotFixApi(router)
        gmHotFixApiEval(router)
    }

    private fun gmLoginApi(router: Router, jwt: JWTAuth) {
        router.route("/gm/loginReq").blockingHandler(BodyHandler.create()).blockingHandler { ctx: RoutingContext ->
            var username = ctx.request().getParam("account")
            var password = ctx.request().getParam("password")
            if ("admin" == username && "admin" == password) {
                var generateToken = jwt.generateToken(JsonObject().put("sub", username))
                ctx.response().addCookie(Cookie.cookie("token",generateToken))
                thymeleafTemplateEngine!!.render(
                    JSONObject(), "templates/index.html"
                ) { res: AsyncResult<Buffer?> ->
                    if (res.succeeded()) {
                        ctx.response().end(res.result())
                    } else {
                        ctx.fail(res.cause())
                    }
                }
            } else {
                loginHtml(ctx)
            }
        }
    }

    private fun gmLogin(router: Router) {
        router.route("/gm/loginHtml").blockingHandler { ctx: RoutingContext ->
            val jsonObject = JSONObject()
            thymeleafTemplateEngine!!.render(
                jsonObject, "templates/login.html"
            ) { res: AsyncResult<Buffer?> ->
                if (res.succeeded()) {
                    ctx.response().end(res.result())
                } else {
                    ctx.fail(res.cause())
                }
            }
        }

    }

    private fun gmHotFixApiEval(router: Router) {
        router.route("/gm/hotFixApiEval").blockingHandler(BodyHandler.create()).blockingHandler { ctx: RoutingContext ->
            val s = ctx.bodyAsString
            log.error(s)
            var eval: String? = ""
            eval = try {
                GlobalContext.getGroovyScriptEngine().eval(s)
            } catch (e: Exception) {
                log.error("脚本执行异常", e)
                e.message
            }
            ctx.response().end(eval)
        }
    }

    private fun gmHotFixApi(router: Router) {
        router.route("/gm/hotFixApi").blockingHandler(BodyHandler.create()).blockingHandler { ctx: RoutingContext ->
            if(checkCookie(ctx)){
                loginHtml(ctx)
                return@blockingHandler
            }
            GlobalScope.async {
                val sb = StringBuilder()
                val split = ctx.request().getParam("servers").split(",".toRegex()).toTypedArray()
                val luaPayload = ctx.request().getParam("luaPayload")
                for (ipPort in split) {
                    val url = "http://$ipPort/gm/hotFixApiEval"
                    try {
                        var s = HttpUtil.postUrl(url, HashMap(), 3000, luaPayload, null)
                        sb.append(s)
                    }catch (e : Exception){
                        log.error("请求异常 ",e)
                        sb.append(e.message)
                    }
                    sb.append("\n<br>----------------------分割线 $ipPort-------------------------<br>")
                }
                val resp: HashMap<String, String> = HashMap<String, String>()
                resp["data"] = sb.toString()
                log.info("----------------------------")
                ctx.response().end(obj2Str(resp))
            }
        }
    }

    private fun gmHotFixHtml(router: Router) {
        router.route("/gm/hotFixHtml").blockingHandler { ctx: RoutingContext ->
            if(checkCookie(ctx)){
                loginHtml(ctx)
                return@blockingHandler
            }
            val jsonObject = JSONObject()
            try {
                jsonObject["servers"] = getAllServer()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            thymeleafTemplateEngine!!.render(
                jsonObject, "templates/hotfix/hotFixHtml.html"
            ) { res: AsyncResult<Buffer?> ->
                if (res.succeeded()) {
                    ctx.response().end(res.result())
                } else {
                    ctx.fail(res.cause())
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun getAllServer(): List<ServerData>? {
        if (GlobalContext.serverConfig().isDebug) {
            val serverData = ServerData()
            serverData.ip = "127.0.0.1"
            serverData.port = 10002
            serverData.workId = "1"
            serverData.centerId = "1"
            return listOf(serverData)
        }
        val serverData1 = ArrayList<ServerData>()
        val allServerMap = GlobalContext.getAllServerMap()
        for ( t  in allServerMap) {
            val value: Tuple4<String, Int, Int, Int> = t.value
            val serverData = ServerData()
            serverData.ip = value.v1
            serverData.port = value.v2
            serverData.centerId = value.v3.toString() + ""
            serverData.workId = value.v4.toString() + ""
            serverData1.add(serverData)
        }
        return serverData1
    }

    private fun gmGameDataApi(router: Router) {
        router.route("/gm/gameDataApi").blockingHandler { ctx: RoutingContext ->
            if(checkCookie(ctx)){
                loginHtml(ctx)
                return@blockingHandler
            }
            val openId = ctx.queryParam("openId")
            if (openId.size == 0 || openId[0].isBlank()) {
                val resultData = ResultData(emptyList())
                ctx.response().end(JSONObject.toJSONString(resultData))
            } else {
                GlobalContext.getDataSystemService().selectByOpenId(
                    openId[0]
                ) { h: AsyncResult<GameData?> ->
                    val result = h.result()
                    if (result == null) {
                        val resultData = ResultData(emptyList())
                        ctx.response().end(JSONObject.toJSONString(resultData))
                    } else {
                        val objects = listOf<Any>(result)
                        val resultData = ResultData(objects)
                        val s = obj2Str(resultData)
                        ctx.response().putHeader("content-type", "application/json")
                        ctx.response().end(s)
                    }
                }
            }
        }
    }

    private fun gmGameDataHtml(router: Router) {
        router.route("/gm/gameDataHtml").blockingHandler { ctx: RoutingContext ->
            thymeleafTemplateEngine!!.render(
                JSONObject(), "templates/data/gameData.html"
            ) { res: AsyncResult<Buffer?> ->
                if (res.succeeded()) {
                    ctx.response().end(res.result())
                } else {
                    ctx.fail(res.cause())
                }
            }
        }
    }

    private fun gmIndex(router: Router) {
        router.route("/gm/index").blockingHandler { ctx: RoutingContext ->
            if(checkCookie(ctx)){
                loginHtml(ctx)
                return@blockingHandler
            }
            thymeleafTemplateEngine!!.render(
                JSONObject(), "templates/index.html"
            ) { res: AsyncResult<Buffer?> ->
                if (res.succeeded()) {
                    ctx.response().end(res.result())
                } else {
                    ctx.fail(res.cause())
                }
            }
        }
    }

    private fun loginHtml(ctx: RoutingContext) {
        ctx.response().end("please login")
    }

    private fun checkCookie(ctx: RoutingContext): Boolean {
        var cookie: @Nullable Cookie? = ctx.getCookie("token") ?: return true
        var checkToken = checkToken(cookie!!.value)
        return !checkToken
    }

    private fun staticResource(router: Router) {
        val handler = StaticHandler.create()
        handler.setWebRoot("templates/static")
        router.route("/templates/static/*").blockingHandler(handler)
    }

    private fun heartCheck(router: Router) {
        router.route("/").blockingHandler { ctx: RoutingContext ->  //ELB HeartCheckController
            ctx.request().response().end("")
        }
    }

    @Throws(java.lang.Exception::class)
    fun checkToken(token: String?): Boolean {
        val jws = JsonWebSignature()
        jws.key = HmacKey(secret.toByteArray(StandardCharsets.UTF_8))
        jws.compactSerialization = token
        val payload = jws.payload
        val jsonObject = JSON.parseObject<Map<String, Any>>(payload, MutableMap::class.java)
        var any = jsonObject["iat"]
        if(any == null){
            return false
        }
        if(!jws.verifySignature()){
            return false
        }
        var toLong = any as Int
        if(TimeUtil.currentSystemTimeSeconds() - toLong > 30 * 60 ){
            return false
        }
        return true
    }
}

