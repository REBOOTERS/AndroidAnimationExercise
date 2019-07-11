package home.smart.fly.animations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.engineer.dateview.api.DataView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import home.smart.fly.animations.fragments.base.BaseFragment;
import home.smart.fly.animations.fragments.base.RoutePaths;
import home.smart.fly.animations.internal.NormalStatus;
import home.smart.fly.animations.internal.Single;
import home.smart.fly.animations.ui.SuperTools;
import home.smart.fly.animations.ui.activity.AllActivity;
import home.smart.fly.animations.utils.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class AppStartActivity extends AppCompatActivity {
    private static final String TAG = "AppStartActivity";
    private Snackbar snackbar = null;
    private CoordinatorLayout main_contetn;
    private Context mContext;
    private AppCompatAutoCompleteTextView mAutoCompleteTextView;

    private AppBarLayout mAppBarLayout;

    private SharedPreferences mPreferences; // 简单粗暴保存一下位置，后期封装一下 TODO


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        mContext = this;
        mPreferences = getSharedPreferences("fragment_pos", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        mAppBarLayout = findViewById(R.id.appbar);
        mAutoCompleteTextView = findViewById(R.id.auto_complete_text);
        // Setup spinner
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                getResources().getStringArray(R.array.fragments)));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                BaseFragment fragment = (BaseFragment) ARouter.getInstance().build(RoutePaths.paths[position]).navigation();
                transition.replace(R.id.container, fragment).commit();
                saveLastSelect(position);

                int resId = fragment.getBackgroundResId();
                int color = PaletteUtils.getMagicColor(getResources(), resId);
                toolbar.setBackgroundColor(color);
                StatusBarUtil.setColor(AppStartActivity.this, color, 0);
                mAutoCompleteTextView.setBackgroundColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setSelection(getLastPosition());
        main_contetn = findViewById(R.id.main_content);
        findViewById(R.id.fab).setOnClickListener(v -> ARouter.getInstance()
                .build("/index/kotlin").navigation(mContext, new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        super.onLost(postcard);
                        snackbar = Snackbar.make(main_contetn, R.string.module_info, Snackbar.LENGTH_LONG)
                                .setAction("知道了", v1 -> snackbar.dismiss());
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        snackbar.setActionTextColor(getResources().getColor(R.color.white));
                        snackbar.show();
                    }
                }));
        print();

        setupAutoCompleteTextView();

        reportFullyDrawn();

        howEnumWork();

        System.out.println("enum " + Single.INSTANCE.getValue());
        System.out.println("enum " + Single.INSTANCE.hashCode());

        RxBus.getInstance().toObservable(SimpleEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(simpleEvent -> Toast.makeText(mContext,simpleEvent.getMsg(),Toast.LENGTH_SHORT).show());
    }

    /**
     * 枚举是如何工作的
     */
    private void howEnumWork() {
        System.out.println("enum " + NormalStatus.SUCCESS.name());
        System.out.println("enum " + Arrays.toString(NormalStatus.values()));
        System.out.println("enum " + NormalStatus.getStats(1));
        System.out.println("enum " + NormalStatus.getStats(0));
        System.out.println("enum " + NormalStatus.getStats(-1));

        System.out.println("enum " + NormalStatus.getValue(NormalStatus.SUCCESS));
        System.out.println("enum " + NormalStatus.getValue(NormalStatus.LOADING));
        System.out.println("enum " + NormalStatus.getValue(NormalStatus.FAIL));
    }

    // <editor-fold defaultstate="collapsed" desc="一些屏幕信息">
    private void print() {

        print("是否存在导航栏 ：" + SuperTools.checkDeviceHasNavigationBar(this));
        print("不包含虚拟导航栏的高度 ： " + SuperTools.getNoHasVirtualKey(this));
        print("包含虚拟导航栏的高度 : " + SuperTools.getHasVirtualKey(this));
        print("getRealHight =" + (SuperTools.getRealHight(this)));
        print("getScreenHeight =" + (SuperTools.getScreenHeight(this)));
        print("导航栏高度：" + SuperTools.getNavigationBarHeight(this));
        print("DecorView高度：" + getWindow().getDecorView().getHeight());
        print("华为手机 ：" + SuperTools.isHUAWEI());
    }

    // </editor-fold>


    private void print(String msg) {
        Log.e("Nj", msg);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String info = BuildConfig.BUILD_TYPE + "-" + BuildConfig.VERSION_NAME;
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
        }
        MMKV.defaultMMKV().putBoolean("running", true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MMKV.defaultMMKV().putBoolean("running", true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e(TAG, "onWindowFocusChanged: " + mAutoCompleteTextView.getMeasuredHeight());
        Log.e(TAG, "onWindowFocusChanged: " + mAutoCompleteTextView.getMeasuredWidth());
    }

    private void saveLastSelect(int lastPosition) {
        mPreferences.edit().putInt("pos", lastPosition).apply();
    }

    private int getLastPosition() {
        return mPreferences.getInt("pos", 2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_start, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //仿home键效果
        moveTaskToBack(true);
//        MMKV.defaultMMKV().putBoolean("running", true);
    }

    // <editor-fold defaultstate="collapsed" desc="onOptionsItemSelected">

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.file_path.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(AppStartActivity.this, SettingsActivity.class));
            return true;
        } else if (id == R.id.change_theme) {
            try {
                Object object = new Object();
                object = null;
                String result = String.valueOf(object.hashCode());
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "catch a null object", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.action_file_util) {
            startActivity(new Intent(mContext, FileUtilsActivity.class));
            return true;
        } else if (id == R.id.action_all) {
            startActivity(new Intent(mContext, AllActivity.class));
            return true;
        } else if (id == R.id.data_view_chart) {
            DataView.show(this);
        }
        return super.onOptionsItemSelected(item);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SpinnerAdapter">
    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        private MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
    // </editor-fold>

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // <editor-fold defaultstate="collapsed" desc="setupAutoCompleteTextView">
    private void setupAutoCompleteTextView() {
        List<String> activites = new ArrayList<>();
        List<String> activitesAll = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(AppUtils.getPackageName(this),
                    PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityInfos = packageInfo.activities;
            for (ActivityInfo activityInfo : activityInfos) {
                String activity = activityInfo.name;
                int dotIndex = activity.lastIndexOf(".");
                String act = activity.substring(dotIndex + 1);
//                Log.e(TAG, "setupAutoCompleteTextView: " + act);
//                Log.e(TAG, "setupAutoCompleteTextView: " + activity);
                if (act.equals("AppStartActivity")) {
                    continue;
                }
                activites.add(act);
                activitesAll.add(activity);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, activites);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.setDropDownBackgroundResource(R.color.white);
        mAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                    try {
                        String content = mAutoCompleteTextView.getText().toString().trim();
                        if (activites.contains(content)) {
                            int index = activites.indexOf(content);
                            String target = activitesAll.get(index);
                            Class<?> targetClass = Class.forName(target);
                            Intent intent = new Intent(this, targetClass);
                            startActivity(intent);
                            mAutoCompleteTextView.setText("");
                        }

                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "setupAutoCompleteTextView: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
        );
    }
    // </editor-fold>
}
