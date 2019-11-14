package com.yalarc.douyin.views.player;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.yalarc.douyin.R;

import cn.jzvd.JzvdStd;

/**
 * ================================================
 * Description:
 * <p> 抖音播放器
 * Created by yalarc on 11/12/2019 18:57
 * <a href="wx:qhb0123">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * <a href="https://github.com/yalarc/DouYinAndroid">Star me</a>
 * ================================================
 */
public class DYJzvdStd extends JzvdStd {

    private SubListener subListener;

    public void setSubListener(SubListener subListener) {
        this.subListener = subListener;
    }

    public DYJzvdStd(Context context) {
        super(context);
    }

    public DYJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dy_jzvd_std;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (subListener != null) {
            new Handler().postDelayed(() -> {
                if (state == JzvdStd.STATE_PREPARING) {
                    subListener.setOnPreparing();
                }
            }, 500);
            subListener.setOnPrepar();
        }


    }


    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (subListener != null) {
            subListener.setOnPlaying();
        }

    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if (subListener != null) {
            subListener.setOnPause();
        }

    }

    @Override
    public void onStateError() {
        super.onStateError();
        if (subListener != null) {
            subListener.setOnError();
        }

    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (subListener != null) {
            subListener.setOnStateAutoComplete();
        }
    }

    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();

    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
        replayTextView.setVisibility(GONE);
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
        if (subListener != null) {
            subListener.setOnProgress(progress, position, duration);
        }

    }

    public interface SubListener {

        void setOnPrepar();

        void setOnPreparing();

        void setOnPlaying();

        void setOnPause();

        void setOnError();

        void setOnStateAutoComplete();

        void setOnProgress(int progress, long position, long duration);
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, thumbImg, bottomPro, retryLayout);
        //屏蔽掉播放器原有的控件
        topContainer.setVisibility(GONE);
        bottomContainer.setVisibility(GONE);
        startButton.setVisibility(GONE);
        loadingProgressBar.setVisibility(GONE);
        thumbImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(GONE);
        mRetryLayout.setVisibility(GONE);
    }
}
