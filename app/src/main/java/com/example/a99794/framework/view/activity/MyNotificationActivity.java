package com.example.a99794.framework.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.a99794.framework.R;

/**
 * Created by 99794 on 2018/5/2.
 */

public class MyNotificationActivity extends AppCompatActivity {

    private ImageView ivNotification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
    }

    private void initView() {
        ivNotification = findViewById(R.id.iv_notification);
    }
}
