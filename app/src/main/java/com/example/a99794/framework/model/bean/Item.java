package com.example.a99794.framework.model.bean;

/**
 * @作者 ClearLiang
 * @日期 2018/5/7
 * @描述 @desc
 **/

public class Item {
    private String iId;
    private String iName;
    private int iImg;

    public Item(int iImg, String iName) {
        this.iName = iName;
        this.iImg = iImg;
    }

    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public int getiImg() {
        return iImg;
    }

    public void setiImg(int iImg) {
        this.iImg = iImg;
    }
}
