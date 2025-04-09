```java
package com.example.aa175.fasttrans;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // 创建pytorch模型
    private Module fastTransNet01;
    private Module fastTransNet02;
    private Module fastTransNet03;

    // 定义ImageView对象show_image展示的图片的路径
    private String showImagePath;
    // 定义相机拍摄图像的路径
    private String cameraImagePath;
    // 定义从相册选择图像的路径
    private String albumImagePath;

    // 开启相机和调用相册的请求码
    private static final int START_CAMERA_CODE = 1111;
    private static final int START_ALBUM_CODE = 1112;
    // 请求权限的请求码
    private static final int REQUIRE_PERMISSION_CODE = 111;

    // 创建用于开启相机的按钮
    private Button btnStartCamera;
    // 创建用于调用系统相册的按钮
    private Button btnStartAlbum;
    // 创建用于保存ImageView内容的按钮
    private Button btnSaveImage;
    // 创建用于启动pytorch前向推理的按钮
    private Button btnStartPredict;

    // 创建ImageView对象
    private ImageView originImageView;
    private ImageView transStyleImageView01;
    private ImageView transStyleImageView02;
    private ImageView transStyleImageView03;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_main);

        // 绑定开启相机的按钮
        btnStartCamera = (Button) findViewById(R.id.button_start_camera);
        btnStartCamera.setOnClickListener(new StartCameraOnClickListener());
        // 绑定调用相册的按钮
        btnStartAlbum = (Button) findViewById(R.id.button_start_album);
        btnStartAlbum.setOnClickListener(new StartAlbumOnClickListener());
        // 绑定保存图片的按钮
        btnSaveImage = (Button) findViewById(R.id.button_save_image);
        btnSaveImage.setOnClickListener(new SaveImageOnClickListener());

        btnStartPredict = (Button) findViewById(R.id.button_start_predict);
        btnStartPredict.setOnClickListener(new StartPredictOnClickListener());

        //  绑定我们在activity_main.xml中定义的Image_View
        originImageView = (ImageView) findViewById(R.id.image_view_show_low_light);
        transStyleImageView01 = (ImageView)findViewById(R.id.image_view_show_trans_01);
        transStyleImageView02 = (ImageView)findViewById(R.id.image_view_show_trans_02);
        transStyleImageView03 = (ImageView)findViewById(R.id.image_view_show_trans_03);
    }

    class StartCameraOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 开启相机，返回相机拍摄图像的路径，传入的请求码是START_CAMERA_CODE
            cameraImagePath = Utils.startCamera(MainActivity.this, START_CAMERA_CODE);
        }
    }

    class StartAlbumOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 调用相册，传入的请求码是START_ALBUM_CODE
            Utils.startAlbum(MainActivity.this, START_ALBUM_CODE);
        }
    }

    class SaveImageOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 获取show_image展示的图片
            Bitmap bitmap = ((BitmapDrawable) transStyleImageView01.getDrawable()).getBitmap();
            // 通过saveImage函数生成图像的保存路径
            String imageSavePath = saveImage(bitmap, 100);
            // 提示用户图片已经被保存
            if (imageSavePath != null) {
                Toast.makeText(MainActivity.this, "save to: " + imageSavePath, Toast.LENGTH_LONG).show();
            }
        }
    }

    class StartPredictOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            fastTrans(showImagePath);
        }
    }

    private static String saveImage(Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }

        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (appDir == null) {
            return null;
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            return file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    // 请求本案例需要的三种权限
    private void requestPermissions() {
        // 定义容器，存储我们需要申请的权限
        List<String> permissionList = new ArrayList<>();
        // 检测应用是否具有CAMERA的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        // 检测应用是否具有READ_EXTERNAL_STORAGE权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // 检测应用是否具有WRITE_EXTERNAL_STORAGE权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 如果permissionList不为空，则说明前面检测的三种权限中至少有一个是应用不具备的
        // 则需要向用户申请使用permissionList中的权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), REQUIRE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 判断请求码
        switch (requestCode) {
            // 如果请求码是我们设定的权限请求代码值，则执行下面代码
            case REQUIRE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        // 如果请求被拒绝，则弹出下面的Toast
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(this, permissions[i] + " was denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case START_ALBUM_CODE:
                    if (data == null) {
                        Log.w("LOG", "user photo data is null");
                        return;
                    }
                    Uri albumImageUri = data.getData();
                    albumImagePath = Utils.getPathFromUri(MainActivity.this, albumImageUri);
                    showImagePath = albumImagePath;
                    Bitmap bitmapAlbum = Utils.getScaleBitmapByPath(albumImagePath);
                    originImageView.setImageBitmap(Utils.getScaleBitmapByBitmap(bitmapAlbum, 448, 448));
                    Toast.makeText(MainActivity.this, "album start", Toast.LENGTH_LONG).show();
                    break;
                case START_CAMERA_CODE:
                    showImagePath = cameraImagePath;
                    Bitmap bitmapCamera = Utils.getScaleBitmapByPath(cameraImagePath);
                    Toast.makeText(MainActivity.this, "camera start", Toast.LENGTH_LONG).show();
                    originImageView.setImageBitmap(Utils.getScaleBitmapByBitmap(bitmapCamera, 448, 448));
                    break;
            }
        }
    }

    private float clip(float x){
        return Math.max(Math.min(x, 255.0f), 0.0f);
    }

    private void fastTrans(String imagePath) {
        // FastTrans的模型文件名称，对应三种风格的模型
        String ptPath01 = "trans_01.pt";
        String ptPath02 = "trans_02.pt";
        String ptPath03 = "trans_03.pt";

        // FastTrans网络的输入和输出图像尺寸
        int inDims[] = {224, 224, 3};
        int outDims[] = {224, 224, 3};
        Bitmap bmp = null;
        Bitmap scaledBmp = null;
        String filePath = "";
        // 加载FastTrans网络
        try {
            filePath = assetFilePath(this, ptPath01);
            fastTransNet01 = Module.load(filePath);
            filePath = assetFilePath(this, ptPath02);
            fastTransNet02 = Module.load(filePath);
            filePath = assetFilePath(this, ptPath03);
            fastTransNet03 = Module.load(filePath);
            Log.d("LOG", "success load pt " + filePath);
        } catch (Exception e) {
            Log.d("LOG", "can not load pt " + filePath);
        }
        // 读取输入图像的Bitmap，并进行缩放
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(imagePath));
            bmp = BitmapFactory.decodeStream(bis);
            scaledBmp = Bitmap.createScaledBitmap(bmp, inDims[0], inDims[1], true);
            bis.close();
        } catch (Exception e) {
            Log.d("LOG", "can not read low light bitmap");
        }

        float[] meanRGB = {0.485f, 0.456f, 0.406f};
        float[] stdRGB = {0.229f, 0.224f, 0.225f};
        // 构建原始图像的Tensor输入
        Tensor oriT = TensorImageUtils.bitmapToFloat32Tensor(scaledBmp, meanRGB, stdRGB);

        try {
            // 完成风格转换网络的前向推理，并获得三种风格的输出
            Tensor transT01 = fastTransNet01.forward(IValue.from(oriT)).toTensor();
            Tensor transT02 = fastTransNet02.forward(IValue.from(oriT)).toTensor();
            Tensor transT03 = fastTransNet03.forward(IValue.from(oriT)).toTensor();
            float[] Arr01 = transT01.getDataAsFloatArray();
            float[] Arr02 = transT02.getDataAsFloatArray();
            float[] Arr03 = transT03.getDataAsFloatArray();

            // 存储风格转换后的三张图像
            float[][][] Img01 = new float[outDims[0]][outDims[1]][outDims[2]];
            float[][][] Img02 = new float[outDims[0]][outDims[1]][outDims[2]];
            float[][][] Img03 = new float[outDims[0]][outDims[1]][outDims[2]];

            int index = 0;
            // g代表第二个通道的索引相对于第一个通道的偏移
            int g = outDims[0] * outDims[1];
            // b代表第三个通道的索引相对于第二个通道的偏移
            int b = 2 * g;
            for (int k = 0; k < outDims[0]; k++) {
                for (int m = 0; m < outDims[1]; m++) {
                    // 进行第一个通道的数值变换与数值截断
                    Img01[k][m][0] = clip((Arr01[index]*0.229f + 0.485f) * 255.0f);
                    Img02[k][m][0] = clip((Arr02[index]*0.229f + 0.485f) * 255.0f);
                    Img03[k][m][0] = clip((Arr03[index]*0.229f + 0.485f) * 255.0f);

                    // 进行第二个通道的数值变换与数值截断
                    Img01[k][m][1] = clip((Arr01[index+g]*0.224f + 0.456f) * 255.0f);
                    Img02[k][m][1] = clip((Arr02[index+g]*0.224f + 0.456f) * 255.0f);
                    Img03[k][m][1] = clip((Arr03[index+g]*0.224f + 0.456f) * 255.0f);

                    // 进行第三个通道的数值变换与数值截断
                    Img01[k][m][2] = clip((Arr01[index+b]*0.225f + 0.406f) * 255.0f);
                    Img02[k][m][2] = clip((Arr02[index+b]*0.225f + 0.406f) * 255.0f);
                    Img03[k][m][2] = clip((Arr03[index+b]*0.225f + 0.406f) * 255.0f);

                    index+=1;
                }
            }

            // 将三种风格的图像显示到手机界面
            Bitmap transBitmap01 = Utils.getBitmap(Img01, outDims);
            Bitmap transBitmap02 = Utils.getBitmap(Img02, outDims);
            Bitmap transBitmap03 = Utils.getBitmap(Img03, outDims);
            transStyleImageView01.setImageBitmap(transBitmap01);
            transStyleImageView02.setImageBitmap(transBitmap02);
            transStyleImageView03.setImageBitmap(transBitmap03);
        } catch (Exception e) {
            Log.e("LOG", "fail to predict");
            e.printStackTrace();
        }
    }


    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
}

```

