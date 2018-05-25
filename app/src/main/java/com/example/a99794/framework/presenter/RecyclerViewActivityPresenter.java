package com.example.a99794.framework.presenter;

import com.example.a99794.framework.presenter.viewinterface.RecyclerViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * Created by 99794 on 2018/5/10.
 */

public class RecyclerViewActivityPresenter extends BasePresenter<RecyclerViewInterface> {
    RecyclerViewInterface mRecyclerViewInterface;

    public RecyclerViewActivityPresenter(RecyclerViewInterface recyclerViewInterface) {
        mRecyclerViewInterface = recyclerViewInterface;
    }
}
