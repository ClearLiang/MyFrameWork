package com.example.a99794.framework.duojimenu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a99794.framework.R;
import com.example.a99794.framework.model.bean.Item;
import com.example.a99794.framework.presenter.RecyclerViewActivityPresenter;
import com.example.a99794.framework.presenter.viewinterface.RecyclerViewInterface;
import com.example.a99794.framework.view.base.BaseActivity;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.TreeRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class RecyclerViewActivity extends BaseActivity<RecyclerViewInterface, RecyclerViewActivityPresenter> implements RecyclerViewInterface {
    private Button btnRecyclerAdd, btnRecyclerShow;
    private RecyclerView mTree;

    private TreeRecyclerAdapter mAdapter;
    protected List<Node> mDatas = new ArrayList<>();

    int num = 0;

    @Override
    protected void intData() {
        // id , pid , label , 其他属性
        Item item = new Item(123, "123");
        mDatas.add(new Node("1", "-1", "文件管理系统", item));

        mDatas.add(new Node(2 + "", 1 + "", "游戏", item));
        mDatas.add(new Node(3 + "", 1 + "", "文档", item));
        mDatas.add(new Node(4 + "", 1 + "", "程序", item));
        mDatas.add(new Node(6 + "", 2 + "", "刀塔传奇", item));
        mDatas.add(new Node(5 + "", 2 + "", "war3", item));


        mDatas.add(new Node(7 + "", 4 + "", "面向对象", item));
        mDatas.add(new Node(8 + "", 4 + "", "非面向对象", item));

        mDatas.add(new Node(9 + "", 7 + "", "C++", item));
        mDatas.add(new Node(10 + "", 7 + "", "JAVA", item));
        mDatas.add(new Node(11 + "", 7 + "", "Javascript", item));
        mDatas.add(new Node(12 + "", 8 + "", "C", item));
        mDatas.add(new Node(13 + "", 12 + "", "C", item));
        /*mDatas.add(new Node(14+"", 13+"", "C"));
        mDatas.add(new Node(15+"", 14+"", "C"));
        mDatas.add(new Node(16+"", 15+"", "C"));*/
    }

    @Override
    protected void initEvent() {
        /**  添加数据  */
        setClick(btnRecyclerAdd, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                List<Node> mlist = new ArrayList<>();
                if (num == 0) {
                    mlist.add(new Node("22", "0", "我是添加的root", new FileNode()));
                }
                /*//添加一个根节点
                mlist.add(new Node("223","0","我也是添加的root节点",new FileNode()));*/
                //加在新节点上
                mlist.add(new Node("333" + num, "22", "我是添加的1" + num));
                mlist.add(new Node("44444" + num, "22", "我是添加的2" + num));
                //加到现有数据的父节点上
                mlist.add(new Node("444454" + num, "4", "我是添加的3" + num));
                num++;
                mAdapter.addData(1, mlist);
            }
        });
        /**  显示选中数据  */
        setClick(btnRecyclerShow, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                StringBuilder sb = new StringBuilder();
                final List<Node> allNodes = mAdapter.getAllNodes();
                for (int i = 0; i < allNodes.size(); i++) {
                    if (allNodes.get(i).isChecked() && allNodes.get(i).isLeaf()) {
                        sb.append(allNodes.get(i).getName() + ",");
                    }
                }
                String strNodesName = sb.toString();
                if (!TextUtils.isEmpty(strNodesName)){
                    Toast.makeText(RecyclerViewActivity.this, strNodesName.substring(0, strNodesName.length() - 1), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected RecyclerViewActivityPresenter createPresenter() {
        return new RecyclerViewActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        initView();

        mTree.setLayoutManager(new LinearLayoutManager(this));
        //第一个参数  RecyclerView
        //第二个参数  上下文
        //第三个参数  数据集
        //第四个参数  默认展开层级数 0为不展开
        //第五个参数  展开的图标
        //第六个参数  闭合的图标
        mAdapter = new SimpleTreeRecyclerAdapter(mTree, RecyclerViewActivity.this,
                mDatas, 1, R.mipmap.tree_ex, R.mipmap.tree_ec);

        mTree.setAdapter(mAdapter);

    }

    private void initView() {
        btnRecyclerAdd = findViewById(R.id.btn_recycler_add);
        btnRecyclerShow = findViewById(R.id.btn_recycler_show);
        mTree = findViewById(R.id.recyclerview);
    }
}
