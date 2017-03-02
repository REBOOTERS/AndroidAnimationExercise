package home.smart.fly.animationdemo.customview.activitys;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rookie on 2017/2/28.
 */

public class FileHelper {
    static final int JPEG = 1;
    static final int PNG = 0;


    /**
     * 返回系统缓存路径
     *
     * @param mContext
     * @return
     */
    public static String getTempDirectoryPath(Context mContext) {
        File cachePath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cachePath = mContext.getExternalCacheDir();
        } else {
            cachePath = mContext.getCacheDir();
        }
        cachePath.mkdirs();
        return cachePath.getAbsolutePath();
    }

    /**
     * 在系统缓存中创建一个文件
     *
     * @param mContext
     * @param type
     * @param name
     * @return
     */
    public static File createFileByType(Context mContext, int type, String name) {
        if (TextUtils.isEmpty(name)) {
            name = ".pic";
        }

        switch (type) {
            case JPEG:
                name = name + ".jpg";
                break;
            case PNG:
                name = name + ".png";
                break;
            default:
                break;

        }
        return new File(getTempDirectoryPath(mContext), name);

    }

    /**
     * Removes the "file://" prefix from the given URI string, if applicable.
     * If the given URI string doesn't have a "file://" prefix, it is returned unchanged.
     *
     * @param uriString the URI string to operate on
     * @return a path without the "file://" prefix
     */
    public static String stripFileProtocol(String uriString) {
        if (uriString.startsWith("file://")) {
            uriString = uriString.substring(7);
        }
        return uriString;
    }

    public static String getPicutresPath(int encodingType) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + (encodingType == JPEG ? ".jpg" : ".png");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        String galleryPath = storageDir.getAbsolutePath() + File.separator + "Camera" + File.separator + imageFileName;
        return galleryPath;
    }

    public static boolean copyResultToGalley(Context context, Uri originUri, Uri galleryUri) {
        boolean result = false;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = context.getContentResolver().openInputStream(originUri);
            outputStream = context.getContentResolver().openOutputStream(galleryUri);

            byte[] buffer = new byte[2048];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


}
