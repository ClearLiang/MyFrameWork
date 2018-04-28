package com.example.a99794.framework.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.utils.EventBusUtils;
import com.example.a99794.mytest.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 Fragment 基类
 **/

/**
 * onAttach()  =>  onCreate()  =>  onCreateView()  =>  onActivityCreated()  =>  onStart()  =>  onResume()
 * =>  onPause()  =>  onStop()  =>  onDestroyView()  =>  onDestroy()  =>  onDetach()
 * */
public abstract class BaseFragment<V,T extends BasePresenter<V>> extends Fragment {
    protected Bundle mBundle = new Bundle();
    protected T mPresenter;
    protected Activity mActivity;

    protected abstract T createPresenter();
    protected abstract void initEvent();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) getActivity());
        initEvent();
    }

    protected void openActivity(Class<?> mClass) {
        LogUtils.d("openActivity");
        openActivity(mClass, null);
    }

    protected void openActivity(Class<?> mClass, Bundle mBundle) {
        Intent mIntent = new Intent(getActivity(), mClass);
        if (null != mBundle) {
            mIntent.putExtras(mBundle);
        }
        getActivity().startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void setClick(View view, Action1<Void> action1) {
        RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(action1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisterEventBus()) {
            EventBusUtils.register(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

}
