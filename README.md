




# AndroidAnimationExercise

这是一个关于Android中各种View的集合，里面包含自己日常开发和博客总结中的代码。
主要内容是**Android动画、Android自定义View 相关的知识**，**包含一些常见应用中动画效果的模仿实例**.
随着时间的推移，里面积累的很多代码，大致用5个Fragment 做了分类：



## ImitateFragment （模仿三方应用特效）

- 仿新浪微博雷达扫描效果及卡片动画效果（[Android 动画实战-仿微博雷达功能](https://juejin.im/post/586d0f9eda2f600055cf3021)）
- [仿懂球帝APP我是教练游戏效果](https://juejin.im/post/5974c0c3f265da6c4c50160f)
- 仿简书长按生成图片效果
- 沉寝式模式初探
- 仿简书头部SearchView
- 仿手Q侧滑菜单效果
- 仿QQ空间下拉顶部放大效果


### TraditionFragment (传统动画）

- 传统动画即帧动画和补间动画相关的代码
- Blur Android 实现毛玻璃效果的demo
- Activity 切换动画
- ViewPager 切换动画

### PropertyFragment  （属性动画）
- 属性动画特点及使用方式
- 仿支付宝支付动画（具体分析可查看日志[Android 动画实战](http://www.jianshu.com/p/d2e06a2e65ad)）
- 仿饿了吗商品加入购物车动画
- Viewgroup 动画（官方demo）
- Reveal Animation

>关于以上两种动画的区别分析可查看日志[Android 动画总结](http://www.jianshu.com/p/420629118c10)




### ViewsFragment （自定义View先关）
- PlayView[属性动画拓展（一）](http://www.jianshu.com/p/f34791f4d5ab)
- 自定义View基础
- Drawable 波浪动画
- 3D 省市联动效果滚轮，很像ios的效果

### OtherFragment （其他杂项）

- Android 截屏后保存图片至手机相册
- 拼图游戏（继承自RelativeLayout的动画效果，来自鸿洋大神）
- Android WebView 中 Java于JavaScript 互相调用
- Android 打开Camera或从相册选取照片，如何正确压缩图片，确保不发生OOM （[Android Bitmap 初探](https://juejin.im/post/58bc1f11ac502e006b0957b7)）
- Bottom Action Sheet 菜单
- Bitmap LRUCache，LruDiskCache 相关



以下是部分内容截屏动画

## [仿懂球帝APP我是教练游戏效果](https://juejin.im/post/5974c0c3f265da6c4c50160f)

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/football.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/football2.gif"/>


## 仿简书生成长图文章效果

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/jianshu.gif"/>


## 动图理解scrollTo &  translate 区别

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/move.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/-move.gif"/>

## 仿QQ侧滑菜单效果

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/qq.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/menu_3d.gif"/>




## 帧动画 & 补间动画 ##

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/frame.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/rotate1.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/translate.gif"/>



## 支付效果  & 贝塞尔曲线实现购物车添加动画 ##

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/alipay.gif"/>     <img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/shopcar.gif"/>


## 属性动画实例 ##


<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/anim1.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/anim2.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/click2.gif"/>




