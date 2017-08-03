package home.smart.fly.animations.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import home.smart.fly.animations.R;
import home.smart.fly.animations.activity.CollegeActivity;

/**
 * Created by Rookie on 2017/8/3.
 */

public class PanoWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_REFRESH = "home.smart.fly.animations.widget.action.REFRESH";


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_REFRESH)) {
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        // Create an Intent to launch CollegeActivity
        Intent intent = new Intent(context, CollegeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);
        views.setOnClickPendingIntent(R.id.image, pendingIntent);


        Intent mIntent = new Intent();
        mIntent.setAction(ACTION_REFRESH);
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, mIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_refresh, refreshPendingIntent);


        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
