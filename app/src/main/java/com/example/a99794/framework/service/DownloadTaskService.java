package com.example.a99794.framework.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.presenter.viewinterface.DownloadListener;
import com.example.a99794.framework.view.activity.MainActivity;

import java.io.File;



public class DownloadTaskService extends Service {
    private DownloadTask downloadTask;

    private String downloadUrl;

    private DownloadListener listener = new DownloadListener() {

        /**
         * 构建了一个用于显示下载进度的通知
         * @param progress
         */
        @Override
        public void onProgress(int progress) {
            //NotificationManager的notify()可以让通知显示出来。
            //notify(),接收两个参数，第一个参数是id:每个通知所指定的id都是不同的。第二个参数是Notification对象。
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        /**
         * 创建了一个新的通知用于告诉用户下载成功
         */
        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",-1));
            ToastUtils.showShort("Download Success");
        }

        /**
         *用户下载失败
         */
        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败时，将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            ToastUtils.showShort("Download Failed");
        }

        /**
         * 用户暂停
         */
        @Override
        public void onPaused() {
            downloadTask = null;
            ToastUtils.showShort("Download Paused");
        }

        /**
         * 用户取消
         */
        @Override
        public void onCanceled() {
            downloadTask = null;
            //取消下载，将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            ToastUtils.showShort("Download Canceled");
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 为了要让DownloadService可以和活动进行通信，我们创建了一个DownloadBinder对象
     */
    public class DownloadBinder extends Binder {

        /**  开始下载  */
        public void  startDownload(String url){
            if(downloadTask == null){
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                //启动下载任务
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("Downloading...",0));
                ToastUtils.showShort("Downloading...");
            }
        }

        /**  暂停下载  */
        public void pauseDownload(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }

        /**  取消下载  */
        public void cancelDownload(){
            if(downloadTask!=null){
                downloadTask.cancelDownload();
            }else {
                if(downloadUrl!=null){
                    //取消下载时需要将文件删除，并将通知关闭
                    String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file=new File(directory+fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    ToastUtils.showShort("下载已取消");
                }
            }
        }

        /**  删除下载的内容  */
        public void deleteDownload(String url){
            //取消下载时需要将文件删除，并将通知关闭
            //String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String fileName = url.substring(url.lastIndexOf("/")+1);
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            File file = new File(directory+File.separator+fileName);
            if(file.exists()){
                file.delete();
                ToastUtils.showShort("下载内容已删除");
            }else {
                ToastUtils.showShort("文件不存在，请确认");
            }
            getNotificationManager().cancel(1);
            stopForeground(true);
        }

    }

    /**
     * 获取NotificationManager的实例，对通知进行管理
     */
    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 获取通知
     */
    private Notification getNotification(String title, int progress){
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        //设置通知的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知的大图标，当下拉系统状态栏时，就可以看到设置的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        //当通知被点击的时候，跳转到MainActivity中
        builder.setContentIntent(pi);
        //设置通知的标题
        builder.setContentTitle(title);
        if(progress>0){
            //当progress大于或等于0时，才需要显示下载进度
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }
}
