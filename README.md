

# AndroidAnimationExercise

[![Build Status](https://travis-ci.org/REBOOTERS/AndroidAnimationExercise.svg?branch=master)](https://travis-ci.org/REBOOTERS/AndroidAnimationExercise)


这是一个关于Android中各种View的集合，里面包含自己日常开发和博客总结中的代码。
主要内容是**Android动画、Android自定义View 相关的知识**，**包含一些常见应用中动画效果的模仿实例**.
随着时间的推移，里面积累的很多代码，大致用5个Fragment 做了分类：


[Apk 下载体验](https://fir.im/tm15)


扫一扫体验

<details>
<summary>打开</summary>

![扫一扫体验](https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/download.png)

</details>





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

### What's New

随着累计的动画效果越来越多，导致工程内代码越来越多，app 运行和编译时间变长。因此采用了**组件化**的方式，单独抽取了 imitate moulde . 

imitate 内的内容全部由 kotlin 语言实现，后续所有内容都会往这个里写，app这个module只作为以往的积累，尽量不再更改。 **本着组件化的思想，imitate 可以作为 app  的依赖组件，也可以单独运行，修改 gradle.properties 中的配置信息即可。**

在组件化的过程中，使用 [ARouter](https://github.com/alibaba/ARouter) 非常方便。完全解决了页面跳转的问题。

### Android 构建流程 Gradle 的学习和 Gradle 插件自定义

在 buildSrc 内包含一些关于 gradle 构建流程的自定义内容，包括

- 实现生成 apk 根据 flavor 改名。
- 对构建流程中执行的 task 按执行时长打印，发现编译耗时的 task
- 在构建流程中对特定注解的方法或类进行耗时检测。
- 对代码中点击事件的插桩埋点
- 通过配置对三方库中的代码进行特定的插桩，实现特定的功能。


以下是部分内容截屏动画

## [仿懂球帝APP我是教练游戏效果](https://juejin.im/post/5974c0c3f265da6c4c50160f)

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/football.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/football2.gif"/>

## galaxy 效果 && gif 反转

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/galaxy.gif"/><img width=287 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/revert_gif.gif"/>



## skeleton  && bitmap mesh 效果

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/skeleton.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/mesh.gif"/>

## parallax 效果 && 2048 游戏(pure web)

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/parallax.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/2048_game.gif"/>


## 仿懂球帝球员数据网状图 && 仿简书生成长图文章效果

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/polyganoView.png"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/jianshu.gif"/>



## 仿知乎广告效果动画 && pure 3D animation view

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/ad.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/ad_fullscreen.gif"/>

## AD Animation

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/3d_shape.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/loading-image.gif"/>


## 动图理解scrollTo &  translate 区别

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/move.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/-move.gif"/>

<details>
<summary>更多动画，点击打开</summary>


## 仿QQ侧滑菜单效果

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/qq.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/menu_3d.gif"/>

## 波浪动画
 
<img src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/wave_animation.gif"/>

## 简易时钟效果

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/ACTION_MOVE_TO_CHANGE.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/CLOCK_VIEW.gif"/>


## 物理动画 & Lottie Animation 

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/LottieAnimation.gif"/><img width=300 height=390 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/physical_animation.gif"/>





## 帧动画 & 补间动画 ##

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/frame.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/rotate1.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/translate.gif"/>


## 仿探探效果 & 约束布局（ConstraintLayout AnimationSet） 动画

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/slide.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/constraint_set_anim.gif"/>

## 支付效果  & loading

<img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/loading.gif"/><img width=300 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/alipay.gif"/>

## 属性动画实例 ##


<img src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/anim1.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/anim2.gif"/><img src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/click2.gif"/>

## 一些系统信息

<img width=400 src="https://raw.githubusercontent.com/REBOOTERS/Images/master/AndroidAnimationExercise/screen/sys_info.jpg"/>



</details>