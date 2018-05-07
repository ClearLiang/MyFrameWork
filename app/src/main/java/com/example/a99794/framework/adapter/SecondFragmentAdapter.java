package com.example.a99794.framework.adapter;

import com.example.a99794.framework.view.base.BaseAdapter;
import com.example.a99794.framework.view.base.BaseHolder;
import com.example.a99794.framework.R;

import java.util.List;

/**
 * @作者 ClearLiang
 * @日期 2018/4/26
 * @描述 @desc
 **/

public class SecondFragmentAdapter extends BaseAdapter<String> {

    public SecondFragmentAdapter(int layoutId, List<String> list) {
        super(layoutId, list);
    }

    @Override
    protected void convert(BaseHolder holder, String item) {
        //holder.setText(R.id.asd,item).setImageResource(R.id.asd,R.drawable.bg_corner_white);
        holder.setText(R.id.tv_second,item);

    }
}
