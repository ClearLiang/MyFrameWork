package com.example.a99794.framework.view.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.utils.AppManager;
import com.example.a99794.framework.utils.EventBusUtils;
import com.example.a99794.framework.utils.KeyboardUtils;
import com.example.a99794.framework.utils.RSAUtils;
import com.example.a99794.framework.view.widget.keyboard.KeyBoardDialogUtils;
import com.example.a99794.framework.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * @作者 ClearLiang
 * @日期 2018/4/13
 * @描述 Activity的基类
 **/

abstract public class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity implements GlobalConstants{
    protected Bundle mBundle = new Bundle();
    protected T mPresenter;
    protected KeyboardUtils keyboardUtils;
    protected KeyBoardDialogUtils keyBoardDialogUtils;
    protected boolean isTouchEvent = true;

    protected abstract void intData();
    protected abstract void initEvent();

    protected abstract T createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        AppManager.getAppManager().addActivity(this);
        // 隐藏软键盘
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        LogUtils.i(getClass().getSimpleName());

        RSAUtils.getRSAUtils().generateRSAKeyPair();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("onStart");
        intData();
        initEvent();
        //注册EventBus
        if (isRegisterEventBus()) {
            EventBusUtils.register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.datachView();
        LogUtils.d("onDestroy");
        AppManager.getAppManager().finishActivity(this);
        EventBusUtils.unregister(this);
    }

    protected void openActivity(Class<?> mClass) {
        openActivity(mClass, null);
    }

    protected void openActivity(Class<?> mClass, Bundle mBundle) {
        Intent mIntent = new Intent(this, mClass);
        if (null != mBundle) {
            mIntent.putExtras(mBundle);
        }
        LogUtils.d("openActivity with bundle");
        startActivity(mIntent);
        // 设置开关Activity的动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void openActivityForResult(Class<?> mClass, int requestCode) {
        openActivityForResult(mClass,requestCode,null);
    }

    protected void openActivityForResult(Class<?> mClass, int requestCode,Bundle bundle) {
        Intent mIntent = new Intent(this, mClass);
        if(bundle != null){
            mIntent.putExtras(bundle);
        }
        startActivityForResult(mIntent, requestCode);
        // 设置开关Activity的动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void openService(Class<?> mClass, Bundle bundle){
        Intent intent = new Intent(this, mClass);
        intent.putExtras(bundle);
        startService(intent);
    }

    @Override
    public void finish() {
        super.finish();
        LogUtils.d("finish");
        // 设置开关Activity的动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**  Rx点击事件防抖动  */
    protected void setClick(View view, Action1<Void> action1) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(action1);
    }
    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**  显示支付密码键盘  */
    protected void showKeyBoard(final Activity activity, final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int inputType = editText.getInputType();
                editText.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
                keyboardUtils = new KeyboardUtils(activity, editText);
                keyboardUtils.showKeyboard();
                editText.setInputType(inputType);
                return false;
            }
        });



    }
    /**  显示安全键盘  */
    protected void showSafeKeyBoard(final Activity activity, final EditText editText){
        keyBoardDialogUtils = new KeyBoardDialogUtils(activity,editText);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBoardDialogUtils.show();
            }
        });

    }

    /**  隐藏键盘  */
    protected void hideKeyboard() {
        if(keyboardUtils != null){
            keyboardUtils.hideKeyboard();
        }
        if(keyBoardDialogUtils != null){
            keyBoardDialogUtils.dismiss();
        }
    }

    /**  点击空白处键盘消失，默认消失  */
    protected void setOnTouchEvent(boolean flag){
        isTouchEvent = flag;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*//隐藏系统默认的输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);*/

        if(isTouchEvent){
            //点击空白处键盘消失
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (this.getCurrentFocus() != null) {
                    if (this.getCurrentFocus().getWindowToken() != null) {
                        hideKeyboard();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
