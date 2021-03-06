package com.example.a99794.framework.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.a99794.framework.presenter.PayActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.EditCompleListener;
import com.example.a99794.framework.presenter.viewinterface.PayViewInterface;
import com.example.a99794.framework.utils.KeyboardUtil;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.framework.view.widget.FontTextView;
import com.example.a99794.framework.view.widget.PayEdit;
import com.example.a99794.framework.R;
import com.yzq.zxinglibrary.common.Constant;
import com.zhy.autolayout.AutoRelativeLayout;

import rx.functions.Action1;

/**
 *@作者 ClearLiang
 *@日期 2018/4/20
 *@描述 密码支付界面
 **/

public class PayActivity extends BaseActivity<PayViewInterface, PayActivityPresenter> implements PayViewInterface {
    private PayEdit payedit;
    private KeyboardUtil keyboardUtil = null;
    private FontTextView tvPwDlgCancel;
    private String flag = "0"; // 0设置了密码  1未设置密码
    private AutoRelativeLayout arl1;
    private AutoRelativeLayout arl2;
    private TextView tvQuickSetpw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initView();
    }

    private void initView() {
        payedit = findViewById(R.id.payedit);
        tvPwDlgCancel = findViewById(R.id.tv_pw_dlg_cancel);
        arl1 = findViewById(R.id.arl_1);
        arl2 = findViewById(R.id.arl_2);

        Intent intent = getIntent();
        flag = intent.getStringExtra("key");

        keyboardUtil = new KeyboardUtil(PayActivity.this, payedit.getCurrentEdit());
        keyboardUtil.setPreviewEnabled(true);

        if (flag.equals("1")) {
            arl1.setVisibility(View.INVISIBLE);
            arl2.setVisibility(View.VISIBLE);
            keyboardUtil.hideKeyboard();
        } else {
            arl1.setVisibility(View.VISIBLE);
            arl2.setVisibility(View.INVISIBLE);
            keyboardUtil.showKeyboard();
        }

        tvQuickSetpw = findViewById(R.id.tv_quick_setpw);
    }

    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {

        payedit.setEditCompleListener(new EditCompleListener() {
            @Override
            public void onNumCompleted(String num) {
                //LogUtils.i("PayActivity收到PayEdit结果：" + num);
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra(Constant.CODED_CONTENT, num);
                //设置返回数据
                setResult(RESULT_OK, intent);
                //关闭Activity
                finish();
            }
        });
        setClick(tvPwDlgCancel, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        setClick(tvQuickSetpw, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

    }


    @Override
    protected PayActivityPresenter createPresenter() {
        return new PayActivityPresenter(this);
    }

}
