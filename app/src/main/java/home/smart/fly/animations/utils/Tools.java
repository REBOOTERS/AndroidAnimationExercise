package home.smart.fly.animations.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rookie on 2017/2/10.
 */

public class Tools {
    /**
     * 获取系统时间 从月份开始到秒结束
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(curDate);
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context mContext) {
        int version = -1;
        String versionStr = "";
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            version = info.versionCode;
            versionStr = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionStr;
    }

    /**
     * 获取应用名称
     *
     * @param mContext
     * @return
     */
    public static String getName(Context mContext) {
        String nameStr = "";
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            nameStr = info.applicationInfo.loadLabel(manager).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nameStr;
    }

    /**
     * JSON保存到SD卡(临时测试)
     *
     * @param filename
     * @param filecontent
     * @throws Exception
     */
    public static void saveToSDCard(String filename, String filecontent) {
        File file = new File(Environment.getExternalStorageDirectory(),
                filename);
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            outStream.write(filecontent.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }

}
