package com.ohayoo.whitebird.data.model.gamedata;

public class BdData {

    /**
     * 用户的唯一身份标识，需要保证同一个用户在本应用内全局唯一，即需要与客户端上报一致
     */
    private String logId="";

    /**
     * 客户端系统 游戏平台
     */
    private String osName="";

    /**
     * userSecret
     */
    private String userSecret="";

    /**
     * userToken 接游戏云接口用
     */
    private String token="";

    /**
     * 登陆类型
     */
    private String loginType="";

    /**
     * 账户类型，ohayoo/huawei/oppo/vivo/xiaomi
     */
    private String accountType="";

    /**
     * 客户端版本，格式 如v0.1.18
     */
    private String version="";

    /**
     * packageName 包名
     */
    private String packageName="";

    /**
     * channel，渠道，如xiaomi/ssjj/jrtt
     */
    private String channel="";

    /**
     * 设备ID，深度转化的bd_did或did
     */
    private String deviceId="";

    /**
     * OPENID
     */
    private String openId="";

    /**
     * osVersion
     */
    private String osVersion="";

    /**
     * serverIp
     */
    private String serverIp="";

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
