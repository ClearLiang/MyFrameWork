package com.example.a99794.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

import java.util.Random;

/**
 * @作者 ClearLiang
 * @日期 2018/4/28
 * @描述 @desc
 **/

public class BindService extends Service {

    public class MyBinder extends Binder {

        public BindService getService() {
            return BindService.this;
        }
    }

    private MyBinder binder = new MyBinder();

    private final Random generator = new Random();

    @Override
    public void onCreate() {
        LogUtils.i("BindService -> onCreate, Thread: " + Thread.currentThread().getName());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("BindService -> onStartCommand, startId: " + startId + ", Thread: " + Thread.currentThread().getName());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("BindService -> onBind, Thread: " + Thread.currentThread().getName());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.i("BindService -> onUnbind, from:" + intent.getStringExtra("from"));
        return false;
    }

    @Override
    public void onDestroy() {
        LogUtils.i("BindService -> onDestroy, Thread: " + Thread.currentThread().getName());
        super.onDestroy();
    }

    //在Service中暴露出去的方法，供client调用
    public int getRandomNumber() {
        return generator.nextInt();
    }
}
