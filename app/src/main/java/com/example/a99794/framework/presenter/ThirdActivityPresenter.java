package com.example.a99794.framework.presenter;

import com.example.a99794.framework.presenter.viewinterface.ThirdViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * @作者 ClearLiang
 * @日期 2018/5/4
 * @描述 @desc
 **/

public class ThirdActivityPresenter extends BasePresenter<ThirdViewInterface>{
    ThirdViewInterface mThirdViewInterface;

    public ThirdActivityPresenter(ThirdViewInterface thirdViewInterface) {
        mThirdViewInterface = thirdViewInterface;
    }
}
