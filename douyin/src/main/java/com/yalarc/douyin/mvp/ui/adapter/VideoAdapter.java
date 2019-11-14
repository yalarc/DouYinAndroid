package com.yalarc.douyin.mvp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.yalarc.douyin.R;
import com.yalarc.douyin.mvp.model.api.Api;
import com.yalarc.douyin.mvp.model.entity.DouYin;
import com.yalarc.douyin.views.DYLikeLayout;
import com.yalarc.douyin.views.likebutton.DYLikeView;
import com.yalarc.douyin.views.player.DYJzvdStd;
import com.yalarc.douyin.views.player.JZMediaIjk;
import com.yalarc.douyin.views.player.JZViewOutlineProvider;

import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

/**
 * ================================================
 * 展示 {@link BaseQuickAdapter} 的用法
 * <p>
 * Created by yalarc on 11/12/2019 12:33
 * <a https://github.com/CymChad/BaseRecyclerViewAdapterHelper">learn more</a>
 * <a href="wx:qhb0123">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * <a href="https://github.com/yalarc/DouYinAndroid">Star me</a>
 * ================================================
 */
public class VideoAdapter extends BaseQuickAdapter<DouYin, BaseViewHolder> {

    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;


    public VideoAdapter(@Nullable List<DouYin> data) {
        super(R.layout.item_video, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DouYin item) {
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        mImageLoader = mAppComponent.imageLoader();
        //拼接视频url
        String url = Api.APP_VIDEO + item.getDy_video() + ".mp4";
        //视频播放器
        DYJzvdStd dyVideoPlayer = helper.getView(R.id.dy_video_player);
        JZDataSource jzDataSource = new JZDataSource(url);

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(mContext,
                ImageConfigImpl
                        .builder()
                        .url(url)
                        .imageView(dyVideoPlayer.thumbImageView)
                        .build());
        //设置视频圆角
        dyVideoPlayer.setOutlineProvider(new JZViewOutlineProvider(20));
        dyVideoPlayer.setClipToOutline(true);
        //配置视频框架 采用ijk内核
        dyVideoPlayer.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);

        //点赞按钮
        DYLikeView dyLikeButton = helper.getView(R.id.dy_like_button);
        //屏幕点赞 效果
        DYLikeLayout dyLikeLayout = helper.getView(R.id.dy_like_layout);
        dyLikeLayout.setLikeClickCallBack(new DYLikeLayout.LikeClickCallBack() {
            @Override
            public void onLikeListener() {
                //多击监听
                if (!dyLikeButton.isLiked()) {
                    dyLikeButton.performClick();
                }
            }

            @Override
            public void onSingleListener() {
                //单击监听

            }
        });

    }


}
