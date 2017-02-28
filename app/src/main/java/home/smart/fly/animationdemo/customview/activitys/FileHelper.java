package home.smart.fly.animationdemo.customview.activitys;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rookie on 2017/2/28.
 */

public class FileHelper {
    public static final int JPEG = 1;
    public static final int PNG = 0;


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

    /**
     * Returns an input stream based on given URI string.
     *
     * @param uriString the URI string from which to obtain the input stream
     * @param mContext  the current application context
     * @return an input stream into the data at the given URI or null if given an invalid URI string
     * @throws IOException
     */
    public static InputStream getInputStreamFromUriString(String uriString, Context mContext)
            throws IOException {
        InputStream returnValue = null;
        if (uriString.startsWith("content")) {
            Uri uri = Uri.parse(uriString);
            returnValue = mContext.getContentResolver().openInputStream(uri);
        } else if (uriString.startsWith("file://")) {
            int question = uriString.indexOf("?");
            if (question > -1) {
                uriString = uriString.substring(0, question);
            }
            if (uriString.startsWith("file:///android_asset/")) {
                Uri uri = Uri.parse(uriString);
                String relativePath = uri.getPath().substring(15);
                returnValue = mContext.getAssets().open(relativePath);
            } else {
                // might still be content so try that first
                try {
                    returnValue = mContext.getContentResolver().openInputStream(Uri.parse(uriString));
                } catch (Exception e) {
                    returnValue = null;
                }

            }
        } else {
            returnValue = new FileInputStream(uriString);
        }
        return returnValue;
    }
}
