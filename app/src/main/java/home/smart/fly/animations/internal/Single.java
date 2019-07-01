package home.smart.fly.animations.internal;

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-01-2019
 */
public enum Single {
    INSTANCE;

    public int getValue() {
        return INSTANCE.hashCode();
    }
}
