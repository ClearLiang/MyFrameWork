package com.example.a99794.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.a99794.framework.utils.DownloadUtil;
import com.example.a99794.framework.view.base.GlobalConstants;

/**
 * @作者 ClearLiang
 * @日期 2018/5/8
 * @描述 下载服务
 **/

public class DownloadService extends Service {
    private String downUrl;
    private DownloadUtil downloadUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        downUrl = intent.getStringExtra("downUrl");
        downloadUtil = new DownloadUtil(this,true);
        downloadUtil.downLoadFile(downUrl,"qq.exe", GlobalConstants.DIRECTORY);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
