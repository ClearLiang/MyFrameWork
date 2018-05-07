package com.example.a99794.framework.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.adapter.MyBaseExpandableListAdapter;
import com.example.a99794.framework.model.bean.Group;
import com.example.a99794.framework.model.bean.Item;
import com.example.a99794.framework.presenter.ThirdActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.ThirdViewInterface;
import com.example.a99794.framework.utils.FingerprintUtil;
import com.example.a99794.framework.view.base.BaseActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * @作者 ClearLiang
 * @日期 2018/5/3
 * @描述 指纹验证
 * 8.0之前，指纹只能错误5次，达到5次时会禁止指纹认证，同时开启30秒倒计时，等待结束后重置错误计数，继续认证
 * 8.0之后，依然是每错误5次就会倒计时30秒，然而30秒结束后错误计数并不会被清空，
 * 8.0上加入了最大20次的限制，累计错误20次之后就无法使用指纹认证功能了，只能用密码的方式才能重置错误计数
 **/

public class ThirdActivity extends BaseActivity<ThirdViewInterface, ThirdActivityPresenter> implements ThirdViewInterface {

    private Button btnFinger, btnFingerCancel, btnList;
    FingerprintUtil fingerprintUtil;
    private int flag = 5;
    private ExpandableListView elv;

    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private Context mContext;
    private MyBaseExpandableListAdapter myAdapter = null;

    @Override
    protected ThirdActivityPresenter createPresenter() {
        return new ThirdActivityPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mContext = ThirdActivity.this;
        initView();
        initExpandableListView();
    }

    private void initView() {
        btnFinger = findViewById(R.id.btn_activity_main_finger);
        btnFingerCancel = findViewById(R.id.btn_finger_cancel);
        btnList = findViewById(R.id.btn_list);
        elv = findViewById(R.id.elv);
    }

    @Override
    protected void intData() {
        //数据准备
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        gData.add(new Group("AD"));
        gData.add(new Group("AP"));
        gData.add(new Group("TANK"));

        lData = new ArrayList<Item>();

        //AD组
        lData.add(new Item(R.mipmap.iv_lol_icon3,"剑圣"));
        lData.add(new Item(R.mipmap.iv_lol_icon4,"德莱文"));
        lData.add(new Item(R.mipmap.iv_lol_icon13,"男枪"));
        lData.add(new Item(R.mipmap.iv_lol_icon14,"韦鲁斯"));
        iData.add(lData);
        //AP组
        lData = new ArrayList<Item>();
        lData.add(new Item(R.mipmap.iv_lol_icon1, "提莫"));
        lData.add(new Item(R.mipmap.iv_lol_icon7, "安妮"));
        lData.add(new Item(R.mipmap.iv_lol_icon8, "天使"));
        lData.add(new Item(R.mipmap.iv_lol_icon9, "泽拉斯"));
        lData.add(new Item(R.mipmap.iv_lol_icon11, "狐狸"));
        iData.add(lData);
        //TANK组
        lData = new ArrayList<Item>();
        lData.add(new Item(R.mipmap.iv_lol_icon2, "诺手"));
        lData.add(new Item(R.mipmap.iv_lol_icon5, "德邦"));
        lData.add(new Item(R.mipmap.iv_lol_icon6, "奥拉夫"));
        lData.add(new Item(R.mipmap.iv_lol_icon10, "龙女"));
        lData.add(new Item(R.mipmap.iv_lol_icon12, "狗熊"));
        iData.add(lData);

    }

    @Override
    protected void initEvent() {

        setClick(btnFinger, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startFinger();
            }
        });

        setClick(btnFingerCancel, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                closeFinger();
            }
        });

        setClick(btnList, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
    }

    @SuppressLint("NewApi")
    private void startFinger() {
        fingerprintUtil = new FingerprintUtil();
        if (fingerprintUtil.setFinger(ThirdActivity.this)) {
            fingerprintUtil.startListening(new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
                    Toast.makeText(ThirdActivity.this, "多次验证错误，请1分钟后再试", Toast.LENGTH_SHORT).show();
                    //LogUtils.i(errString);

                    Intent rIntent = new Intent();
                    rIntent.putExtra(Constant.CODED_CONTENT, "-1");
                    setResult(RESULT_OK, rIntent);
                    finish();
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    Toast.makeText(ThirdActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
                    LogUtils.i("识别成功");
                            /*Intent intent = new Intent(ThirdActivity.this,MainActivity.class);
                            intent.putExtra(Constant.CODED_CONTENT,"0");
                            startActivityForResult(intent,RESULT_OK);*/

                    Intent rIntent = new Intent();
                    rIntent.putExtra(Constant.CODED_CONTENT, "0");
                    setResult(RESULT_OK, rIntent);
                    finish();
                }

                @Override
                public void onAuthenticationFailed() {
                    flag--;
                    if (flag != 0) {
                        Toast.makeText(ThirdActivity.this, "指纹识别失败，还剩下" + flag + "次机会", Toast.LENGTH_SHORT).show();
                        LogUtils.i("指纹识别失败，还剩下" + flag + "次机会");
                    }
                }
            });
        }
    }

    private void closeFinger() {
        fingerprintUtil.closeFinger();
    }

    private void initExpandableListView(){

        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        elv.setAdapter(myAdapter);

        //为列表设置点击事件
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(mContext, "你点击了：" + iData.get(groupPosition).get(childPosition).getiName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

}
