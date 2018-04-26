package com.example.a99794.framework.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.adapter.MyPagerAdapter;
import com.example.a99794.framework.presenter.FragmentActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.FragmentActivityInterface;
import com.example.a99794.framework.view.base.BaseActivity;
import com.example.a99794.framework.view.base.DataGenerator;
import com.example.a99794.framework.view.fragment.FirstFragment;
import com.example.a99794.framework.view.fragment.SecondFragment;
import com.example.a99794.framework.view.fragment.ThirdFragment;
import com.example.a99794.mytest.R;

import java.util.ArrayList;

/**
 *@作者 ClearLiang
 *@日期 2018/4/24
 *@描述 @desc
 **/

public class FragmentActivity extends BaseActivity<FragmentActivityInterface, FragmentActivityPresenter> implements FragmentActivityInterface {

    private ViewPager mViewPager;
    private TabLayout bottomTabLayout;
    private Fragment[] mFragments = DataGenerator.getFragments();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        initView();
        initViewPager();

        //initViewPager_1();
    }


    private void initViewPager() {
        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        // 装填
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());
        // 创建ViewPager适配器
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.setFragments(fragments);
        // 给ViewPager设置适配器
        mViewPager.setAdapter(myPagerAdapter);
        // TabLayout 指示器 (记得自己手动创建4个Fragment,注意是 app包下的Fragment 还是 V4包下的 Fragment)
        bottomTabLayout.addTab(bottomTabLayout.newTab());
        bottomTabLayout.addTab(bottomTabLayout.newTab());
        bottomTabLayout.addTab(bottomTabLayout.newTab());
        // 使用 TabLayout 和 ViewPager 相关联
        bottomTabLayout.setupWithViewPager(mViewPager);

        // TabLayout指示器添加文本
        bottomTabLayout.getTabAt(0).setText(DataGenerator.mTabTitle[0]);
        bottomTabLayout.getTabAt(1).setText(DataGenerator.mTabTitle[1]);
        bottomTabLayout.getTabAt(2).setText(DataGenerator.mTabTitle[2]);

        bottomTabLayout.getTabAt(0).setIcon(DataGenerator.mTabResPressed[0]);
        bottomTabLayout.getTabAt(1).setIcon(DataGenerator.mTabRes[1]);
        bottomTabLayout.getTabAt(2).setIcon(DataGenerator.mTabRes[2]);

        /*bottomTabLayout.getTabAt(0).setCustomView(DataGenerator.getTabView(this,0));
        bottomTabLayout.getTabAt(1).setCustomView(DataGenerator.getTabView(this,1));
        bottomTabLayout.getTabAt(2).setCustomView(DataGenerator.getTabView(this,2));*/

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.i("滑动一个"+tab.getPosition());
                //改变Tab 状态
                for(int i=0;i< bottomTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        bottomTabLayout.getTabAt(i).setIcon(DataGenerator.mTabResPressed[i]);
                    }else{
                        bottomTabLayout.getTabAt(i).setIcon(DataGenerator.mTabRes[i]);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void intData() {

    }

    @Override
    protected void initEvent() {

    }

    private void initViewPager_1() {
        // 提供自定义的布局添加Tab
        for(int i=0;i<3;i++){
            bottomTabLayout.addTab(bottomTabLayout.newTab().setCustomView(DataGenerator.getTabView(this,i)));
        }

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                //改变Tab 状态
                for(int i=0;i< bottomTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        bottomTabLayout.removeTabAt(i);
                        bottomTabLayout.getTabAt(i)
                                .setCustomView(DataGenerator.getTabPressedView(FragmentActivity.this,i));
                                //.setIcon(getResources().getDrawable(DataGenerator.mTabResPressed[i]));
                                //.setIcon(getResources().getDrawable(R.drawable.qmui_icon_checkbox_checked));


                    }else{
                        bottomTabLayout.removeTabAt(i);
                        bottomTabLayout.getTabAt(i)
                                .setCustomView(DataGenerator.getTabView(FragmentActivity.this,i));
                                //.setIcon(getResources().getDrawable(DataGenerator.mTabRes[i]));
                                //.setIcon(getResources().getDrawable(R.drawable.qmui_icon_checkbox_checked));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*bottomTabLayout.addTab(bottomTabLayout.newTab()
                //.setIcon(getResources().getDrawable(DataGenerator.mTabRes[0]))
                .setIcon(getResources().getDrawable(R.drawable.qmui_icon_checkbox_normal))
                .setText(DataGenerator.mTabTitle[0]));
        bottomTabLayout.addTab(bottomTabLayout.newTab()
                //.setIcon(getResources().getDrawable(DataGenerator.mTabRes[1]))
                .setIcon(getResources().getDrawable(R.drawable.qmui_icon_checkbox_normal))
                .setText(DataGenerator.mTabTitle[1]));
        bottomTabLayout.addTab(bottomTabLayout.newTab()
                //.setIcon(getResources().getDrawable(DataGenerator.mTabRes[2]))
                .setIcon(getResources().getDrawable(R.drawable.qmui_icon_checkbox_normal))
                .setText(DataGenerator.mTabTitle[2]));*/

        //bottomTabLayout.setupWithViewPager(mViewPager);
    }

    private void onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragments[0];
                break;
            case 1:
                fragment = mFragments[1];
                break;
            case 2:
                fragment = mFragments[2];
                break;
            default:
                break;
        }
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }

    @Override
    protected FragmentActivityPresenter createPresenter() {
        return new FragmentActivityPresenter(this);
    }

    private void initView() {
        bottomTabLayout = findViewById(R.id.bottom_tab_layout);
        mViewPager = findViewById(R.id.viewPager);
    }

}
