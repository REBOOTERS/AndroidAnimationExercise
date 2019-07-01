package home.smart.fly.animations.internal;

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-01-2019
 */
public enum NormalStatus {

    SUCCESS(1), FAIL(-1), LOADING(0);

    private int status;

    NormalStatus(int i) {
        status = i;
    }

    /**
     *
     * @param i
     * @return get Normal Status By int
     */
    public static NormalStatus getStats(int i) {
        for (NormalStatus value : NormalStatus.values()) {
            if (value.status == i) {
                return value;
            }
        }
        return null;
    }

    public static int getValue(NormalStatus v) {
        for (NormalStatus value : NormalStatus.values()) {
            if (value == v) {
                return value.status;
            }
        }
        return 0;
    }
}

