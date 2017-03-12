




# AndroidAnimationExercise

这是一个关于Android中各种View的集合，里面包含自己日常开发和博客总结中的代码。
主要内容是**Android动画、Android自定义View 相关的知识**。
随着时间的推移，里面积累的很多代码，大致用4个Fragment 做了分类：

### TraditionFragment

- 传统动画即帧动画和补间动画相关的代码
- Blur Android 实现毛玻璃效果的demo
- Activity 切换动画
- ViewPager 切换动画

### PropertyFragment

- 属性动画特点及使用方式
- 仿支付宝支付动画（具体分析可查看日志[Android 动画实战](http://www.jianshu.com/p/d2e06a2e65ad)）
- 仿饿了吗商品加入购物车动画
- Viewgroup 动画（官方demo）
- Reveal Animation

>关于以上两种动画的区别分析可查看日志[Android 动画总结](http://www.jianshu.com/p/420629118c10)

### ViewsFragment

- 自定义View基础
- 仿新浪微博雷达扫描效果及卡片动画效果（[Android 动画实战-仿微博雷达功能](https://juejin.im/post/586d0f9eda2f600055cf3021)）
- 沉寝式模式初探
- Drawable 波浪动画

### OtherFragment

- Android 截屏后保存图片至手机相册
- 拼图游戏（继承自RelativeLayout的动画效果，来自鸿洋大神）
- Android WebView 中 Java于JavaScript 互相调用
- Android 打开Cameron或从相册选取照片，如何正确压缩图片，确保不发生OOM （[Android Bitmap 初探](https://juejin.im/post/58bc1f11ac502e006b0957b7)）
- Bottom Action Sheet 菜单
- Bitmap LRUCache，LruDiskCache 相关


以下是部分内容截屏动画



## Frame Animation ##

![frame](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/frame.gif)

## Tweened Animation ##

![roate1](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/rotate1.gif)

## Alipay PayAnimation ##

![alipay](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/alipay.gif)

## Add goods to ShoppingCar Animation ##

![shopcar](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/shopcar.gif)

## many traditional Animation ##

![translate](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/translate.gif)

## property Animation with Interpolator ##

![anim1](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/anim1.gif)

![anim2](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/anim2.gif)

## difference between tweened Animation and property Animation

![click1](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/click1.gif)

![click2](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/click2.gif)


# 注意、注意、注意 #


由于应用中使用到了百度地图SDK，需要根据运行机器生成特定的key,为了方便在不同的机器上使用，在app/build.gradle 中已配置了运行时特定的签名key.

```
signingConfigs {
        debug {
            keyAlias 'animation'
            keyPassword '123456'
            storeFile file('F:/2016_prj/AndroidAnimationExercise/animationkey')
            storePassword '123456'
        }
    }

```

如代码所示，这个key的配置路径是一个绝对路径，如果直接运行项目会报找不到key的错误（除非很巧，你的工程目录和我是一样的），因此；
- 如果想了解LBS相关内容，请按照自己机器的工程路径重新配置storeFile file()的内容，内容为这个key在你硬盘上的一个绝对路径，animationkey 已包含在项目根目录中；
- 如果对LBS 不感兴趣，可直接删除此配置信息；不影响其他功能。
- 仿新浪微博雷达扫描效果及卡片动画效果 需要保留此配置