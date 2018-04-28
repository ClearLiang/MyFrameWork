package com.example.a99794.framework.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.event.Event;
import com.example.a99794.framework.event.FirstEvent;
import com.example.a99794.framework.model.dao.Shop;
import com.example.a99794.framework.model.dao.ShopDao;
import com.example.a99794.framework.presenter.FirstFragmentPresenter;
import com.example.a99794.framework.presenter.viewinterface.FirstFragmentViewInterface;
import com.example.a99794.framework.utils.DaoUtils;
import com.example.a99794.framework.utils.EventBusUtils;
import com.example.a99794.framework.view.base.BaseFragment;
import com.example.a99794.mytest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.functions.Action1;

/**
 * @作者 ClearLiang
 * @日期 2018/4/13
 * @描述 第一个 fragment
 **/

public class FirstFragment extends BaseFragment<FirstFragmentViewInterface, FirstFragmentPresenter> implements FirstFragmentViewInterface {


    private int a = 0;
    private Button btnFirstDaoAdd;
    private Button btnFirstDaoDelete;
    private Button btnFirstDaoDeleteAll;
    private Button btnFirstDaoQuery;
    private Button btnFirstDaoQueryAll;
    private Button btnFirstDaoAlter;


    @Override
    protected FirstFragmentPresenter createPresenter() {
        return new FirstFragmentPresenter(this);
    }

    public static FirstFragment newInstance() {

        Bundle args = new Bundle();

        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    protected void initEvent() {
        setClick(btnFirstDaoAdd, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Shop shop = new Shop();shop.setAddress("123" + a);shop.setId((long) 123 + a);shop.setImage_url("123" + a);shop.setName("123" + a);shop.setPrice("123");shop.setSell_num(123 + a);shop.setType(1 + a);
                DaoUtils.getDaoUtils().insertData(shop);
                a++;
            }
        });
        setClick(btnFirstDaoDelete, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (DaoUtils.getDaoUtils().checkIsBlank()) {
                    LogUtils.i("123的数据为空");
                } else {
                    DaoUtils.getDaoUtils().deleteData(123);
                }
            }
        });
        setClick(btnFirstDaoDeleteAll, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (DaoUtils.getDaoUtils().checkIsBlank()) {
                    DaoUtils.getDaoUtils().deleteDataAll();
                } else {
                    LogUtils.i("数据为空");
                }
            }
        });
        setClick(btnFirstDaoQuery, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                List<Shop> shops;
                if (DaoUtils.getDaoUtils().checkIsBlank()) {
                    LogUtils.i("数据为空");
                } else {
                    shops = DaoUtils.getDaoUtils().queryData(ShopDao.Properties.Id.eq(123));
                    if(shops.size() == 0){
                        LogUtils.i("数据123不存在");
                    }else {
                        for (int i = 0;i<shops.size();i++){
                            LogUtils.i("数据为: Id:"+shops.get(i).getId()+"--Name:"+shops.get(i).getName());
                        }
                    }
                }
            }
        });
        setClick(btnFirstDaoQueryAll, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                List<Shop> shops;
                if (DaoUtils.getDaoUtils().checkIsBlank()) {
                    LogUtils.i("数据为空");
                } else {
                    shops = DaoUtils.getDaoUtils().queryAll();
                    for (int i = 0;i<shops.size();i++){
                        LogUtils.i("数据为: Id:"+shops.get(i).getId()+"--Name:"+shops.get(i).getName());
                    }
                }
            }
        });
        setClick(btnFirstDaoAlter, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Shop shop = new Shop();
                shop = DaoUtils.getDaoUtils().queryData(ShopDao.Properties.Id.eq(123)).get(0);
                shop.setName("asd");
                if (DaoUtils.getDaoUtils().checkIsBlank()) {
                    LogUtils.i("数据为空");
                } else {
                    DaoUtils.getDaoUtils().updateData(shop);
                }

            }
        });

    }

    private void initView(View view) {
        btnFirstDaoAdd = view.findViewById(R.id.btn_first_dao_add);
        btnFirstDaoDelete = view.findViewById(R.id.btn_first_dao_delete);
        btnFirstDaoDeleteAll = view.findViewById(R.id.btn_first_dao_delete_all);
        btnFirstDaoQuery = view.findViewById(R.id.btn_first_dao_query);
        btnFirstDaoQueryAll = view.findViewById(R.id.btn_first_dao_query_all);
        btnFirstDaoAlter = view.findViewById(R.id.btn_first_dao_alter);
    }

}
