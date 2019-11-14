package com.yalarc.douyin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


/**
 * ================================================
 * Description:
 * <p> 阻止向下传递事件
 * Created by yalarc on 11/12/2019 18:57
 * <a href="wx:qhb0123">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * <a href="https://github.com/yalarc/DouYinAndroid">Star me</a>
 * ================================================
 */
public class ARelativeLayout extends RelativeLayout {

    private boolean isIntercept = true;


    public ARelativeLayout(Context context) {
        super(context);
    }

    public ARelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ARelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isIntercept;
    }
}