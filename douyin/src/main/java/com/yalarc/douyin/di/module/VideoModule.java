package com.yalarc.douyin.di.module;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalarc.douyin.mvp.contract.VideoContract;
import com.yalarc.douyin.mvp.model.VideoModel;
import com.yalarc.douyin.mvp.model.entity.DouYin;
import com.yalarc.douyin.mvp.ui.adapter.VideoAdapter;
import com.yalarc.douyin.views.layoutmanager.ViewPagerLayoutManager;

import java.util.ArrayList;
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
@Module
public abstract class VideoModule {

    @Binds
    abstract VideoContract.Model bindVideoModel(VideoModel model);

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(VideoContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @ActivityScope
    @Provides
    static ViewPagerLayoutManager provideLayoutManager(VideoContract.View view) {
        return new ViewPagerLayoutManager(view.getActivity(), OrientationHelper.VERTICAL);
    }

    @ActivityScope
    @Provides
    static List<DouYin> provideVideoList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static BaseQuickAdapter<DouYin, BaseViewHolder> provideVideoAdapter(List<DouYin> list) {
        return new VideoAdapter(list);
    }

}