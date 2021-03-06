package com.dq.huibao.adapter.index;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dq.huibao.Interface.OnItemClickListener;
import com.dq.huibao.R;
import com.dq.huibao.bean.index.Index;
import com.dq.huibao.utils.AppUtil;
import com.dq.huibao.utils.BaseRecyclerViewHolder;
import com.dq.huibao.utils.HttpPath;
import com.dq.huibao.utils.ImageUtils;

import java.util.List;

/**
 * 首页 menu适配器
 * Created by jingang on 2018/1/10.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private Context mContext;
    private List<Index.DataBean.ChildBean> menuList;
    private OnItemClickListener onItemClickListener;

    public MenuAdapter(Context mContext, List<Index.DataBean.ChildBean> menuList) {
        this.mContext = mContext;
        this.menuList = menuList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder vh = new MyViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_menu, viewGroup, false)
        );
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition(); // 1
                    onItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }

        ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.height = AppUtil.getWidth()/7;
        layoutParams.width = AppUtil.getWidth()/7;
        holder.img.setLayoutParams(layoutParams);

        Glide.with(mContext)
                .load(ImageUtils.getImagePath(menuList.get(position).getThumb()))
                .placeholder(R.mipmap.icon_empty)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.img);

        holder.text.setText("" + menuList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MyViewHolder extends BaseRecyclerViewHolder {
        private ImageView img;
        private TextView text;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.iv_item_goods_img);
            text = (TextView) view.findViewById(R.id.tv_item_goods_name);
        }
    }
}
