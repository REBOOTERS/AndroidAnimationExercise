package home.smart.fly.animations.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.didichuxing.doraemonkit.kit.network.utils.StreamUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by rookie on 2017/2/10.
 */

public class Tools {
    private static final String TAG = "Tools";

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


    public static String getCurrentTime(long tempStap) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年", Locale.CHINA);
        Date curDate = new Date(tempStap);
        return simpleDateFormat.format(curDate);
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getAppVersion(Context mContext) {
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

    public static String readStrFromAssets(String filename, Context mContext) {
        String result = "";
        try {
            InputStream mInputStream = mContext.getAssets().open(filename);
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[mInputStream.available()];
            int len;
            while ((len = mInputStream.read(buffer)) != -1) {
                String str = new String(buffer, 0, len);
                sb.append(str);
            }
            result = sb.toString();
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static boolean copyFileFromAssetsToBox(String filename, Context mContext) {
        boolean result = false;
        String out = mContext.getFilesDir() + File.separator + filename;
        InputStream inputStream;
        OutputStream outputStream;
        try {
            inputStream = mContext.getAssets().open(filename);
            outputStream = new FileOutputStream(new File(out));
            StreamUtil.copy(inputStream, outputStream, new byte[1024]);
            inputStream.close();
            outputStream.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static Bitmap View2Bitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        canvas.save();
        return bitmap;
    }

    public static void getPhotoInfo(String url) {
        try {
            ExifInterface exifInterface = new ExifInterface(url);
            String FFNumber = exifInterface
                    .getAttribute(ExifInterface.TAG_F_NUMBER);
            String FDateTime = exifInterface
                    .getAttribute(ExifInterface.TAG_DATETIME);
            String FExposureTime = exifInterface
                    .getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            String FFlash = exifInterface
                    .getAttribute(ExifInterface.TAG_FLASH);
            String FFocalLength = exifInterface
                    .getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            String FImageLength = exifInterface
                    .getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String FImageWidth = exifInterface
                    .getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            String FISOSpeedRatings = exifInterface
                    .getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
            String FMake = exifInterface
                    .getAttribute(ExifInterface.TAG_MAKE);
            String FModel = exifInterface
                    .getAttribute(ExifInterface.TAG_MODEL);
            String FOrientation = exifInterface
                    .getAttribute(ExifInterface.TAG_ORIENTATION);
            String FWhiteBalance = exifInterface
                    .getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            String gps_altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String gps_longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String gps_distace = exifInterface.getAttribute(ExifInterface.TAG_GPS_DEST_DISTANCE);


            float[] latlng = new float[2];
            exifInterface.getLatLong(latlng);

            Log.e(TAG, "FFNumber:" + FFNumber);
            Log.e(TAG, "FDateTime:" + FDateTime);
            Log.e(TAG, "FExposureTime:" + FExposureTime);
            Log.e(TAG, "FFlash:" + FFlash);
            Log.e(TAG, "FFocalLength:" + FFocalLength);
            Log.e(TAG, "FImageLength:" + FImageLength);
            Log.e(TAG, "FImageWidth:" + FImageWidth);
            Log.e(TAG, "FISOSpeedRatings:" + FISOSpeedRatings);
            Log.e(TAG, "FMake:" + FMake);
            Log.e(TAG, "FModel:" + FModel);
            Log.e(TAG, "FOrientation:" + FOrientation);
            Log.e(TAG, "FWhiteBalance:" + FWhiteBalance);
            Log.e(TAG, "gps_altitude:" + gps_altitude);
            Log.e(TAG, "gps_longitude:" + gps_longitude);
            Log.e(TAG, "gps_latitude:" + latlng[0]);
            Log.e(TAG, "gps_longitude:" + latlng[1]);
            Log.e(TAG, "gps_distace:" + gps_distace);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }


}
