package com.example.a99794.framework.presenter;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.model.bean.UserBean;
import com.example.a99794.framework.presenter.viewinterface.KeyboardViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 *@作者 ClearLiang
 *@日期 2018/4/26
 *@描述 @desc
 **/

public class KeyboardPresenter extends BasePresenter<KeyboardViewInterface> {
    KeyboardViewInterface keyboardViewInterface;

    public KeyboardPresenter(KeyboardViewInterface keyboardViewInterface) {
        this.keyboardViewInterface = keyboardViewInterface;
    }

}