```
package com.example.aa175.fasttrans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Utils {

    /**
     * @param activity 活动
     * @param requestCode 请求码
     * @return 拍摄图像的路径
     */
    public static String startCamera(Activity activity, int requestCode) {
        Uri imageUri;
        // 将拍摄图像保存到指定路径,图片的名字由系统使用时间生成,这样就不会重名
        File outputImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/XiaoMei/", System.currentTimeMillis() + ".jpg");
        Log.d("outputImage", outputImage.getAbsolutePath());
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            File outPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/XiaoMei/");
            if (!outPath.exists()) {
                outPath.mkdirs();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            // 兼容性提升
            imageUri = FileProvider.getUriForFile(activity,
                    "com.example.aa175.fasttrans", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        // 开启相机的Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 指定图片的保存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 设置图像的质量
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        activity.startActivityForResult(intent, requestCode);
        // 返回图像的保存路径
        return outputImage.getAbsolutePath();
    }

    /**
     * @param activity 活动
     * @param requestCode 请求码
     */
    public static void startAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param context 上下文
     * @param uri 资源标识
     * @return 路径值
     */
    public static String getPathFromUri(Context context, Uri uri) {

        String result;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * @param image_array 输入的四维数组
     * @param dim_info 未读信息
     * @return
     */
    public static Bitmap getBitmap(float[][][] image_array, int[] dim_info) {
        int count = 0;
        int[] color_info = new int[dim_info[0] * dim_info[1]];
        // 遍历图像,获取颜色信息
        for (int i = 0; i < dim_info[0]; i++) {
            for (int j = 0; j < dim_info[1]; j++) {
                float[] arr = image_array[i][j];
                int alpha = 255;
                int red = (int) arr[0];
                int green = (int) arr[1];
                int blue = (int) arr[2];
                int tempARGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                color_info[count++] = tempARGB;
            }
        }
        // 创建bitmap对象
        return Bitmap.createBitmap(color_info, dim_info[0], dim_info[1], Bitmap.Config.ARGB_8888);
    }

    /**
     * @param filePath 文件路径
     * @return Bitmap对象
     */

    public static Bitmap getScaleBitmapByPath(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int width = options.outWidth;
        int height = options.outHeight;

        int maxSize = 500;
        options.inSampleSize = 1;
        while (true) {
            if (width / options.inSampleSize < maxSize || height / options.inSampleSize < maxSize) {
                break;
            }
            options.inSampleSize *= 2;
        }
        options.inJustDecodeBounds = false;

        // 返回解码后的图片
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * @param origin 原始Bitmap
     * @param newWidth 缩放后的宽度
     * @param newHeight 缩放后的高度
     * @return 缩放后的Bitmap
     */
    public static Bitmap getScaleBitmapByBitmap(Bitmap origin, int newWidth, int newHeight) {
        // 如果输入的Bitmap为空，则直接返回
        if (origin == null) {
            return null;
        }
        // 原始Bitmap的长宽
        int height = origin.getHeight();
        int width = origin.getWidth();

        // 计算缩放后的图像比例
        float scaleWidthRatio= ((float) newWidth) / width;
        float scaleHeightRatio = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthRatio, scaleHeightRatio);

        // 创建新的Bitmap
        Bitmap scaledBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return scaledBitmap;
    }
}
```