package com.yalarc.douyin.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.yalarc.douyin.di.module.VideoModule;
import com.yalarc.douyin.mvp.contract.VideoContract;

import com.jess.arms.di.scope.ActivityScope;
import com.yalarc.douyin.mvp.ui.activity.VideoActivity;


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
@Component(modules = VideoModule.class, dependencies = AppComponent.class)
public interface VideoComponent {
    void inject(VideoActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        VideoComponent.Builder view(VideoContract.View view);

        VideoComponent.Builder appComponent(AppComponent appComponent);

        VideoComponent build();
    }
}