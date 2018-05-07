package com.example.a99794.framework.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;

import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;

/**
 * @作者 ClearLiang
 * @日期 2018/5/4
 * @描述 指纹验证工具，先setFinger验证，再startListening设置监听
 * 设置成单例的话，多次验证错误后，必须重启App才能重新验证
 **/

public class FingerprintUtil {
    /*private static FingerprintUtil sFingerprintUtil;

    private FingerprintUtil() {
    }

    public static synchronized FingerprintUtil getFingerprintUtil() {
        if(sFingerprintUtil == null){
            sFingerprintUtil = new FingerprintUtil();
        }
        return sFingerprintUtil;
    }*/

    FingerprintManagerCompat mFingerprintManagerCompat;
    FingerprintManager manager;
    KeyguardManager mKeyManager;

    private Context mContext;

    private int flag = 5;

    public boolean setFinger(Context context){
        mContext = context;

        manager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);
        mKeyManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);

        if (isFinger()) {
            //Toast.makeText(mContext, "请进行指纹识别", Toast.LENGTH_LONG).show();
            LogUtils.i("可以");
            //startListening(null);
            return true;
        }
        return false;
    }


    @SuppressLint("NewApi")
    private boolean isFinger() {
        if (!manager.isHardwareDetected()) {
            Toast.makeText(mContext, "没有指纹识别模块", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mKeyManager.isKeyguardSecure()) {
            Toast.makeText(mContext, "没有开启锁屏密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!manager.hasEnrolledFingerprints()) {
            Toast.makeText(mContext, "没有录入指纹", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private CancellationSignal mCancellationSignal = new CancellationSignal();

    //回调方法
    /*@SuppressLint("NewApi")
    FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
            Toast.makeText(mContext, "多次验证错误，请1分钟后再试", Toast.LENGTH_SHORT).show();
            LogUtils.i(errString);

            *//*Intent intent = mKeyManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
            if (intent != null) {
                startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
            }*//*
        }
        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

            Toast.makeText(mContext, helpString, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            Toast.makeText(mContext, "指纹识别成功", Toast.LENGTH_SHORT).show();
            LogUtils.i("识别成功");
        }
        @Override
        public void onAuthenticationFailed() {
            flag--;
            if(flag != 0){
                Toast.makeText(mContext, "指纹识别失败，还剩下"+flag+"次机会", Toast.LENGTH_SHORT).show();
                LogUtils.i("指纹识别失败，还剩下"+flag+"次机会");
            }
        }
    };*/

    @SuppressLint("NewApi")
    public void startListening(FingerprintManager.CryptoObject cryptoObject,FingerprintManager.AuthenticationCallback mSelfCancelled) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.authenticate(cryptoObject, mCancellationSignal, 0, mSelfCancelled, null);

    }

    @SuppressLint("NewApi")
    public void startListening(FingerprintManager.AuthenticationCallback mSelfCancelled) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.authenticate(null, mCancellationSignal, 0, mSelfCancelled, null);

    }

    public void closeFinger(){
        mCancellationSignal.cancel();
    }

}
