package home.smart.fly.animations.customview.views;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by rookie on 2017/2/15.
 */

public class WindowHelper {
    private DisplayMetrics mDisplayMetrics;


    public WindowHelper(Context mContext) {
        Activity activity = (Activity) mContext;
        mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    public int getScreenW() {
        return mDisplayMetrics.widthPixels;
    }

    public int getScreenH() {
        return mDisplayMetrics.heightPixels;
    }
}
