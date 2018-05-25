package com.example.a99794.framework.view.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.presenter.FourActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.FourViewInterface;
import com.example.a99794.framework.service.DownloadTaskService;
import com.example.a99794.framework.view.base.BaseActivity;

import java.util.ArrayList;


public class FourActivity extends BaseActivity<FourViewInterface, FourActivityPresenter> implements FourViewInterface, View.OnClickListener {

    private Button startDownload, pauseDownload, cancelDownload, deleteDownload;
    private MyGestureListener mMyGestureListener;
    private GestureDetector mDetector;
    private GestureOverlayView gesture;
    private GestureLibrary gestureLibrary;


    @Override
    protected FourActivityPresenter createPresenter() {
        return new FourActivityPresenter(this);
    }

    @Override
    protected void intData() {

    }

    private void initView() {
        startDownload = findViewById(R.id.start_download);
        pauseDownload = findViewById(R.id.pause_download);
        cancelDownload = findViewById(R.id.cancel_download);
        deleteDownload = findViewById(R.id.delete_download);

        gesture = findViewById(R.id.gesture);
    }

    @Override
    protected void initEvent() {
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);
        deleteDownload.setOnClickListener(this);
    }

    private DownloadTaskService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadTaskService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        initView();
        initGesture();

        //实例化GestureListener与GestureDetector对象
        mMyGestureListener = new MyGestureListener();
        mDetector = new GestureDetector(this, mMyGestureListener);

        Intent intent = new Intent(this, DownloadTaskService.class);
        startService(intent);
        //因为启动服务可以保证DownloadService一直在后台运行，绑定服务则可以让Activity和Service，进行通信，因此两个方法的调用都必不可少。
        bindService(intent, connection, BIND_AUTO_CREATE);

        /**
         *运行时权限处理：我们需要再用到权限的地方，每次都要检查是否APP已经拥有权限
         * 下载功能，需要些SD卡的权限，我们在写入之前检查是否有WRITE_EXTERNAL_STORAGE权限,没有则申请权限
         */
        if (ContextCompat.checkSelfPermission(FourActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FourActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void initGesture() {
        gestureLibrary = GestureLibraries.fromFile("mnt/sdcard/mygestures");
        if (gestureLibrary.load()) {
            ToastUtils.showShort("手势库加载成功");
        } else {
            ToastUtils.showShort("手势库加载失败");
        }

        //获取手势编辑组件后，设置相关参数
        gesture.setGestureColor(Color.GREEN);
        gesture.setGestureStrokeWidth(5);
        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
                //识别用户刚绘制的手势
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                ArrayList<String> result = new ArrayList<>();
                //遍历所有找到的Prediction对象
                for (Prediction pred : predictions) {
                    if (pred.score > 2.0) {
                        result.add("与手势【" + pred.name + "】相似度为" + pred.score);
                    }
                }
                if (result.size() > 0) {
                    ArrayAdapter<Object> adapter = new ArrayAdapter<>(FourActivity.this, android.R.layout.simple_dropdown_item_1line, result.toArray());
                    new AlertDialog.Builder(FourActivity.this)
                            .setAdapter(adapter, null)
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    View saveDialog = getLayoutInflater().inflate(R.layout.dialog_save, null, false);
                    ImageView img_show = saveDialog.findViewById(R.id.img_show);
                    final EditText edit_name = saveDialog.findViewById(R.id.edit_name);
                    Bitmap bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
                    img_show.setImageBitmap(bitmap);
                    new AlertDialog.Builder(FourActivity.this)
                            .setView(saveDialog)
                            .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 获取文件对应的手势库
                                    GestureLibrary gestureLib = GestureLibraries.fromFile("/mnt/sdcard/mygestures");

                                    gestureLib.addGesture(edit_name.getText().toString(), gesture);
                                    gestureLib.save();

                                    gestureLibrary = GestureLibraries.fromFile("mnt/sdcard/mygestures");
                                    if (gestureLibrary.load()) {
                                        ToastUtils.showShort("重新加载成功");
                                    } else {
                                        ToastUtils.showShort("重新加载失败");
                                    }

                                }
                            })
                            .setNegativeButton("取消", null).show();
                }

            }
        });
    }

    public void onClick(View v) {
        if (downloadBinder == null) {
            return;
        }
        String url = DOWNLOAD_APP_URL;
        switch (v.getId()) {
            case R.id.start_download:
                //String url="http://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadBinder.startDownload(url);
                LogUtils.i("start_download");
                break;
            case R.id.pause_download:
                downloadBinder.pauseDownload();
                LogUtils.i("pause_download");
                break;
            case R.id.cancel_download:
                downloadBinder.cancelDownload();
                LogUtils.i("cancel_download");
                break;
            case R.id.delete_download:
                downloadBinder.deleteDownload(url);
                LogUtils.i("delete_download");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定服务
        unbindService(connection);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /*//手势操作
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }*/
    private class MyGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            LogUtils.d("onDown:按下");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            LogUtils.d("onShowPress:手指按下一段时间,不过还没到长按");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            LogUtils.d("onSingleTapUp:手指离开屏幕的一瞬间");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            LogUtils.d("onScroll:在触摸屏上滑动");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            LogUtils.d("onLongPress:长按并且没有松开");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            LogUtils.d("onFling:迅速滑动，并松开");

            if (motionEvent.getY() - motionEvent1.getY() > 200) {
                startActivity(new Intent(FourActivity.this, MainActivity.class));
                ToastUtils.showShort("通过手势启动Activity");
            } else if (motionEvent.getY() - motionEvent1.getY() < 200) {
                finish();
                ToastUtils.showShort("通过手势关闭Activity");
            }
            return true;
        }
    }


    /**
     * 用户选择允许或拒绝后,会回调onRequestPermissionsResult
     *
     * @param requestCode  请求码
     * @param permissions
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
