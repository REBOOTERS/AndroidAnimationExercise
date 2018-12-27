package home.smart.fly.animations;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhihu.android.sugaradapter.SugarAdapter;
import com.zhihu.android.sugaradapter.SugarHolder;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.sugar.bean.Item;
import home.smart.fly.animations.sugar.viewholder.LargeItemHolder;
import home.smart.fly.animations.sugar.viewholder.SmallItemHolder;

public class FileUtilsActivity extends AppCompatActivity {

    private List<Item> items;
    private SugarAdapter mSugarAdapter;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_file_utils);
        mRecyclerView = findViewById(R.id.recyclerView);

        items = new ArrayList<>();
        mSugarAdapter = SugarAdapter.Builder.with(items)
                .add(LargeItemHolder.class)
                .add(SmallItemHolder.class)
                .build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSugarAdapter);

        mSugarAdapter.addDispatcher(new SugarAdapter.Dispatcher<Item>() {
            @Nullable
            @Override
            public Class<? extends SugarHolder> dispatch(@NonNull Item data) {
                if (data.getTitle().length() < 40) {
                    return LargeItemHolder.class;
                }
                return SmallItemHolder.class;
            }
        });

        refreshList();
    }


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

        items.add(new Item("android.os.Build.VERSION.SDK_INT", String.valueOf(version)));
        items.add(new Item("android.os.Build.VERSION.RELEASE", String.valueOf(mRelease)));
        items.add(new Item("android.os.Build.SERIAL", String.valueOf(mSerial)));
        items.add(new Item("Secure.ANDROID_ID", String.valueOf(android_id)));
        items.add(new Item("mManager.getMemoryClass()  应用可用内存", String.valueOf(size)+"M"));

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

        items.add(new Item("mContext.mContext.getExternalCacheDir()()", String.valueOf(getExternalCacheDir)));
        items.add(new Item("mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)", String.valueOf(getExternalFilesDir_DIRECTORY_PICTURES)));
        items.add(new Item("mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)", String.valueOf(getExternalFilesDir_DIRECTORY_DOCUMENTS)));
        items.add(new Item("mContext.getFilesDir()", String.valueOf(filesDir)));
        items.add(new Item("mContext.getFilesDir()", String.valueOf(filesDir)));

        mSugarAdapter.notifyDataSetChanged();
    }
}
