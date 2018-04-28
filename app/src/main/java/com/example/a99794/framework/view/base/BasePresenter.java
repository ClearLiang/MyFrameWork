package com.example.a99794.framework.view.base;


import com.example.a99794.framework.model.api.ApiService;
import com.example.a99794.framework.model.api.AppClient;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 Presenter基类
 **/
public abstract class BasePresenter<T> implements GlobalConstants{
    protected static final String TAG = "BasePresenter";

    Reference<T> mViewRef;//防止发生内存泄露，使用弱引用
    protected ApiService apiStores;
    private CompositeSubscription mCompositeSubscription;

    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
        apiStores = AppClient.retrofit().create(ApiService.class);
    }

    protected T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void datachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        onUnsubscribe();
    }


    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

}
