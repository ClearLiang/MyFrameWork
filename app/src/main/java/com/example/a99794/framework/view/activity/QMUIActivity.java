package com.example.a99794.framework.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.R;
import com.example.a99794.framework.utils.ToastUtil;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

/**
 * Created by 99794 on 2018/5/21.
 */

public class QMUIActivity extends AppCompatActivity {

    private QMUIGroupListView groupListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmui);

        initQMUI();

    }

    private void initQMUI() {
        groupListView = findViewById(R.id.groupListView);

        QMUICommonListItemView itemWithSwitch = groupListView.createItemView("Item 5");
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastUtils.showShort("checked = " + isChecked);
            }
        });

        QMUIGroupListView.newSection(this)
                .setTitle("Section 1: 默认提供的样式")
                .setDescription("Section 1 的描述")
                .addItemView(itemWithSwitch, null)
                .addTo(groupListView);
    }
}
