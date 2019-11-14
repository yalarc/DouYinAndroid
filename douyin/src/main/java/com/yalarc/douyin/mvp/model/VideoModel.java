package com.yalarc.douyin.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.yalarc.douyin.mvp.contract.VideoContract;
import com.yalarc.douyin.mvp.model.api.cache.CommonCache;
import com.yalarc.douyin.mvp.model.api.service.VideoService;
import com.yalarc.douyin.mvp.model.entity.BaseResponse;
import com.yalarc.douyin.mvp.model.entity.DouYin;
import com.yalarc.douyin.mvp.model.entity.Video;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;


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
public class VideoModel extends BaseModel implements VideoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    public static final int PAGE_SIZE = 3;

    @Inject
    public VideoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Video> getVideos(int lastIdQueried, boolean update) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(VideoService.class)
                .getVideos(lastIdQueried, PAGE_SIZE))
                .flatMap(new Function<Observable<Video>, ObservableSource<Video>>() {
                    @Override
                    public ObservableSource<Video> apply(@NonNull Observable<Video> listObservable) throws Exception {
                        return mRepositoryManager.obtainCacheService(CommonCache.class)
                                .getVideos(listObservable
                                        , new DynamicKey(lastIdQueried)
                                        , new EvictDynamicKey(update))
                                .map(listReply -> listReply.getData());
                    }
                });
    }
}