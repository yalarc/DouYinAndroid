# DouYinAndroid

仿抖音视频列表、屏幕点赞效果、点赞按钮效果、上拉下拉动画，加载动画效果...

下载体验（密码 123456）

![抖快音手](https://github.com/yalarc/DouYinAndroid/blob/master/douyin/down-qr.png)


# 介绍：

1. 采用[MVPArms框架](https://github.com/JessYanCoding/MVPArms)进行项目搭建（star 9K+）

2. 上啦/下拉加载操作 采用[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)(star 19.2K+)+自定义底部动画实现抖音下拉/上拉效果

3. 视频播放 采用[JiaoZiVideoPlayer播放器](https://github.com/lipangit/JiaoZiVideoPlayer)(star 9.9+)+B站的IJK内核[ijkplayer](https://github.com/bilibili/ijkplayer)

4. 抖音播放列表滑动 采用recyclerview + 自定义ViewPagerSnapHelper 实现列表滑动

5. 抖音点赞动效 自定义DYLikeLayout控件 实现连续点击屏幕，红星扩散效果

6. 抖音点赞按钮效果 自定义DYLikeView控件，实现点击动画

7. 抖音正在加载效果 自定义DYLoadingView控件 ，实现双球滚动效果

# 计划

1. 完善视频缓存策略：初步采用先缓存（下载到本地）后加载播放 ，在当前播放往后缓存3个，遇到视频下载失败等网络问题重试几次后，
不成功直接删除，跳过。（缓存策略大家有好的想法可以与我交流 后续会开源出来）

# 初衷

1. 学习别人优秀的架构能力，设计模式
2. 交朋友
3. 开源分享，贡献一点力量

# 接口

[自己写的php接口](https://github.com/yalarc/PhpApplication)：前端开发最要命的就是接口，所以干脆自己搭了一个服务器，练习服务器部署，
方便自己用，也就开源出来了，需要简单接口的，可以给我格式，帮你们搞

# 数据来源
[自己写的抖音爬虫](https://github.com/yalarc/GetDouYinApplication): 这个也是自己第一份爬虫代码，已经有15个star了，如果大家喜欢的话，
给个star支持一下

# 最后

如果 帮到您 或者 想与我交流沟通的可以加我 * wx qhb0123备注 github *  ,顺手star一下，你最美！
