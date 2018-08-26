package home.smart.fly.animations.fragments.base;

import android.app.Activity;

/**
 * @version V1.0
 * @author: Rookie
 * @date: 2018-08-20 10:59
 */
public class ItemInfo {
    public final int desc;
    public final Class<? extends Activity> activitys;

    public ItemInfo(int desc, Class<? extends Activity> demoClass) {
        this.desc = desc;
        this.activitys = demoClass;
    }
}
