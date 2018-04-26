package com.example.a99794.framework.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;


/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 扫描/创建 二维码 工具类
 **/

public class ScanUtils {
    private static ScanUtils scanUtils;

    private ScanUtils() {
    }

    public static synchronized ScanUtils getScanUtils() {
        if(scanUtils == null){
            scanUtils = new ScanUtils();
        }
        return scanUtils;
    }

    /**
     *@函数名 -startScan
     *@功能   -扫描二维码，可进入相册选择照片
     * ----------------------------------------------
     *@参数   -activity    上下文
     *        -requestCode 回传扫到的信息
     * ----------------------------------------------
     *@返回值 -无
     **/
    public void startScan(Activity activity,int requestCode){
        Intent intent = new Intent(activity, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        config.setPlayBeep(true);//是否播放提示音
        config.setShake(true);//是否震动
        config.setShowAlbum(true);//是否显示相册
        config.setShowFlashLight(true);//是否显示闪光灯
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     *@函数名 -startCreate
     *@功能   -根据传入的editText生成二维码
     * ----------------------------------------------
     *@参数   -activity 上下文
     *        -editText 二维码信息
     *        -logoIcon 二维码的logo
     * ----------------------------------------------
     *@返回值 -Bitmap 图片，可以用来保存或展示
     **/
    public Bitmap startCreate(Activity activity,String editText){
        return startCreate(activity,editText,0);
    }

    public Bitmap startCreate(Activity activity, String editText, int logoIcon){
        Bitmap bitmap = null;
        Bitmap logo = null;
        try {
            if(logoIcon != 0){
                logo = BitmapFactory.decodeResource(activity.getResources(), logoIcon);
            }
            bitmap = CodeCreator.createQRCode(editText, 400, 400, logo);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
