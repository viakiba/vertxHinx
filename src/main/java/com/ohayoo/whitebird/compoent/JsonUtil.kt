package com.ohayoo.whitebird.compoent

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.json.JsonWriterSettings
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import java.lang.RuntimeException
import java.io.IOException
import com.fasterxml.jackson.databind.DeserializationFeature
import lombok.extern.slf4j.Slf4j

@Slf4j
object JsonUtil {
    var jackson = ObjectMapper()
    //mongo long型转换 解决 从mongo取出数据的格式问题 { "$date" : { "$numberLong" : "<dateAsMilliseconds>" } }
    val LONG_WRITER_SETTING =
        JsonWriterSettings.builder().int64Converter { value, writer -> writer.writeNumber(value.toString()) }
            .build()

    @JvmStatic
    fun obj2Str(`object`: Any?): String {
        return try {
            jackson.writeValueAsString(`object`)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun <T> str2Obj(jsonStr: String, valueType: Class<T>): T {
        return try {
            jackson.readValue(jsonStr, valueType)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun <T> byteArray2Obj(bytes: ByteArray, valueType: Class<T>): T {
        return try {
            jackson.readValue(bytes, valueType)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun <T> str2Obj(str: String, type: TypeReference<T>): T {
        return try {
            jackson.readValue(str, type)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    init {
        jackson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}