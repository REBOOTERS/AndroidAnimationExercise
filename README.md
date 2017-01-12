#注意、注意、注意#


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




# AndroidAnimationExercise
A Android Exercise include traditional and property Animations.

Also Some example like Alipay 

##Frame Animation##

![frame](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/frame.gif)

##Tweened Animation##

![roate1](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/rotate1.gif)

##Alipay PayAnimation##

![alipay](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/alipay.gif)

##Add goods to ShoppingCar Animation##

![shopcar](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/shopcar.gif)

##many traditional Animation ##

![translate](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/translate.gif)

##property Animation with Interpolator##

![anim1](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/anim1.gif)

![anim2](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/anim2.gif)

##difference between tweened Animation and property Animation

![click1](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/click1.gif)

![click2](https://raw.githubusercontent.com/REBOOTERS/AndroidAnimationExercise/master/screen/click2.gif)
