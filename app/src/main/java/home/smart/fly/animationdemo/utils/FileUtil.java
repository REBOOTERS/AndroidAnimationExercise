package home.smart.fly.animationdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by rookie on 2016/11/10.
 * <p>
 * 文件读取工具类
 */


public class FileUtil {
    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param name
     * @return
     */
    public static boolean savaBitmap2SDcard(Context context, Bitmap bitmap, String name) {
        boolean result = false;
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String filename = name + ".jpg";
        File file = new File(fileDir, filename);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), filename, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileDir.toString())));
        return result;
    }

    /**
     * 获取bitmap
     *
     * @param filename
     * @return
     */
    public static Bitmap getBitmapFormSDcard(String filename) {
        Bitmap bitmap;
        String filepath = Environment.getExternalStorageDirectory() + File.separator + "Screenshots" + File.separator + filename;
        bitmap = BitmapFactory.decodeFile(filepath);
        return bitmap;
    }


    /**
     * 从本地assets  文件读取 json 文件，模拟网络请求结果
     *
     * @return
     */
    public static String getLocalResponse(Context mContext, String FileName) {

        String result = "";

        try {
            InputStream inputStream = mContext.getAssets().open(FileName);
            StringBuffer sb = new StringBuffer();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            inputStream.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 保存String 到手机存储
     *
     * @param str
     * @param fileName
     * @return
     */
    public static boolean saveStrToSDCard(String str, String fileName) {
        boolean success = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = Environment.getExternalStorageDirectory() + File.separator + fileName;

            File file = new File(path);
            try {
                OutputStream os = new FileOutputStream(file);
                byte[] b = str.getBytes();
                os.write(b);
                os.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return success;
    }

    /**
     * 汉字转拼音
     *
     * @param src
     * @return
     */
    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else
                    t4 += java.lang.Character.toString(t1[i]);
            }
            // System.out.println(t4);
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }
}
