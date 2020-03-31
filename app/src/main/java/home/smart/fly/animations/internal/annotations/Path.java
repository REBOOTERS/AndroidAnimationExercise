package home.smart.fly.animations.internal.annotations;

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 03-29-2020
 *
 * just for test ,no function
 */
public @interface Path {
    String value();
    int level();
    String[] params() default {};

}
