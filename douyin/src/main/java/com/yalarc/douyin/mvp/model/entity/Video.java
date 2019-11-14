/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yalarc.douyin.mvp.model.entity;

import java.util.List;

/**
 * ================================================
 * DouYin 实体类
 * <p>
 * Created by yalarc on 11/11/2019 20:46
 * <a href="yalarcs@gmail.com">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * ================================================
 */
public class Video extends BaseResponse<Video> {

    private List<DouYin> videos;

    public List<DouYin> getVideos() {
        return videos;
    }

    public void setVideos(List<DouYin> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "videos length -> " + videos.size();
    }
}
