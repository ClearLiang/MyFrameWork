package com.example.a99794.framework.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.example.a99794.framework.event.Event;
import com.example.a99794.framework.presenter.SecondActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.SecondViewInterface;
import com.example.a99794.framework.service.BindService;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.mytest.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @作者 ClearLiang
 * @日期 2018/4/25
 * @描述 @desc
 **/

public class SecondActivity extends BaseActivity<SecondViewInterface, SecondActivityPresenter> implements SecondViewInterface {
    private TextView tvActivitySecond;
    private Button button;
    private Button button2;

    private BindService service = null;

    private boolean isBound = false;

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
        tvActivitySecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ServiceUtils.startService(RemindService.class);
                //单击了“bindService”按钮
                Intent intent = new Intent(SecondActivity.this, BindService.class);
                intent.putExtra("from", "ActivityA");
                LogUtils.i("ActivityA 执行 bindService");
                bindService(intent, conn, BIND_AUTO_CREATE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ServiceUtils.stopService(RemindService.class);
                //单击了“unbindService”按钮
                if (isBound) {
                    LogUtils.i("ActivityA 执行 unbindService");
                    isBound = false;
                    unbindService(conn);
                }
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
        setContentView(R.layout.activity_second);

        initView();
    }

    private void initView() {
        tvActivitySecond = findViewById(R.id.tv_activity_second);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        tvActivitySecond.setText(event.getMessage());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
}
