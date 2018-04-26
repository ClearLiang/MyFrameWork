package com.example.a99794.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *@作者 ClearLiang
 *@日期 2018/4/16
 *@描述 退出App时，保持后台运行
 **/
// TODO: 2018/4/16  退出App时，保持后台运行
public class ExitInBack extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
