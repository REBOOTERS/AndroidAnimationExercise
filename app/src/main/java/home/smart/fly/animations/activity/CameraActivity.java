package home.smart.fly.animations.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.T;
import home.smart.fly.animations.utils.V;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CameraActivity";
    private Context mContext;
    private static final int REQUEST_CODE_TAKE_PIC_CAMERA = 100;
    private static final int REQUEST_CODE_CROP_PIC = 101;
    private static final int REQUEST_CODE_PICK_FROM_GALLEY = 102;
    private static final float M_RATE = 1024 * 1024;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_WRITE_STORAGE = 2;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //
    private ImageView mImageView;
    private AppCompatCheckBox edit, compress, sample, insert;
    //压缩
    private AppCompatSeekBar compressSeekBar;
    private LinearLayout rateShell;
    private TextView rate;

    //裁剪
    private LinearLayout cropWidthShell;
    private AppCompatSeekBar cropWidthValue;
    private TextView cropWidth;
    private LinearLayout cropHeightShell;
    private AppCompatSeekBar cropHeightValue;
    private TextView cropHeight;


    private TextView info;

    //
    private boolean needEdit;
    private boolean needCompress;
    private boolean needSample;
    private boolean insertToGallery;
    private int compressRate;
    private int cropTargetHeight;
    private int cropTargetWidth;
    private int destType = FileHelper.JPEG;
    private Uri imageUrl;
    private Uri copyUrl;

    private int screenWidth;
    private int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_camera);
        InitView();
    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //创建一个临时文件夹存储拍摄的照片
        File file = FileHelper.createFileByType(mContext, destType, "test");
        imageUrl = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            file = new File(mContext.getCacheDir(), "test.jpg");
            imageUrl = Uri.fromFile(file);

            Toast.makeText(mContext, "TODO", Toast.LENGTH_SHORT).show();
            // 将文件转换成content://Uri的形式
            Uri contentUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", file);
            // 申请临时访问权限
            takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        } else {

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
        }


        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PIC_CAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //Camera
            case REQUEST_CODE_TAKE_PIC_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (needEdit) {
                        CropTheImage(imageUrl);
                    } else {
                        ProcessResult(imageUrl);
                    }
                }
                break;
            //Crop
            case REQUEST_CODE_CROP_PIC:
                if (resultCode == Activity.RESULT_OK) {
                    ProcessResult(copyUrl);
                }
                break;
            //gallery
            case REQUEST_CODE_PICK_FROM_GALLEY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        if (needEdit) {
                            CropTheImage(uri);
                        } else {
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);
                            if (c != null) {
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                String imagePath = c.getString(columnIndex);
                                Uri imageUri = Uri.parse(imagePath);
                                ProcessResult(imageUri);
                            }

                        }
                    }
                }
            default:
                break;
        }
    }

    /**
     * 打开手机相册
     */
    private void selectFromGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, REQUEST_CODE_PICK_FROM_GALLEY);
    }

    /**
     * 裁剪照片
     *
     * @param imageUrl
     */
    private void CropTheImage(Uri imageUrl) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(imageUrl, "image/*");
        cropIntent.putExtra("cropWidth", "true");
        cropIntent.putExtra("outputX", cropTargetWidth);
        cropIntent.putExtra("outputY", cropTargetHeight);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = new File(mContext.getCacheDir(), "test1.jpg");
            copyUrl = Uri.fromFile(file);
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            cropIntent.putExtra("output", contentUri);
        } else {
            File copyFile = FileHelper.createFileByType(mContext, destType, String.valueOf(System.currentTimeMillis()));
            copyUrl = Uri.fromFile(copyFile);
            cropIntent.putExtra("output", copyUrl);
        }
        startActivityForResult(cropIntent, REQUEST_CODE_CROP_PIC);
    }

    /**
     * 根据图片Uri 获取Bitmap
     *
     * @param destUrl
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void ProcessResult(Uri destUrl) {
        String pathName = FileHelper.stripFileProtocol(destUrl.toString());
        showBitmapInfos(pathName);
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        if (bitmap != null) {

            //添加图片到相册
            if (insertToGallery) {
                insertToGallery(destUrl);
            }


            if (needCompress) {
                bitmap = getCompressedBitmap(bitmap);

            }

            if (needSample) {
                bitmap = getRealCompressedBitmap(pathName, screenWidth, screenHeight);
            }

            mImageView.setImageBitmap(bitmap);
            float count = bitmap.getByteCount() / M_RATE;
            float all = bitmap.getAllocationByteCount() / M_RATE;
            String result = "这张图片占用内存大小:\n" +
                    "bitmap.getByteCount()== " + count + "M\n" +
                    "bitmap.getAllocationByteCount()= " + all + "M";
            info.setText(result);
            Log.e(TAG, result);
        } else {
            T.showLToast(mContext, "fail");
        }
    }

    private void insertToGallery(Uri imageUrl) {
        Uri galleryUri = Uri.fromFile(new File(FileHelper.getPicutresPath(destType)));
        boolean result = FileHelper.copyResultToGalley(mContext, imageUrl, galleryUri);
        if (result) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(galleryUri);
            sendBroadcast(mediaScanIntent);
        }
    }

    private Bitmap getRealCompressedBitmap(String pathName, int reqWidth, int reqHeight) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        int width = options.outWidth / 2;
        int height = options.outHeight / 2;
        int inSampleSize = 1;

        while (width / inSampleSize >= reqWidth && height / inSampleSize >= reqHeight) {
            inSampleSize = inSampleSize * 2;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(pathName, options);
        showBitmapInfos(pathName);
        return bitmap;
    }

    /**
     * 获取Bitmap的信息
     *
     * @param pathName
     */
    private void showBitmapInfos(String pathName) {
        try {
            ExifInterface mExifInterface = new ExifInterface(pathName);
            String FFNumber = mExifInterface
                    .getAttribute(ExifInterface.TAG_APERTURE);
            String FDateTime = mExifInterface
                    .getAttribute(ExifInterface.TAG_DATETIME);
            String FExposureTime = mExifInterface
                    .getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            String FFlash = mExifInterface
                    .getAttribute(ExifInterface.TAG_FLASH);
            String FFocalLength = mExifInterface
                    .getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            String FImageLength = mExifInterface
                    .getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String FImageWidth = mExifInterface
                    .getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            String FISOSpeedRatings = mExifInterface
                    .getAttribute(ExifInterface.TAG_ISO);
            String FMake = mExifInterface
                    .getAttribute(ExifInterface.TAG_MAKE);
            String FModel = mExifInterface
                    .getAttribute(ExifInterface.TAG_MODEL);
            String FOrientation = mExifInterface
                    .getAttribute(ExifInterface.TAG_ORIENTATION);
            String FWhiteBalance = mExifInterface
                    .getAttribute(ExifInterface.TAG_WHITE_BALANCE);

            Log.i(TAG, "FFNumber:" + FFNumber);
            Log.i(TAG, "FDateTime:" + FDateTime);
            Log.i(TAG, "FExposureTime:" + FExposureTime);
            Log.i(TAG, "FFlash:" + FFlash);
            Log.i(TAG, "FFocalLength:" + FFocalLength);
            Log.i(TAG, "FImageLength:" + FImageLength);
            Log.i(TAG, "FImageWidth:" + FImageWidth);
            Log.i(TAG, "FISOSpeedRatings:" + FISOSpeedRatings);
            Log.i(TAG, "FMake:" + FMake);
            Log.i(TAG, "FModel:" + FModel);
            Log.i(TAG, "FOrientation:" + FOrientation);
            Log.i(TAG, "FWhiteBalance:" + FWhiteBalance);
        } catch (IOException e) {
            e.printStackTrace();
        }


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        int width = options.outWidth;
        int height = options.outHeight;

        Log.e(TAG, "showBitmapInfos: \n" +
                "width=: " + width + "\n" +
                "height=: " + height);
        options.inJustDecodeBounds = false;
    }

    /**
     * 将Bitmap按比例压缩后返回
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCompressedBitmap(Bitmap bitmap) {
        try {
            //创建一个用于存储压缩后Bitmap的文件
            File compressedFile = FileHelper.createFileByType(mContext, destType, "compressed");
            Uri uri = Uri.fromFile(compressedFile);
            OutputStream os = getContentResolver().openOutputStream(uri);
            Bitmap.CompressFormat format = destType == FileHelper.JPEG ?
                    Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG;
            boolean success = bitmap.compress(format, compressRate, os);
            if (success) {
                T.showLToast(mContext, "success");
            }

            final String pathName = FileHelper.stripFileProtocol(uri.toString());
            showBitmapInfos(pathName);
            bitmap = BitmapFactory.decodeFile(pathName);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PermissionChecker.PERMISSION_DENIED) {
                        String reason = permissions[i] + " is denied !";
                        T.showLToast(mContext, reason);
                        return;
                    }
                }
                openCamera();
                break;
            default:
                break;
        }
    }

    private boolean hasPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PermissionChecker.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                if (hasPermission(permissions)) {
                    mImageView.setImageDrawable(null);
                    info.setText("");
                    openCamera();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, permissions, REQUEST_CAMERA);
                }
                break;
            case R.id.select:
                if (hasPermission(permissions)) {
                    mImageView.setImageDrawable(null);
                    selectFromGalley();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, permissions, REQUEST_WRITE_STORAGE);
                }
            default:
                break;
        }
    }


    private void InitView() {
        findViewById(R.id.open).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);
        mImageView = V.f(this, R.id.image);
        info = V.f(this, R.id.info);
        rate = V.f(this, R.id.rate);
        rateShell = V.f(this, R.id.rateShell);
        compressSeekBar = V.f(this, R.id.compressRate);
        edit = V.f(this, R.id.edit);
        compress = V.f(this, R.id.compress);
        sample = V.f(this, R.id.sample);
        insert = V.f(this, R.id.insert);
        cropWidthShell = V.f(this, R.id.cropWidthShell);
        cropWidthValue = V.f(this, R.id.cropWidthValue);
        cropWidth = V.f(this, R.id.cropWidth);
        cropHeightShell = V.f(this, R.id.cropHeightShell);
        cropHeightValue = V.f(this, R.id.cropHeightValue);
        cropHeight = V.f(this, R.id.cropHeight);

        compressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                compressRate = progress;
                rate.setText("quality: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cropWidthValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cropTargetWidth = progress;
                cropWidth.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cropHeightValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cropTargetHeight = progress;
                cropHeight.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needEdit = isChecked;
                if (isChecked) {
                    cropWidthShell.setVisibility(View.VISIBLE);
                    cropHeightShell.setVisibility(View.VISIBLE);
                    cropWidthValue.setProgress(1080);
                    cropHeightValue.setProgress(960);
                } else {
                    cropHeightShell.setVisibility(View.GONE);
                    cropWidthShell.setVisibility(View.GONE);
                }
            }
        });

        compress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needCompress = isChecked;
                if (isChecked) {
                    rateShell.setVisibility(View.VISIBLE);
                } else {
                    rateShell.setVisibility(View.GONE);
                }
            }
        });

        sample.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needSample = isChecked;
            }
        });

        insert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                insertToGallery = isChecked;

            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
