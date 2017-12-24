

# AndroidAnimationExercise

这是一个关于Android中各种View的集合，里面包含自己日常开发和博客总结中的代码。
主要内容是**Android动画、Android自定义View 相关的知识**，**包含一些常见应用中动画效果的模仿实例**.
随着时间的推移，里面积累的很多代码，大致用5个Fragment 做了分类：


[Apk 下载体验](https://fir.im/tm15)


![扫一扫体验](data:image/png;base64,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)



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


## 仿懂球帝球员数据网状图

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/polyganoView.png"/>

## 仿简书生成长图文章效果

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/jianshu.gif"/>

## 贝塞尔曲线应用

<img src="https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/bessel.gif"/>


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




