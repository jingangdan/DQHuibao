package com.dq.huibao.adapter.pingo;

import android.content.Context;
import android.graphics.Paint;
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
import com.dq.huibao.bean.pingo.GoodsListTop;
import com.dq.huibao.bean.pingo.PinGoIndexMoreGoods;
import com.dq.huibao.utils.BaseRecyclerViewHolder;
import com.dq.huibao.utils.HttpPath;
import com.dq.huibao.utils.ImageUtils;

import java.util.List;

/**
 * 同学拼go上边横向的商品列表adapter
 * Created by jingang on 2018/1/10.
 */

public class PinGoGoodsTopAdapter extends RecyclerView.Adapter<PinGoGoodsTopAdapter.MyViewHolder> {
    private Context mContext;
    private List<GoodsListTop.DataBean.ListBean> listBean;
    private OnItemClickListener onItemClickListener;
    private int layoutId;
    public PinGoGoodsTopAdapter(Context mContext, List<GoodsListTop.DataBean.ListBean> appimgList) {
        this.mContext = mContext;
        this.listBean = appimgList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder vh = new MyViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_pinggo_goods_top, viewGroup, false)
        );
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int i) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition(); // 1
                    onItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });

        }
        holder.price.setText(listBean.get(i).getMarketprice());
        holder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.oldPrice.setText(listBean.get(i).getProductprice());
        Glide.with(mContext).load(ImageUtils.getImagePath(listBean.get(i)
                .getThumb()))
                .placeholder(R.mipmap.icon_empty002)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.img);
//        ImageUtils.loadIntoUseFitWidth2(mContext, HttpPath.NEW_HEADER + listBean.get(i).getThumb(), R.mipmap.icon_stub, holder.img);
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    public class MyViewHolder extends BaseRecyclerViewHolder {
        private ImageView img;
        private TextView price,oldPrice;

        public MyViewHolder(View view) {
            super(view);
            img =  view.findViewById(R.id.item_pingo_goods_top_image);
            price =  view.findViewById(R.id.item_pingo_goods_top_price);
            oldPrice =  view.findViewById(R.id.item_pingo_goods_top_oldprice);
        }
    }
}
