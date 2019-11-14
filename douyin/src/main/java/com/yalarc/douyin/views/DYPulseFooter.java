package com.yalarc.douyin.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.yalarc.douyin.R;

import me.jessyan.autosize.utils.ScreenUtils;

/**
 * ================================================
 * Description:
 * <p> 上拉加载更多动画，配置在smartFresh中使用
 * Created by yalarc on 11/12/2019 18:57
 * <a href="wx:qhb0123">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * <a href="https://github.com/yalarc/DouYinAndroid">Star me</a>
 * ================================================
 */
public class DYPulseFooter extends InternalAbstract implements RefreshFooter {


    private DYLoadingView mDYLoadingView;

    public DYPulseFooter(@NonNull Context context) {
        this(context, null);
    }

    public DYPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DYPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.dy_pulse_footer, null);
        addView(view);
        mDYLoadingView = view.findViewById(R.id.dy_load_view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDYLoadingView.getLayoutParams();
        params.width = ScreenUtils.getScreenSize(context)[0];
        mDYLoadingView.setLayoutParams(params);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        mDYLoadingView.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        mDYLoadingView.stop();
        return 0;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

}
