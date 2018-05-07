package com.example.a99794.framework.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a99794.framework.presenter.viewinterface.EditCompleListener;
import com.example.a99794.framework.R;
import com.zhy.autolayout.AutoLinearLayout;

;

/**
 * @作者 ClearLiang
 * @日期 2018/4/17
 * @描述 自定义六位数支付密码组件
 *       etPay：TextView， etPay1：ImageView
 **/

public class PayEdit extends AutoLinearLayout {
    private LayoutInflater inflater;
    private View contentView;
    private Context context;
    private StringBuilder stringBuilder;
    private ImageView ivPw1,ivPw2,ivPw3,ivPw4,ivPw5,ivPw6;
    private EditText etPay;
    private ImageView[] imageViews;

    private int flag = 0;//0是自定义  1是和项目的相同

    //密码输入完成后的回调
    public EditCompleListener mListener;

    public void setEditCompleListener(EditCompleListener mListener) {
        this.mListener = mListener;
    }

    public PayEdit(Context context) {
        super(context);
        new PayEdit(context,null);
    }

    public PayEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
        stringBuilder = new StringBuilder();
        initView();
        initEvent();
    }

    private void initView() {
        contentView = inflater.inflate(R.layout.widget_pay, null);
            ivPw1 = contentView.findViewById(R.id.iv_pw_1);
            ivPw2 = contentView.findViewById(R.id.iv_pw_2);
            ivPw3 = contentView.findViewById(R.id.iv_pw_3);
            ivPw4 = contentView.findViewById(R.id.iv_pw_4);
            ivPw5 = contentView.findViewById(R.id.iv_pw_5);
            ivPw6 = contentView.findViewById(R.id.iv_pw_6);
            etPay  = contentView.findViewById(R.id.et_test);
            imageViews = new ImageView[]{ivPw1,ivPw2,ivPw3,ivPw4,ivPw5,ivPw6};

        LinearLayout.LayoutParams lParams = new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        this.addView(contentView, lParams);

    }

    private void initEvent(){
        etPay.addTextChangedListener(mTextWatcher);
        etPay.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPay.setOnKeyListener(onKeyListener);

    }

    /*系统键盘删除键的监听,自定义键盘无用*/
    OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {

            if ((keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP)) {
                delTextValue();
                return true;
            }
            return false;
        }
    };

    private void delTextValue(){
        String str = stringBuilder.toString();
        int len = str.length();
        if (len == 0) {
            return;
        }
        if (len > 0 && len <= 6) {
            stringBuilder.delete(len - 1, len);
        }
        imageViews[len - 1].setVisibility(INVISIBLE);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.toString().length() == 0){
                //初始化
                imageViews[0].setVisibility(INVISIBLE);
                stringBuilder.delete(0,stringBuilder.length());
                return;
            }else if(editable.toString().length() == 6){
                //LogUtils.i("已经六位了："+editable.toString());
                if (mListener != null) {
                    mListener.onNumCompleted(editable.toString());
                }
            }else {
                /*LogUtils.i("stringBuilder长度："+stringBuilder.length());
                LogUtils.i("editable长度："     +editable.toString().length());*/

                if(stringBuilder.length()>editable.toString().length()){
                    imageViews[stringBuilder.length()-1].setVisibility(INVISIBLE);
                }else {
                    imageViews[stringBuilder.length()].setVisibility(VISIBLE);
                }
                stringBuilder.delete(0,stringBuilder.length());
                stringBuilder.append(editable.toString());
            }

        }
    };

    public EditText getCurrentEdit(){
        return this.etPay;
    }
}
