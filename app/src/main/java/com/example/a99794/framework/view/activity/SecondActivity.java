package com.example.a99794.framework.view.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.event.Event;
import com.example.a99794.framework.presenter.SecondActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.SecondViewInterface;
import com.example.a99794.framework.service.BindService;
import com.example.a99794.framework.service.DownloadService;
import com.example.a99794.framework.utils.DownloadUtil;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.framework.view.base.GlobalConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * @作者 ClearLiang
 * @日期 2018/4/25
 * @描述 @desc
 **/

public class SecondActivity extends BaseActivity<SecondViewInterface, SecondActivityPresenter> implements SecondViewInterface {
    private TextView tvActivitySecond;
    private Button button,button2,button3;
    private MapView mMapView = null;

    private Context mContext;
    private BindService service = null;
    private boolean isBound = false;

    //1.定义不同颜色的菜单项的标识:
    final private int DOWNLOAD = 110;
    final private int GREEN = 111;
    final private int BLUE = 112;
    final private int YELLOW = 113;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBound = true;
            BindService.MyBinder myBinder = (BindService.MyBinder) binder;
            service = myBinder.getService();
            LogUtils.i("ActivityA onServiceConnected");
            int num = service.getRandomNumber();
            LogUtils.i("ActivityA 中调用 TestService的getRandomNumber方法, 结果: " + num);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            LogUtils.i("ActivityA onServiceDisconnected");
        }
    };


    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {
        setClick(tvActivitySecond, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        setClick(button, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //ServiceUtils.startService(RemindService.class);
                //单击了“bindService”按钮
                Intent intent = new Intent(SecondActivity.this, BindService.class);
                intent.putExtra("from", "ActivityA");
                LogUtils.i("ActivityA 执行 bindService");
                bindService(intent, conn, BIND_AUTO_CREATE);
            }
        });

        setClick(button2, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //ServiceUtils.stopService(RemindService.class);
                //单击了“unbindService”按钮
                if (isBound) {
                    LogUtils.i("ActivityA 执行 unbindService");
                    isBound = false;
                    unbindService(conn);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopWindow(view);
            }
        });

    }

    @Override
    protected SecondActivityPresenter createPresenter() {
        return new SecondActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_second);
        mContext = SecondActivity.this;
        initLocation();
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,DOWNLOAD,1,"下载文件");
        menu.add(1,GREEN,   2,"绿色");
        menu.add(1,BLUE,    3,"蓝色");
        menu.add(1,YELLOW,  4,"黄色");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case DOWNLOAD:
                //final DownloadUtil downloadUtil = new DownloadUtil();
                //downloadUtil.downLoadFile("https://dldir1.qq.com/qqfile/qq/QQ9.0.3/23719/QQ9.0.3.exe","qq.exe", GlobalConstants.DIRECTORY);
                Intent intent = new Intent(this,DownloadService.class);
                intent.putExtra("downUrl", "https://dldir1.qq.com/qqfile/qq/QQ9.0.3/23719/QQ9.0.3.exe");
                startService(intent);
                break;
            case GREEN:
                ToastUtils.showShort("绿色");
                break;
            case BLUE:
                ToastUtils.showShort("蓝色");
                break;
            case YELLOW:
                ToastUtils.showShort("黄色");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popup, null, false);

        final PopupWindow popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setAnimationStyle(R.anim.anim_pop);//设置加载动画
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.showAsDropDown(v,0,0, Gravity.CENTER);

        //设置popupWindow里的按钮的事件
        final TextView tv_popup_1 = view.findViewById(R.id.tv_popup_1);
        final TextView tv_popup_2 = view.findViewById(R.id.tv_popup_2);
        tv_popup_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击了"+tv_popup_1.getText().toString());
                popupWindow.dismiss();
            }
        });
        tv_popup_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击了"+tv_popup_2.getText().toString());
                popupWindow.dismiss();
            }
        });

    }

    private void initView() {
        tvActivitySecond = findViewById(R.id.tv_activity_second);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
    }

    private void initLocation(){
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        tvActivitySecond.setText(event.getMessage());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



}
