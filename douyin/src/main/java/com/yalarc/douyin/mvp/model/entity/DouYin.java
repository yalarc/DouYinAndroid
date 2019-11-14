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

import java.io.Serializable;

/**
 * ================================================
 * DouYin 实体类
 * <p>
 * Created by yalarc on 11/11/2019 20:46
 * <a href="yalarcs@gmail.com">Contact me</a>
 * <a href="https://github.com/yalarc">Follow me</a>
 * ================================================
 */
public class DouYin{
    private final int dy_id;
    private final String dy_video;

    public DouYin(int dy_id, String dy_video) {
        this.dy_id = dy_id;
        this.dy_video = dy_video;
    }

    public int getDy_id() {
        return dy_id;
    }

    public String getDy_video() {
        return dy_video;
    }

    @Override
    public String toString() {
        return "dy_id -> " + dy_id + " dy_video -> " + dy_video;
    }
}
