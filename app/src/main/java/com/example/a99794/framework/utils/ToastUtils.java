package com.example.a99794.framework.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a99794.framework.R;

/**
 *@作者 ClearLiang
 *@日期 2018/4/16
 *@描述 Toast工具类
 **/

public class ToastUtils {
    private static ToastUtils toastUtils;
    private Toast toast;

    private static TextView mTextView;
    private static ImageView mImageView;

    private ToastUtils() {
    }

    public static synchronized ToastUtils getToastUtils() {
        if(toastUtils == null){
            toastUtils = new ToastUtils();
        }
        return toastUtils;
    }

    public void showLoading(){

    }
    /**
     * 不带图标的提示
     * @param context
     * @param msgString
     */
    public synchronized void showToast(Context context, String msgString) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.utils_toast, null);
        //初始化布局控件
        mTextView = toastRoot.findViewById(R.id.message);
        mImageView = toastRoot.findViewById(R.id.imageView);
        //为控件设置属性
        mImageView.setVisibility(View.GONE);
        mTextView.setText(msgString);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        setToastPos(toastStart);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    /**
     * 带图标的提示
     * @param context
     * @param msgString
     */
    public synchronized void showToast(Context context, String msgString,int icon) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.utils_toast, null);
        //初始化布局控件
        mTextView  = toastRoot.findViewById(R.id.message);
        mImageView = toastRoot.findViewById(R.id.imageView);
        //为控件设置属性
        mTextView.setText(msgString);
        mImageView.setImageResource(icon);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        setToastPos(toastStart);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    private static void setToastPos(Toast toast) {
        if (toast != null) {
            toast.setGravity(Gravity.CENTER, 0, 0);
            //toast.setDuration(Toast.LENGTH_LONG);
        }
    }

}
