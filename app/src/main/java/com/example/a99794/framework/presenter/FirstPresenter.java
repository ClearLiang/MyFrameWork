package com.example.a99794.framework.presenter;


import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.presenter.viewinterface.FirstViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by 99794 on 2018/4/13.
 */

public class FirstPresenter extends BasePresenter<FirstViewInterface> {
    FirstViewInterface firstViewInterface;

    public FirstPresenter(FirstViewInterface firstViewInterface) {
        this.firstViewInterface = firstViewInterface;
    }

    public void register(){
        addSubscription(apiStores.register("", ""), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    LogUtils.i(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
