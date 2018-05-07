package com.example.a99794.framework.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a99794.framework.presenter.FirstActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.FirstViewInterface;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.framework.R;


/**
 * @作者 ClearLiang
 * @日期 2018/4/13
 * @描述 第一个Activity
 **/

public class FirstActivity extends BaseActivity<FirstViewInterface, FirstActivityPresenter> implements FirstViewInterface {


    private TextView tvFirstLogin;
    private EditText etFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
    }

    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {
        etFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFirst.requestFocus();
            }
        });
    }

    @Override
    protected FirstActivityPresenter createPresenter() {
        return new FirstActivityPresenter(this);
    }

    private void initView() {
        tvFirstLogin = findViewById(R.id.tv_first_login);
        etFirst = findViewById(R.id.et_first);

        showKeyBoard(FirstActivity.this,etFirst);
    }

}
