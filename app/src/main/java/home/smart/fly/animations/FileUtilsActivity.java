package home.smart.fly.animations;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import home.smart.fly.animations.internal.annotations.Tiger;
import home.smart.fly.animations.sugar.bean.Item;
import home.smart.fly.animations.utils.AppUtils;
import home.smart.fly.animations.utils.BaseListKt;
import home.smart.fly.animations.utils.RxBus;
import home.smart.fly.animations.utils.SimpleEvent;
import home.smart.fly.animations.utils.StatusBarUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;

@Tiger
public class FileUtilsActivity extends AppCompatActivity {

    private static final String TAG = "FileUtilsActivity";

    private List<Item> items;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private FloatingActionButton mRetry;
    private FloatingActionButton mRxBus;
    private String mNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_file_utils);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRetry = findViewById(R.id.retry);
        mRxBus = findViewById(R.id.rxbus);

        items = new ArrayList<>();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshList();
        BaseListKt.bind(mRecyclerView, items).map(R.layout.large_info_item, (item, integer) -> item.getTitle().length() < 40, (view, item, integer) -> {
            TextView titleTv = view.findViewById(R.id.title);
            TextView descTv = view.findViewById(R.id.desc);
            titleTv.setText(item.getTitle());
            descTv.setText(item.getSubTitle());
            return Unit.INSTANCE;
        }).map(R.layout.small_info_item, (item, integer) -> item.getTitle().length() >= 40, (view, item, integer) -> {
            TextView titleTv = view.findViewById(R.id.title);
            TextView descTv = view.findViewById(R.id.desc);
            titleTv.setText(item.getTitle());
            descTv.setText(item.getSubTitle());
            return Unit.INSTANCE;
        });

        mRetry.setOnClickListener(v -> recyclerview());

        mRxBus.setOnClickListener(v -> RxBus.getInstance().post(new SimpleEvent(FileUtilsActivity.class.getSimpleName())));

        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("name", "mike");
        arrayMap.put("address", "beijing");

        for (String sets : arrayMap.keySet()) {
            Log.e(TAG, "onCreate: sets=" + sets);
        }

        for (String values : arrayMap.values()) {
            Log.e(TAG, "onCreate: values=" + values);
        }
        if (!TextUtils.isEmpty("3")) {

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        printCommonInfo();
    }

    private void recyclerview() {
        printCommonInfo();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 300, 0);
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            mRecyclerView.setX(value);
            mRecyclerView.setY(value);

            mRetry.setScaleX(animation.getAnimatedFraction());
            mRetry.setScaleY(animation.getAnimatedFraction());
        });
        valueAnimator.setDuration(3000);
        valueAnimator.start();
    }

    //region printCommonInfo
    private void printCommonInfo() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.e(TAG, "recyclerview: top==   " + mRecyclerView.getTop());
        Log.e(TAG, "recyclerview: left==  " + mRecyclerView.getLeft());
        Log.e(TAG, "recyclerview: bottom==" + mRecyclerView.getBottom());
        Log.e(TAG, "recyclerview: right== " + mRecyclerView.getRight());

        Log.e(TAG, "recyclerview: x==     " + mRecyclerView.getX());
        Log.e(TAG, "recyclerview: y==     " + mRecyclerView.getY());

        Log.e(TAG, "recyclerview: width== " + mRecyclerView.getWidth());
        Log.e(TAG, "recyclerview: height==" + mRecyclerView.getHeight());

        Log.e(TAG, " \n\n");

        Log.e(TAG, "viewinfos: statusH=" + StatusBarUtil.getStatusBarHeight(this));
        Log.e(TAG, "viewinfos: screenW=" + displayMetrics.widthPixels);
        Log.e(TAG, "viewinfos: screenH=" + displayMetrics.heightPixels);
        Log.e(TAG, "viewinfos: density=" + displayMetrics.density);
        Log.e(TAG, "viewinfos: scaledDensity=" + displayMetrics.scaledDensity);
        Log.e(TAG, "viewinfos: xdpi=" + displayMetrics.xdpi);
        Log.e(TAG, "viewinfos: ydpi=" + displayMetrics.ydpi);
        Log.e(TAG, "viewinfos: densityDpi=" + displayMetrics.densityDpi);

        Log.e(TAG, " \n\n");

        Log.e(TAG, "retry_fab: top==   " + mRetry.getTop());
        Log.e(TAG, "retry_fab: left==  " + mRetry.getLeft());
        Log.e(TAG, "retry_fab: bottom==" + mRetry.getBottom());
        Log.e(TAG, "retry_fab: right== " + mRetry.getRight());
    }
    //endregion

    //<editor-fold desc="Description">
    private void refreshList() {

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

        ActivityManager mManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        int size = mManager.getMemoryClass();

        items.add(new Item("应用包名", AppUtils.getPackageName(this)));

        items.add(new Item("BuildConfig.APPLICATION_ID", String.valueOf(BuildConfig.APPLICATION_ID)));
        items.add(new Item("BuildConfig.BUILD_TYPE", String.valueOf(BuildConfig.BUILD_TYPE)));
        items.add(new Item("BuildConfig.FLAVOR", String.valueOf(BuildConfig.FLAVOR)));
        items.add(new Item("BuildConfig.VERSION_NAME", String.valueOf(BuildConfig.VERSION_NAME)));
        items.add(new Item("BuildConfig.VERSION_CODE", String.valueOf(BuildConfig.VERSION_CODE)));

        items.add(new Item("android.os.Build.VERSION.SDK_INT", String.valueOf(version)));
        items.add(new Item("android.os.Build.VERSION.RELEASE", String.valueOf(mRelease)));
        items.add(new Item("android.os.Build.SERIAL", String.valueOf(mSerial)));
        items.add(new Item("Secure.ANDROID_ID", String.valueOf(android_id)));
        items.add(new Item("mManager.getMemoryClass()  应用可用内存", String.valueOf(size) + "M"));

        items.add(new Item("Environment.getExternalStorageDirectory()", String.valueOf(filepath)));
        items.add(new Item("Environment.getDataDirectory()", String.valueOf(getDataDirectory)));
        items.add(new Item("Environment.getRootDirectory()", String.valueOf(getRootDirectory)));
        items.add(new Item("Environment.getDownloadCacheDirectory()", String.valueOf(getDownloadCacheDirectory)));

        items.add(new Item("Environment.getExternalStorageDirectory(Environment.DIRECTORY_DCIM)", String.valueOf(DIRECTORY_DCIM)));
        items.add(new Item("Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOCUMENTS)", String.valueOf(DIRECTORY_DOCUMENTS)));
        items.add(new Item("Environment.getExternalStorageDirectory(Environment.DIRECTORY_PICTURES)", String.valueOf(DIRECTORY_PICTURES)));
        items.add(new Item("Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOWNLOADS)", String.valueOf(DIRECTORY_DOWNLOADS)));

        items.add(new Item("mContext.getCacheDir()", String.valueOf(cacheDir)));
        items.add(new Item("mContext.getFilesDir()", String.valueOf(filesDir)));

        items.add(new Item("mContext.getExternalCacheDir()()", String.valueOf(getExternalCacheDir)));
        items.add(new Item("mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)", String.valueOf(getExternalFilesDir_DIRECTORY_PICTURES)));
        items.add(new Item("mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)", String.valueOf(getExternalFilesDir_DIRECTORY_DOCUMENTS)));


        test();
    }
    //</editor-fold>

    private void test() {
        Map map = JSONObject.parseObject(json, Map.class);
        for (Object o : map.keySet()) {
            JSONArray JSONArray = (com.alibaba.fastjson.JSONArray) map.get(o);
            List<Info> list = convert1(JSONArray, Info.class);
            for (Info info : list) {
                Log.d(TAG, "test() called key = " + o + ",list = " + info);
            }
        }
    }

    public <T> List<T> convert1(JSONArray array, Class<T> clazz) {
        if (array.isEmpty()) {
            return new ArrayList<T>();
        }
        ArrayList<T> result = new ArrayList<>();

        for (Object o : array) {
            if (o instanceof Number || o instanceof String || o instanceof Boolean) {
                result.add((T) o);
            } else {
                T t = JSONObject.toJavaObject((JSON) o, clazz);
                result.add(t);
            }
        }
        return result;
    }

    private static class Info {
        public String time;
        public String value;

//        public Info() {}

        @Override
        public String toString() {
            return "Info{" + "time='" + time + '\'' + ", value='" + value + '\'' + '}';
        }
    }

    public static final String json = "{\n" + "  \"2022-02-01\": [\n" + "    {\n" + "      \"time\": \"mike\",\n" + "      \"value\": \"北京\"\n" + "    },\n" + "    {\n" + "      \"time\": \"mike1\",\n" + "      \"value\": \"北京1\"\n" + "    },\n" + "    {\n" + "      \"time\": \"mike2\",\n" + "      \"value\": \"北京2\"\n" + "    }\n" + "  ],\n" + "  \"2022-02-02\": [\n" + "    {\n" + "      \"time\": \"lucy\",\n" + "      \"value\": \"南京\"\n" + "    },\n" + "    {\n" + "      \"time\": \"lucy1\",\n" + "      \"value\": \"南京1\"\n" + "    }\n" + "  ],\n" + "  \"2022-02-03\": [\n" + "    {\n" + "      \"time\": \"lily\",\n" + "      \"value\": \"西安\"\n" + "    },\n" + "    {\n" + "      \"time\": \"lily1\",\n" + "      \"value\": \"西安1\"\n" + "    },\n" + "    {\n" + "      \"time\": \"lily2\",\n" + "      \"value\": \"西安2\"\n" + "    }\n" + "  ],\n" + "  \"2022-02-04\": [\n" + "    {\n" + "      \"time\": \"tom\",\n" + "      \"value\": \"tokyo\"\n" + "    },\n" + "    {\n" + "      \"time\": \"tom1\",\n" + "      \"value\": \"tokyo1\"\n" + "    },\n" + "    {\n" + "      \"time\": \"tom2\",\n" + "      \"value\": \"tokyo2\"\n" + "    }\n" + "  ]\n" + "}";

}
