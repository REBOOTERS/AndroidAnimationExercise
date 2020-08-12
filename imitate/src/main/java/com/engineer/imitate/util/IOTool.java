package com.engineer.imitate.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 2020/8/12.
 *
 * @author rookie
 */
public class IOTool {
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
}
