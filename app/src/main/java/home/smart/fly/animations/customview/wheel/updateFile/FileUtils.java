package home.smart.fly.animations.customview.wheel.updateFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    /**
     * 文件名
     */
    private final static String FILE_NAME = "city.json";
    /**
     * SharedPreferences 文件名
     */
    private final static String SP_FILE_NAME = "baobao";
    /**
     * json文件存储key
     */
    private final static String NAME_KEY = "jsonName";

    /**
     * 检测是否需要更新文件
     *
     * @param context
     * @return
     */
    public static boolean needUpdate(Context context, String NewJsonName) {
        boolean result = false;
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        String oldName = sp.getString(NAME_KEY, "");
        // 文件不存在或 文件名称不一致时均需要再次更新
        if (TextUtils.isEmpty(oldName) || !NewJsonName.equals(oldName)) {
            result = true;
            Editor editor = sp.edit();
            editor.putString(NAME_KEY, NewJsonName);
            editor.commit();
        }
        return result;
    }

    /**
     * 检测文件是否存在
     *
     * @param context
     * @return
     */
    public static boolean fileExist(Context context) {
        boolean result = false;
        String filePath = context.getFilesDir() + File.separator + FILE_NAME;
        File file = new File(filePath);
        if (file.exists()) {
            result = true;
        }
        return result;
    }

    /**
     * 写文件
     *
     * @param context
     * @param fileContent
     */
    public static void writeFile(Context context, String fileContent) {
        FileOutputStream os = null;
        try {
            os = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            os.write(fileContent.getBytes());
            os.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 读文件
     *
     * @param context
     * @return
     */
    public static String readFile(Context context) {
        String result = "";
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();//

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);//
            }

            byte[] content_byte = outStream.toByteArray();
            result = new String(content_byte);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
