package com.example.a99794.framework.presenter;


import com.example.a99794.framework.presenter.viewinterface.PayViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * Created by 99794 on 2018/4/17.
 */

public class PayPresenter extends BasePresenter<PayViewInterface> {

    PayViewInterface payViewInterface;

    public PayPresenter(PayViewInterface payViewInterface) {
        this.payViewInterface = payViewInterface;
    }

}
