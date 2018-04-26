package com.example.a99794.framework.presenter;

import com.example.a99794.framework.presenter.viewinterface.FragmentActivityInterface;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * Created by 99794 on 2018/4/24.
 */

public class FragmentActivityPresenter extends BasePresenter<FragmentActivityInterface> {

    FragmentActivityInterface mFragmentActivityInterface;

    public FragmentActivityPresenter(FragmentActivityInterface fragmentActivityInterface) {
        mFragmentActivityInterface = fragmentActivityInterface;
    }

}
