package com.yalarc.douyin.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalarc.douyin.di.component.DaggerVideoComponent;
import com.yalarc.douyin.mvp.contract.VideoContract;
import com.yalarc.douyin.mvp.model.entity.DouYin;
import com.yalarc.douyin.mvp.presenter.VideoPresenter;

import com.yalarc.douyin.R;
import com.yalarc.douyin.views.DYLoadingView;
import com.yalarc.douyin.views.layoutmanager.OnViewPagerListener;
import com.yalarc.douyin.views.layoutmanager.ViewPagerLayoutManager;


import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by yalarc on 11/11/2019 18:57
 * <a href="wx:qhb0123">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * <a href="https://github.com/yalarc/DouYinAndroid">Star me</a>
 * ================================================
 */
public class VideoActivity extends BaseActivity<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.loading_view)
    DYLoadingView dyLoadingView;

    @Inject
    RxPermissions mRxPermissions;

    @Inject
    ViewPagerLayoutManager mLayoutManager;

    @Inject
    BaseQuickAdapter<DouYin, BaseViewHolder> mAdapter;

    private boolean isFirst = true;
    private int currentPosition;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_video; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        mRefreshLayout.setEnableScrollContentWhenLoaded(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.requestVideos(true);
        });
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPresenter.requestVideos(false);
        });
    }

    private void initListener() {
        //recyclerview滑动监听
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                //position = 0 未滑动状态
                mRecyclerView.post(() -> {
                    mPresenter.playVideos(0);
                });

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                //界面滑动开始 （上一个界面消失状态）
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                if (currentPosition == position) return;
                currentPosition = position;
                //界面滑动结束
                mRecyclerView.post(() -> {
                    mPresenter.playVideos(position);
                });

            }
        });
        //item 点击监听
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mPresenter.itemDetail(position);
        });
        mRefreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                if (mAdapter.getItem(currentPosition + 1) != null) {
                    mRecyclerView.smoothScrollBy(0, mRefreshLayout.getHeight());
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

            }
        });

    }

    @Override
    public void showLoading() {
        if (isFirst) {
            isFirst = false;
            dyLoadingView.setVisibility(View.VISIBLE);
            dyLoadingView.start();
        }

    }

    @Override
    public void hideLoading() {
        dyLoadingView.stop();
        dyLoadingView.setVisibility(View.GONE);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void startLoadMore() {
        //mRefreshLayout 会自动触发，无需设置
    }

    @Override
    public void endLoadMore() {
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }
}
