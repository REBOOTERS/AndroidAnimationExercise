package com.engineer.imitate.ui.fragments.di;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

/**
 * 打开其他应用 Activity
 * <p>
 * https://www.cnblogs.com/loaderman/p/12156274.html
 */
public class TestHelper {
    private static final String TAG = "TestHelper";
    private static final String PACKAGE_NAME = "home.smart.fly.animations";

    public static boolean openActivity(Context context) {
        Log.d(TAG, "openActivity() called with: context = [" + context + "]");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        /**知道要跳转应用的包命与目标Activity*/
        ComponentName componentName =
                new ComponentName(PACKAGE_NAME,
                        "home.smart.fly.animations.ui.activity.BitmapMeshActivity");
        intent.setComponent(componentName);
        context.startActivity(intent);
        return true;
    }

    public static boolean openActivityHome(Context context) {
        Log.d(TAG, "openActivityHome() called with: context = [" + context + "]");
        PackageManager packageManager = context.getPackageManager();
        for (PackageInfo installedPackage : packageManager.getInstalledPackages(
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES)) {
            Log.i(TAG, "package " + installedPackage.packageName);
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        if (intent != null) {
            intent.putExtra("type", "110");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

    public static boolean openActivityByUrl(Context context) {
        Log.d(TAG, "openActivityByUrl() called with: context = [" + context + "]");
        Intent intent = new Intent();
        intent.setData(Uri.parse("custom_scheme://custom_host"));
        intent.putExtra("", "");//这里Intent当然也可传递参数,但是一般情况下都会放到上面的URL中进行传递
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }
}
