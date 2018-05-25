package com.example.a99794.framework.view.base;

/**
 * @作者 ClearLiang
 * @日期 2018/5/4
 * @描述 @desc
 **/

public interface GlobalConstants {
    int REQUEST_SCAN_CODE   = 1001;   //扫描请求码
    int REQUEST_PAY_CODE    = 1002;   //支付请求码
    int REQUEST_FINGER_CODE = 1003;   //指纹请求码

    int TYPE_SUCCESS  = 0;  //下载成功
    int TYPE_FAILED   = 1;  //下载失败
    int TYPE_PAUSED   = 2;  //下载暂停
    int TYPE_CANCELED = 3;  //下载取消

    String TAG = "信息：";

    String DIRECTORY = "FrameWork";

    String DOWNLOAD_APP_URL="https://dldir1.qq.com/qqfile/qq/QQ9.0.3/23719/QQ9.0.3.exe";

}
