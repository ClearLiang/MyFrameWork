package com.example.a99794.framework.view.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a99794.framework.view.fragment.FirstFragment;
import com.example.a99794.framework.view.fragment.SecondFragment;
import com.example.a99794.framework.view.fragment.ThirdFragment;
import com.example.a99794.mytest.R;

/**
 *@作者 ClearLiang
 *@日期 2018/4/24
 *@描述 @desc
 **/

public class DataGenerator {
    //tab标签点击前
    public static final int []mTabRes = new int[]{
            R.drawable.qmui_icon_checkbox_normal,R.drawable.qmui_icon_checkbox_normal,R.drawable.qmui_icon_checkbox_normal,
            /*R.drawable.tab_home_selector,
            R.drawable.tab_discovery_selector,
            R.drawable.tab_attention_selector,*/
    };
    //tab标签点击后
    public static final int []mTabResPressed = new int[]{
            R.drawable.qmui_icon_checkbox_checked,R.drawable.qmui_icon_checkbox_checked,R.drawable.qmui_icon_checkbox_checked
            /*R.drawable.ic_tab_strip_icon_feed_selected,
            R.drawable.ic_tab_strip_icon_category_selected,
            R.drawable.ic_tab_strip_icon_pgc_selected,*/
    };
    //tab标签text
    public static final String []mTabTitle = new String[]{
            "首页","发现","关注","我的"
    };

    public static Fragment[] getFragments(){
        Fragment fragments[] = new Fragment[4];
        fragments[0] = FirstFragment.newInstance();
        fragments[1] = SecondFragment.newInstance();
        fragments[2] = ThirdFragment.newInstance();
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.widget_tab,null);
        ImageView tabIcon = view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText  = view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }

    public static View getTabPressedView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.widget_tab,null);
        ImageView tabIcon = view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabResPressed[position]);
        TextView tabText  = view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        //tabText.setTextColor(Color.BLUE);
        return view;
    }
}
