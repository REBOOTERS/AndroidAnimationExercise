package home.smart.fly.animations.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.File;

import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.CameraActivity;
import home.smart.fly.animations.ui.activity.ClipBoardActivity;
import home.smart.fly.animations.ui.activity.FullscreenActivity;
import home.smart.fly.animations.ui.activity.ImgCacheActivity;
import home.smart.fly.animations.ui.activity.InputActivity;
import home.smart.fly.animations.ui.activity.LaunchOtherAppActivity;
import home.smart.fly.animations.ui.activity.LoadCircleImageActivity;
import home.smart.fly.animations.ui.activity.MailActivity;
import home.smart.fly.animations.ui.activity.MergeIncludeViewActivity;
import home.smart.fly.animations.ui.activity.OptionalActivity;
import home.smart.fly.animations.ui.activity.OrientationActivity;
import home.smart.fly.animations.ui.activity.PdfActivity;
import home.smart.fly.animations.ui.activity.ScreenCaptureActivity;
import home.smart.fly.animations.ui.activity.ViewPagerNestedActivity;
import home.smart.fly.animations.ui.activity.ViewStubActivity;
import home.smart.fly.animations.ui.activity.multifragments.MultiFragmentsActivity;
import home.smart.fly.animations.bga.BgaAllActivity;
import home.smart.fly.animations.customview.bottomsheet.BottomSheetActivity;
import home.smart.fly.animations.customview.puzzle.PuzzleActivity;
import home.smart.fly.animations.fragments.base.BaseFragment;
import home.smart.fly.animations.fragments.base.ItemInfo;
import home.smart.fly.animations.fragments.base.RoutePaths;
import home.smart.fly.animations.recyclerview.BaseRecyclerViewActivity;
import home.smart.fly.animations.webview.AllWebViewActivity;


/**
 * Created by rookie on 2016/8/12.
 */
@Route(path = RoutePaths.OTHER)
public class OtherFragment extends BaseFragment {

    @Override
    public void InitView() {
        demos.add(new ItemInfo(R.string.title_activity_multi_fragments, MultiFragmentsActivity.class));
        demos.add(new ItemInfo(R.string.take_screen, ScreenCaptureActivity.class));
        demos.add(new ItemInfo(R.string.ViewStub, ViewStubActivity.class));
        demos.add(new ItemInfo(R.string.ViewMerge, MergeIncludeViewActivity.class));
        demos.add(new ItemInfo(R.string.launch_app, LaunchOtherAppActivity.class));
        demos.add(new ItemInfo(R.string.viewpager_nested, ViewPagerNestedActivity.class));
        demos.add(new ItemInfo(R.string.full_screen, FullscreenActivity.class));
        demos.add(new ItemInfo(R.string.pdf_view, PdfActivity.class));
        demos.add(new ItemInfo(R.string.mail_view, MailActivity.class));
        demos.add(new ItemInfo(R.string.puzzle_game, PuzzleActivity.class));
        demos.add(new ItemInfo(R.string.webviewInfo, AllWebViewActivity.class));
        demos.add(new ItemInfo(R.string.camera, CameraActivity.class));
        demos.add(new ItemInfo(R.string.bottomSheet, BottomSheetActivity.class));
        demos.add(new ItemInfo(R.string.image_cache, ImgCacheActivity.class));
        demos.add(new ItemInfo(R.string.bga_view, BgaAllActivity.class));
        demos.add(new ItemInfo(R.string.circle_view, LoadCircleImageActivity.class));
        demos.add(new ItemInfo(R.string.base_recyclerview, BaseRecyclerViewActivity.class));
        demos.add(new ItemInfo(R.string.input_view, InputActivity.class));
        demos.add(new ItemInfo(R.string.orientation, OrientationActivity.class));
        demos.add(new ItemInfo(R.string.optional, OptionalActivity.class));
        demos.add(new ItemInfo(R.string.clipboard, ClipBoardActivity.class));

        PrintSystemDirInfo();
    }

    /**
     * 打印系统目录信息
     */
    private void PrintSystemDirInfo() {


        final int version = Build.VERSION.SDK_INT;
        final String mRelease = Build.VERSION.RELEASE;
        final String mSerial = Build.SERIAL;
        String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //
        String getDataDirectory = Environment.getDataDirectory().getAbsolutePath();
        String getRootDirectory = Environment.getRootDirectory().getAbsolutePath();
        String getDownloadCacheDirectory = Environment.getDownloadCacheDirectory().getAbsolutePath();
        //
        String DIRECTORY_DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String DIRECTORY_DOCUMENTS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        String DIRECTORY_PICTURES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        String DIRECTORY_DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        //
        String cacheDir = mContext.getCacheDir().getAbsolutePath();
        String filesDir = mContext.getFilesDir().getAbsolutePath();
        //
        String getExternalCacheDir = mContext.getExternalCacheDir().getAbsolutePath();
        String getExternalFilesDir_DIRECTORY_PICTURES = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        String getExternalFilesDir_DIRECTORY_DOCUMENTS = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();


        String[] files = new String[mContext.getExternalCacheDirs().length];
        for (int i = 0; i < mContext.getExternalCacheDirs().length; i++) {
            File mFile = mContext.getExternalCacheDirs()[i];
            if (mFile != null) {
                files[i] = mFile.getAbsolutePath() + "\n";

            }
        }


        Log.e("device_info", "android.os.Build.VERSION.SDK_INT = " + version);
        Log.e("device_info", "android.os.Build.VERSION.RELEASE = " + mRelease);
        Log.e("device_info", "android.os.Build.SERIAL = " + mSerial);
        Log.e("device_info", "Secure.ANDROID_ID = " + android_id);
        Log.e("device_info", "--------------------------------------------------");
        Log.e("device_info", "Environment.getExternalStorageDirectory() = " + filepath);
        Log.e("device_info", "Environment.getDataDirectory() = " + getDataDirectory);
        Log.e("device_info", "Environment.getRootDirectory() = " + getRootDirectory);
        Log.e("device_info", "Environment.getDownloadCacheDirectory() = " + getDownloadCacheDirectory);
        Log.e("device_info", "--------------------------------------------------");
        Log.e("device_info", "Environment.getExternalStorageDirectory(Environment.DIRECTORY_DCIM) = " + DIRECTORY_DCIM);
        Log.e("device_info", "Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOCUMENTS) = " + DIRECTORY_DOCUMENTS);
        Log.e("device_info", "Environment.getExternalStorageDirectory(Environment.DIRECTORY_PICTURES) = " + DIRECTORY_PICTURES);
        Log.e("device_info", "Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOWNLOADS) = " + DIRECTORY_DOWNLOADS);
        Log.e("device_info", "--------------------------------------------------");
        Log.e("device_info", "mContext.getCacheDir() = " + cacheDir);
        Log.e("device_info", "mContext.getFilesDir() = " + filesDir);
        Log.e("device_info", "--------------------------------------------------");
        Log.e("device_info", "mContext.getExternalCacheDir() = " + getExternalCacheDir);
        Log.e("device_info", "mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) = " + getExternalFilesDir_DIRECTORY_PICTURES);
        Log.e("device_info", "mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) = " + getExternalFilesDir_DIRECTORY_DOCUMENTS);
        Log.e("device_info", "mContext.getExternalCacheDirs() size= " + files.length + " \n [ ");
        for (String str : files) {
            if (str != null) {
                Log.e("device_info", str);
            }
        }

        ActivityManager mManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        int size = mManager.getMemoryClass();

        Log.e("device_info", "mManager.getMemoryClass()  应用可用内存 = " + size + " M");

    }
}
