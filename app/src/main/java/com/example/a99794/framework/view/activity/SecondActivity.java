package com.example.a99794.framework.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a99794.framework.event.Event;
import com.example.a99794.framework.presenter.SecondActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.SecondViewInterface;
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

    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {

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

        tvActivitySecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        tvActivitySecond.setText(event.getMessage());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
}
