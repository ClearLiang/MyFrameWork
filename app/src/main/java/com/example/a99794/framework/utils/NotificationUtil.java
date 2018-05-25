package com.example.a99794.framework.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.example.a99794.framework.R;
import com.example.a99794.framework.view.activity.MyNotificationActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @作者 ClearLiang
 * @日期 2018/5/4
 * @描述 消息通知工具
 **/

public class NotificationUtil {
    private static Context mContext;
    private NotificationManager mNManager;
    private Notification notification;
    Bitmap LargeBitmap = null;
    private static final int NOTIFYID_1 = 1;
    private int a= 1;

    private NotificationUtil() {
    }

    public static NotificationUtil getNotificationUtils(Context context) {
        mContext = context;
        return NotificationUtilsHolder.S_NOTIFICATION_UTIL;
    }

    private static class NotificationUtilsHolder{
        private static final NotificationUtil S_NOTIFICATION_UTIL = new NotificationUtil();
    }

    public void showNotification(RemoteViews remoteViews,PendingIntent pendingIntent){

        //创建大图标的Bitmap
        LargeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.bitmap1);
        mNManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        //定义一个PendingIntent点击Notification后启动一个Activity
        /*Intent it = new Intent(mContext, MyNotificationActivity.class);
        PendingIntent pit = PendingIntent.getActivity(mContext, 0, it, 0);*/

        /**设置图片,通知标题,发送时间,提示方式等属性
        - setContentTitle       设置标题
        - setContentText        设置内容
        - setLargeIcon          设置通知栏大图标
        - setSmallIcon          设置通知栏小图标
        - setContent            设置RemoteViews
        - setContentIntent      当通知条目被点击，就执行这个被设置的Intent.
        - setDeleteIntent       当用户点击"Clear All Notifications"按钮区删除所有的通知的时候，这个被设置的Intent被执行
        - setLights             设置闪光灯
        - setSound              设置声音
        - setPriority           设置优先级
        */
        notification = new Notification.Builder(mContext)
                .setContentTitle(mContext.getResources().getString(R.string.app_name))
                .setContentText("点击即可详细了解情况")
                //.setSubText("——记住我叫叶良辰")
                //.setTicker("收到叶良辰发送过来的信息~")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.delete)
                .setLargeIcon(LargeBitmap)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                //.setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.biaobiao))  //设置自定义的提示音
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .build();

        //mNManager.notify(a, notify1);
        mNManager.notify(NOTIFYID_1, notification);
        a++;
        if(a>10){
            a = 1;
        }
    }

    public void showNotification(int largeBitmap,String title,String text,int smallIcon){

        //创建大图标的Bitmap
        LargeBitmap = BitmapFactory.decodeResource(mContext.getResources(), largeBitmap);
        mNManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        //定义一个PendingIntent点击Notification后启动一个Activity
        Intent it = new Intent(mContext, MyNotificationActivity.class);
        PendingIntent pit = PendingIntent.getActivity(mContext, 0, it, 0);

        //设置图片,通知标题,发送时间,提示方式等属性
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setContentTitle(title)                        //标题
                .setContentText(text)      //内容
                //.setSubText("——记住我叫叶良辰")                    //内容下面的一小段文字
                //.setTicker("收到叶良辰发送过来的信息~")             //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setSmallIcon(smallIcon)            //设置小图标
                .setLargeIcon(LargeBitmap)                     //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                //.setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.biaobiao))  //设置自定义的提示音
                .setAutoCancel(true)                           //设置点击后取消Notification
                .setContentIntent(pit);                        //设置PendingIntent
        notification = mBuilder.build();
        mNManager.notify(NOTIFYID_1, notification);
    }

    public void closeNotification(){
        //除了可以根据ID来取消Notification外,还可以调用cancelAll();关闭该应用产生的所有通知
        if (mNManager != null){
            mNManager.cancel(NOTIFYID_1);                          //取消Notification
        }
    }

    public void updataNotification(){
        mNManager.notify(NOTIFYID_1, notification);
    }

}
