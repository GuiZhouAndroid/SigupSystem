package com.zhangsong.com.signup_system.adapter.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.zhangsong.com.signup_system.MainActivity;
import com.zhangsong.com.signup_system.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的adapter
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MyItemClickListener mItemClickListener;
    private Context mContext;
    private List<String> mList;


    public RecyclerViewAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_recyclerview, null);
        //将全局的监听传递给holder
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //给空间赋值
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;

        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}