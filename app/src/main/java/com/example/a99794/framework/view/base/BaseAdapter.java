package com.example.a99794.framework.view.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 ClearLiang
 * @日期 2018/4/26
 * @描述 @desc
 **/

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> implements View.OnClickListener {
    private List<T> mList = new ArrayList<>();
    private int layoutId;
    private OnItemClickListener mItemClickListener;

    @Override
    public void onClick(View view) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick(view);
            mItemClickListener.onItemLongClick(view);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view);
        void onItemLongClick(View view);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public BaseAdapter(int layoutId,List<T> list){
        this.layoutId=layoutId;
        this. mList=list;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =   LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        view.setOnClickListener(this);

        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        T item = mList.get(position);
        holder.itemView.setTag(position);
        convert(holder,item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mItemClickListener.onItemLongClick(view);
                return true;
            }
        });

    }

    protected void convert(BaseHolder holder, T item) {
        //什么都没有做
    }

    //获取记录数据
    @Override
    public int getItemCount() {
        return mList.size();
    }




}