package com.example.a99794.framework.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a99794.framework.R;
import com.example.a99794.framework.presenter.viewinterface.FourViewInterface;
import com.example.a99794.framework.view.activity.FourActivity;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * Created by 99794 on 2018/5/14.
 */

public class FourActivityPresenter extends BasePresenter<FourViewInterface> {
    FourViewInterface mFourViewInterface;

    public FourActivityPresenter(FourViewInterface fourViewInterface) {
        mFourViewInterface = fourViewInterface;
    }


}
