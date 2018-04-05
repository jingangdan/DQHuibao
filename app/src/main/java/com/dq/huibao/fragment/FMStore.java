package com.dq.huibao.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dq.huibao.R;
import com.dq.huibao.adapter.xstore.XStoreIndexAdapter;
import com.dq.huibao.base.BaseFragment;
import com.dq.huibao.bean.account.Login;
import com.dq.huibao.bean.xstore.XStoreGoods;
import com.dq.huibao.bean.xstore.XStoreInfo;
import com.dq.huibao.ui.LoginActivity;
import com.dq.huibao.ui.xstore.XStoreSettingActivity;
import com.dq.huibao.utils.CodeUtils;
import com.dq.huibao.utils.GsonUtil;
import com.dq.huibao.utils.HttpPath;
import com.dq.huibao.utils.HttpxUtils;
import com.dq.huibao.utils.SPUserInfo;
import com.dq.huibao.view.DoubleWaveView;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description：小店
 * Created by jingang on 2017/10/20.
 */
public class FMStore extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    @Bind(R.id.lin_xstore_nologin)
    LinearLayout linPercenNoLogin;
    @Bind(R.id.tv_base_title)
    TextView tvBaseTitle;
    @Bind(R.id.but_percen_login)
    Button butPercenLogin;
    @Bind(R.id.ptrv_xstore)
    LRecyclerView lRecyclerView;
    @Bind(R.id.tv_nologin_title)
    TextView tvNologinTitle;
    private DoubleWaveView waveView, waveView2, waveView3;

    private View view;

    /*本地轻量型缓存*/
    private SPUserInfo spUserInfo;
    private String uid = "", phone = "", token = "";

    private int page = 1, pageSize = 4;

    /**
     * 用户信息
     */
    XStoreInfo xStoreInfo;
    XStoreIndexAdapter listAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    //头布局
    View headView;
    ImageView xstoreTopBackgroundImage;
    ImageView xstoreHeadImage;
    ImageView xstoreSetting;
    TextView xstoreName,xstoreAllcount;
    /**是否是刷新*/
    private boolean isRefresh = true;

    /**小店自选状态**/
    public static boolean isZx = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, null);

        ButterKnife.bind(this, view);
        tvNologinTitle.setText("我的小店");
        //
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        lRecyclerView.setLayoutManager(manager);

        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);
        //
        listAdapter = new XStoreIndexAdapter(getActivity());
        lRecyclerViewAdapter = new LRecyclerViewAdapter(listAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        //
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.include_xstore_top, null);
        lRecyclerViewAdapter.addHeaderView(headView);
        xstoreTopBackgroundImage = headView.findViewById(R.id.xstore_top_background_image);
        xstoreHeadImage = headView.findViewById(R.id.xstore_head_image);
        xstoreSetting = headView.findViewById(R.id.xstore_setting);
        xstoreName = headView.findViewById(R.id.xstore_name);
        xstoreAllcount = headView.findViewById(R.id.xstore_allcount);
        //点击事件
        xstoreSetting.setOnClickListener(this);
        butPercenLogin.setOnClickListener(this);
        view.findViewById(R.id.iv_base_back).setVisibility(View.GONE);

        isLogin();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            System.out.println("离开FMShopCart");

        } else {
            System.out.println("刷新FMShopCart");
//            shopList.clear();
            isLogin();

        }

    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        isLogin();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        isRefresh = false;
        page++;
        getStoreList();
    }

    /**
     * 判断登录状态
     */
    @SuppressLint("WrongConstant")
    public void isLogin() {
        isRefresh = true;
        spUserInfo = new SPUserInfo(getActivity().getApplication());

        if (spUserInfo.getLogin().equals("1")) {

            if (!(spUserInfo.getLoginReturn().equals(""))) {
                Login login = GsonUtil.gsonIntance().gsonToBean(spUserInfo.getLoginReturn(), Login.class);
                uid = login.getData().getUid();
                phone = login.getData().getPhone();
                token = login.getData().getToken();
                tvBaseTitle.setText("我的小店");
                getStoreInfo();
            }

            lRecyclerView.setVisibility(View.VISIBLE);
            linPercenNoLogin.setVisibility(View.GONE);
        } else {
            lRecyclerView.setVisibility(View.GONE);
            linPercenNoLogin.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取小店信息
     */
    public void getStoreInfo() {
        getStoreList();
        Map<String, String> map = new HashMap<>();
        map.put("mid", uid);
        HttpxUtils.Get(HttpPath.PATHS + HttpPath.XSHOP_INFO, map,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        xStoreInfo = GsonUtil.gsonIntance().gsonToBean(result, XStoreInfo.class);
                        //小店自选开关状态
                        isZx = xStoreInfo.getData().getGoodstatus().equals("1");
                        updateUI(xStoreInfo);
//                        System.out.println("获取小店信息 = " + xStoreInfo.getData().getShopname());
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        System.out.println("获取小店信息 = 失败" + ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

    /**
     * 获取商品列表
     */
    public void getStoreList() {
        Map<String, String> map = new HashMap<>();
        map.put("mid", uid);
        map.put("page", "" + page);
        map.put("pagesize", "" + pageSize);
//        Log.d("mmmmmmmmm","小店首页列表:"+map.toString());
        HttpxUtils.Get(HttpPath.PATHS + HttpPath.XSHOP_GOODS, map,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        XStoreGoods storeGoods = GsonUtil.gsonIntance().gsonToBean(result, XStoreGoods.class);
//                        Log.d("mmmmmmmmm","小店首页列表:"+storeGoods.getData().getList().toString());
                        if (isRefresh){
                            xstoreAllcount.setText(storeGoods.getData().getAllcount());
                            listAdapter.clear();
                        }
                        lRecyclerView.refreshComplete(pageSize);
                        listAdapter.addAll(storeGoods.getData().getList());
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        System.out.println("获取小店信息 = 失败" + ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

    /**
     * 更新ui
     */
    public void updateUI(XStoreInfo info) {
        Glide.with(getActivity())
                .load(HttpPath.NEW_HEADER + info.getData().getThumb())
                .crossFade(1000)
                .placeholder(R.mipmap.ic_header)
                .error(R.mipmap.ic_header)
                .into(xstoreHeadImage);
        Glide.with(getActivity())
                .load(HttpPath.NEW_HEADER + info.getData().getFocusthumb())
                .crossFade(1000)
                .placeholder(R.mipmap.icon_empty001)
                .error(R.mipmap.icon_error001)
                .into(xstoreTopBackgroundImage);
        xstoreName.setText(info.getData().getShopname());
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xstore_setting:
                Intent intent = new Intent(getActivity(), XStoreSettingActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("phone", phone);
                intent.putExtra("token", token);
                startActivity(intent);
                break;
            case R.id.but_percen_login:
                //登录
                Intent intentL = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intentL, CodeUtils.CART_FM);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.CART_FM) {
            if (resultCode == CodeUtils.CONFIRM_ORDER || resultCode == CodeUtils.LOGIN) {
                isLogin();
            }
        }
    }
}
