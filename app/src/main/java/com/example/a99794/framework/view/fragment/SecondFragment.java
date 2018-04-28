package com.example.a99794.framework.view.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.adapter.SecondFragmentAdapter;
import com.example.a99794.framework.presenter.FirstFragmentPresenter;
import com.example.a99794.framework.presenter.viewinterface.FirstFragmentViewInterface;
import com.example.a99794.framework.service.BindService;
import com.example.a99794.framework.view.base.BaseAdapter;
import com.example.a99794.framework.view.base.BaseFragment;
import com.example.a99794.framework.view.widget.GlideImageLoader;
import com.example.a99794.mytest.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * @作者 ClearLiang
 * @日期 2018/4/13
 * @描述 第二个 fragment
 **/

public class SecondFragment extends BaseFragment<FirstFragmentViewInterface, FirstFragmentPresenter> implements FirstFragmentViewInterface {
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private RecyclerView rvSecond;
    private ArrayList<String> list_data;

    private BindService bindService = null;
    private boolean isBound = false;
    private Intent intent = null;
    private Button button,button2,button3,button4;
    private Context mContext;


    @Override
    protected FirstFragmentPresenter createPresenter() {
        return new FirstFragmentPresenter(this);
    }

    public static SecondFragment newInstance() {

        Bundle args = new Bundle();

        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            BindService.MyBinder binder = (BindService.MyBinder) iBinder;
            bindService = binder.getService();
            int num = bindService.getRandomNumber();
            LogUtils.i("numA=" + num);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected void initEvent() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), BindService.class);
                intent.putExtra("from", "ActivityA");
                mContext.bindService(intent, conn, BIND_AUTO_CREATE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBound = false;
                mContext.unbindService(conn);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        initData();
        initView(view);
        initBanner(view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        rvSecond.setLayoutManager(new LinearLayoutManager(getContext()));

        SecondFragmentAdapter secondFragmentAdapter = new SecondFragmentAdapter(R.layout.item_rv, list_data);
        secondFragmentAdapter.setItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rvSecond.getChildAdapterPosition(view);
                ToastUtils.showShort("onItemClick-点击了位置:" + position);
            }

            @Override
            public void onItemLongClick(View view) {
                int position = rvSecond.getChildAdapterPosition(view);
                ToastUtils.showShort("onItemLongClick-点击了位置:" + position);
            }
        });
        rvSecond.setAdapter(secondFragmentAdapter);
    }

    private void initData() {
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_data = new ArrayList<>();

        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        for (int i = 0; i < 10; i++) {
            list_data.add(String.valueOf("结果：" + i));
        }

    }

    private void initBanner(View view) {
        banner = view.findViewById(R.id.banner);

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                LogUtils.i("你点了第" + (position + 1) + "张轮播图");
            }
        });
        //必须最后调用的方法，启动轮播图。
        banner.start();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView(View view) {
        rvSecond = view.findViewById(R.id.rv_second);
        button   = view.findViewById(R.id.button);
        button2  = view.findViewById(R.id.button2);
        button3  = view.findViewById(R.id.button3);
        button4  = view.findViewById(R.id.button4);
    }


}
