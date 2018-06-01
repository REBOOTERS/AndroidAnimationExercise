package home.smart.fly.animations.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import home.smart.fly.animations.R;

public class AppWidgetService extends Service implements Runnable {
    private static final String TAG = "AppWidgetService";

    private SharedPreferences sp;

    private boolean live = false;
    private int index;

    static int[] ids = {R.drawable.a5, R.drawable.a6, R.drawable.totoro,};


    public AppWidgetService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        sp = getApplicationContext().getSharedPreferences(PanoWidgetProvider.SP_NAME, MODE_PRIVATE);
        live = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (live) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(getPackageName(),
                    R.layout.app_widget_layout);//远程视图绑定布局视图
            views.setImageViewResource(R.id.image, ids[index]);//设置对应views中的内容（图片）
            ComponentName provider = new ComponentName(this, PanoWidgetProvider.class);//提供者
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            mAppWidgetManager.updateAppWidget(provider, views);//更新视图
            index = (++index) % ids.length;// 取余，循环下标

            Log.e(TAG, "run: index=" + index);

            sp.edit().putInt(PanoWidgetProvider.INDEX_KEY, index).apply();

            try {
                Thread.sleep(15 * 1000);// 15s刷新一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        live = false;
    }
}
