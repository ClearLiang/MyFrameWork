package com.example.a99794.framework.model.entity;

import java.io.Serializable;

/**
 * Created by 99794 on 2018/5/3.
 */

public class WXCheckTokenEntity implements Serializable {
    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
