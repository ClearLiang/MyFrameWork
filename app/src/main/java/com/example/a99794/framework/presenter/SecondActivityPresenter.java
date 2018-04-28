package com.example.a99794.framework.presenter;

import com.example.a99794.framework.presenter.viewinterface.SecondViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * Created by 99794 on 2018/4/27.
 */

public class SecondActivityPresenter extends BasePresenter<SecondViewInterface> {
    SecondViewInterface mSecondViewInterface;

    public SecondActivityPresenter(SecondViewInterface secondViewInterface) {
        mSecondViewInterface = secondViewInterface;
    }
}
