package com.example.a99794.framework.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

/**
 * @作者 ClearLiang
 * @日期 2018/4/28
 * @描述 响铃震动服务
 **/
public class RemindService extends Service {
    private MediaPlayer mMediaPlayer = null;
    private Vibrator vibrator;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("onBind方法被调用!");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("onCreate方法被调用!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("onStartCommand方法被调用!");
        Context context = getApplicationContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        // 等待1秒，震动3秒，从第0个索引开始，一直循环
        if (vibrator != null) {
            vibrator.vibrate(new long[]{1000, 3000}, 0);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy方法被调用!");

        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            // 要释放资源，不然会打开很多个MediaPlayer
            mMediaPlayer.release();
        }
        if(vibrator != null){
            vibrator.cancel();
        }
    }

    public  void PlaySound(final Context context) {
        LogUtils.i("正在响铃");
        // 使用来电铃声的铃声路径
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        /*// 如果为空，才构造，不为空，说明之前有构造过
        if(mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(context, R.raw.warning);
            //mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setLooping(true); //循环播放
        mMediaPlayer.start();*/

        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setLooping(true); //循环播放
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
