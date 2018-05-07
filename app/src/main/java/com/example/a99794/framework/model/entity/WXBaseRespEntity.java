package com.example.a99794.framework.model.entity;

import java.io.Serializable;

/**
 * description ：
 * project name：CCloud
 * author : Vincent
 * creation date: 2017/6/10 9:39
 *
 * @version 1.0
 */

public class WXBaseRespEntity implements Serializable {
    /**
     * code    : 用户换取access_token的code，仅在ErrCode为0时有效
     * country : 微信用户当前国家信息
     * errCode : ERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消）
     * lang    : 微信客户端当前语言
     * state   : 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用sendReq时传入，由微信终端回传，state字符串长度不能超过1K
     * type    : 1
     * url     : wxb363a9ff53731258://oauth?code=0811Tw9Z0n1fEZ1yKdaZ06tO9Z01Tw94&state=wechat_sdk_%E5%BE%AE%E4%BF%A1%E7%99%BB%E5%BD%95
     */

    private String code;
    private String country;
    private int errCode;
    private String lang;
    private String state;
    private int type;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
