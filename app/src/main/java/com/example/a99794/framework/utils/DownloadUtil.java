package com.example.a99794.framework.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.view.activity.MainActivity;
import com.example.a99794.framework.view.base.GlobalConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @作者 ClearLiang
 * @日期 2018/5/8
 * @描述 下载工具类
 **/

public class DownloadUtil {
    private String SDCardRoot;//根目录
    private String DirPath;   //文件目录
    private String FilePath;  //文件目录
    private int temp = 0;
    private boolean isShowNotice = true;//是否显示通知

    private RemoteViews remoteViews;
    private Context mContext;

    public DownloadUtil(Context context,boolean isShowNotification) {
        mContext = context;
        isShowNotice = isShowNotification;
        SDCardRoot = Environment.getExternalStorageDirectory().getPath() + File.separator;
        //SDCardRoot = Environment.getExternalStorageDirectory().getPath() + File.separator+destFileDir + File.separator + destFileName;
        LogUtils.i("SDCardRoot：" + SDCardRoot);
        showNotification();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 下载中
                    int progress = (int) msg.obj;
                    LogUtils.i("下载中..." + progress + "%");
                    if(isShowNotice){
                        remoteViews.setTextViewText(R.id.tv_widget_remote_1, "下载中..." + progress + "%");
                        NotificationUtil.getNotificationUtils(mContext).updataNotification();
                    }
                    break;
                case 2:
                    // 下载完毕，取消通知
                    LogUtils.i("下载完成");
                    if(isShowNotice){
                        remoteViews.setTextViewText(R.id.tv_widget_remote_1, "下载完成");
                        NotificationUtil.getNotificationUtils(mContext).updataNotification();
                    }
                    break;
            }
        }
    };

    //在SD卡上创建目录
    private File createSDDir(String dir)throws IOException{
        File dirFile = new File(SDCardRoot+dir);
        if(!dirFile.exists()){
            dirFile.mkdir();//mkdir()只能创建一层文件目录，mkdirs()可以创建多层文件目录
        }
        DirPath = SDCardRoot+dir;
        LogUtils.i("目录："+DirPath);
        return dirFile;
    }

    //创建文件
    private File createFileInSDCard(String fileName) throws IOException {
        File file = new File(DirPath+File.separator+fileName);
        if (file.exists()){
            file.delete();
        }
        FilePath = DirPath+File.separator+fileName;
        LogUtils.i("新建文件："+FilePath);
        file.createNewFile();

        return file;
    }

    //判断文件是否存在
    public boolean isFileExist(String fileName){
        File file=new File(DirPath+File.separator+fileName);
        return file.exists();
    }

    //下载文件，将下载到的数据写入到SD卡中
    public void downLoadFile(String url, final String fileName, final String dir){

        OkHttpUtils.get().url(url).build().execute(new Callback<File>() {
                    @Override
                    public File parseNetworkResponse(Response response, final int id) throws Exception {
                        //File file = write2SDFromInput(fileName,dir,response);
                        File file=null;
                        InputStream is = null;
                        OutputStream output=null;
                        try {
                            createSDDir(dir);//创建目录
                            file = createFileInSDCard(fileName);//创建文件
                        } catch (IOException e) {
                            LogUtils.i("创建文件错误："+e);
                        }

                        is = response.body().byteStream();
                        final long total = response.body().contentLength();//文件长度，单位：b
                        LogUtils.i("文件大小：" + total);
                        long sum = 0;

                        try {
                            output = new FileOutputStream(file);
                            byte buffer[] = new byte[4*1024];//每次存4K
                            int temp = 0;
                            //写入数据
                            while((temp = is.read(buffer))!=-1){
                                sum += temp;
                                output.write(buffer,0,temp);
                                final long finalSum = sum;

                                inProgress(finalSum * 1.0f / total,total,id);
                                /*OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        inProgress(finalSum * 1.0f / total,total,id);
                                    }
                                });*/
                            }
                            output.flush();
                        } catch (Exception e) {
                            LogUtils.i("写数据异常："+e);
                        } finally{
                            try {
                                output.close();
                            } catch (Exception e2) {
                                LogUtils.i(e2);
                            }
                        }

                        if(file.exists()){
                            ToastUtils.showShort("下载成功！");
                        }
                        return file;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                        Message msg = new Message();
                        int progresstemp = (int) (progress * 100) ;
                        if (temp == progresstemp) {
                            return;
                        }
                        if (progresstemp < 100){
                            msg.what = 1;
                            msg.obj = progresstemp;
                            temp = progresstemp;
                        }else {
                            msg.what = 2;
                        }
                        handler.sendMessage(msg);
                    }
                });
    }

    private void showNotification() {
        if(isShowNotice){
            //1.创建RemoteViews实例
            remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_remote);
            remoteViews.setTextViewText(R.id.tv_widget_remote_1, "下载中..." );
            //remoteViews.setImageViewResource(R.id.imageView3, R.drawable.qmui_icon_checkbox_checked);
            remoteViews.setViewVisibility(R.id.imageView3,View.GONE);
            //2.构建一个打开Activity的PendingIntent
            Intent pendingIntent = new Intent(mContext,MainActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //3.创建一个Notification
            NotificationUtil.getNotificationUtils(mContext).showNotification(remoteViews,mPendingIntent);
        }

    }

}
