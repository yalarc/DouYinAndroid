package com.yalarc.douyin.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.internal.LinkedTreeMap;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import cn.jzvd.JzvdStd;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;
import com.yalarc.douyin.R;
import com.yalarc.douyin.mvp.contract.VideoContract;
import com.yalarc.douyin.mvp.model.entity.BaseResponse;
import com.yalarc.douyin.mvp.model.entity.DouYin;
import com.yalarc.douyin.mvp.model.entity.Video;
import com.yalarc.douyin.views.layoutmanager.OnViewPagerListener;
import com.yalarc.douyin.views.layoutmanager.ViewPagerLayoutManager;
import com.yalarc.douyin.views.player.DYJzvdStd;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 11/11/2019 18:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class VideoPresenter extends BasePresenter<VideoContract.Model, VideoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    AppManager mAppManager;

    @Inject
    List<DouYin> mVideos;

    @Inject
    BaseQuickAdapter<DouYin, BaseViewHolder> mAdapter;

    @Inject
    ViewPagerLayoutManager mLayoutManager;

    private int page = 1;//页码
    private boolean isFirst = true;

    private DYJzvdStd dyVideoPlayer;
    private ImageButton btnPlay;


    @Inject
    public VideoPresenter(VideoContract.Model model, VideoContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        JzvdStd.goOnPlayOnResume();
        requestVideos(true);//打开 App 时自动加载列表

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        JzvdStd.goOnPlayOnPause();
        requestVideos(true);//打开 App 时自动加载列表

    }

    public void requestVideos(final boolean pullToRefresh) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                requestFromModel(pullToRefresh);
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    private void requestFromModel(boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;//下拉刷新默认只请求第一页
        } else {
            page++;
        }

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        boolean isEvictCache = pullToRefresh;//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }

        mModel.getVideos(page, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(2, 10))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<Video>(mErrorHandler) {
                    @Override
                    public void onNext(Video video) {
                        if (pullToRefresh && video.getData() != null && video.getData().getVideos() != null)
                            mAdapter.replaceData(video.getData().getVideos());
                        else
                            mAdapter.addData(video.getData().getVideos());
                    }
                });
    }

    public void playVideos(int position) {

        dyVideoPlayer = (DYJzvdStd) mAdapter.getViewByPosition(position, R.id.dy_video_player);
        btnPlay = (ImageButton) mAdapter.getViewByPosition(position, R.id.btn_play);
        JzvdStd.goOnPlayOnPause();//暂停上一个视频播放
        //先把播放按钮取消
        if (btnPlay != null && btnPlay.getVisibility() != View.GONE) {
            btnPlay.setVisibility(View.GONE);
        }
        //视频播放
        if (dyVideoPlayer != null) {
            dyVideoPlayer.startVideo();
        }

    }

    public void itemDetail(int position) {
        btnPlay = (ImageButton) mAdapter.getViewByPosition(position, R.id.btn_play);
        if (dyVideoPlayer == null) return;
        if (dyVideoPlayer.state == JzvdStd.STATE_PLAYING) {
            dyVideoPlayer.goOnPlayOnPause();
            btnPlay.setVisibility(View.VISIBLE);
        } else if (dyVideoPlayer.state == JzvdStd.STATE_PAUSE) {
            dyVideoPlayer.goOnPlayOnResume();
            btnPlay.setVisibility(View.GONE);
        } else {
            dyVideoPlayer.startVideo();
            btnPlay.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mVideos = null;
    }
}
