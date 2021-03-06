package com.dq.huibao.adapter.pingo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.huibao.Interface.OnItemClickListener;
import com.dq.huibao.R;
import com.dq.huibao.bean.pingo.PinGoIndex;
import com.dq.huibao.bean.pingo.PinGoiQuSelect;
import com.dq.huibao.utils.BaseRecyclerViewHolder;
import com.dq.huibao.utils.HttpPath;
import com.dq.huibao.utils.ImageUtils;

import java.util.List;

/**
 * 拼go地区adapter
 * Created by jingang on 2018/1/10.
 */

public class PinGoQuYuAdapter extends RecyclerView.Adapter<PinGoQuYuAdapter.MyViewHolder> {
    private Context mContext;
    private List<PinGoiQuSelect.DataBean.ListBean> listBeans;
    private OnItemClickListener onItemClickListener;
    public PinGoQuYuAdapter(Context mContext, List<PinGoiQuSelect.DataBean.ListBean> listBeans) {
        this.mContext = mContext;
        this.listBeans = listBeans;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder vh = new MyViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_pingo_diqu, viewGroup, false)
        );
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int i) {
//        if (onItemClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
//        }
        holder.name.setText(listBeans.get(i).getRegname());
        holder.name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (nowcheckBox != null && nowcheckBox != holder.name){
                        nowcheckBox.setChecked(false);
                    }
                    int position = holder.getLayoutPosition(); // 1
                    onItemClickListener.onItemClick(holder.itemView, position); // 2
                    nowcheckBox = holder.name;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }
    CheckBox nowcheckBox;
    public class MyViewHolder extends BaseRecyclerViewHolder {
        CheckBox name;
        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.item_pingo_diqu);
        }
    }
}
