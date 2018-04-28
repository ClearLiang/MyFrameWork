package com.example.a99794.framework.presenter;


import com.example.a99794.framework.model.bean.UserBean;
import com.example.a99794.framework.presenter.viewinterface.FirstViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;

/**
 *@作者 ClearLiang
 *@日期 2018/4/26
 *@描述 @desc
 **/

public class FirstPresenter extends BasePresenter<FirstViewInterface> {
    FirstViewInterface firstViewInterface;

    public FirstPresenter(FirstViewInterface firstViewInterface) {
        this.firstViewInterface = firstViewInterface;
    }

    public void register(){
        addSubscription(apiStores.BlogService(), new Subscriber<Result<UserBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<UserBean> userBeanResult) {

            }
        });

    }
}
