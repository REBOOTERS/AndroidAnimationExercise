package home.smart.fly.animations.internal.core;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class BeautyLog {
    private static final String TAG = "Cat";
    private static final String TRACK = "Track";

    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "───────────────────────────────────------";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;

    private static final String CLASS_NAME_FORMAT = "%s class's name:       %s";
    private static final String CLASS_FORMAT = "%s class's name:             %s";
    private static final String METHOD_NAME_FORMAT = "%s method's name:      %s";
    private static final String VIEW_NAME_FORMAT = "%s           view's id:      %s";
    private static final String PACK_NAME_FORMAT = "%s view's package name:      %s";
    private static final String ARGUMENT_FORMAT = "%s method's arguments: %s";
    private static final String RESULT_FORMAT = "%s method's result:    %s";
    private static final String COST_TIME_FORMAT = "%s method cost time:   %.2f ms";

    public static void printMethodInfo(MethodInfo methodInfo) {
        Log.e(0 + TAG, TOP_BORDER);
        Log.e(1 + TAG, String.format(CLASS_NAME_FORMAT, HORIZONTAL_LINE, methodInfo.getClassName()));
        Log.e(2 + TAG, String.format(METHOD_NAME_FORMAT, HORIZONTAL_LINE, methodInfo.getMethodName()));
        Log.e(3 + TAG, String.format(ARGUMENT_FORMAT, HORIZONTAL_LINE, methodInfo.getParams()));
        Log.e(4 + TAG, String.format(RESULT_FORMAT, HORIZONTAL_LINE, methodInfo.getResult()));
        Log.e(5 + TAG, String.format(Locale.CHINA, COST_TIME_FORMAT, HORIZONTAL_LINE, methodInfo.getTime()));
        Log.e(6 + TAG, BOTTOM_BORDER);
    }

    public static void printClickInfo(View view) {
        Context context = view.getContext();

        Class contextClass = context.getClass();
        String humanId = context.getResources().getResourceName(view.getId());
        String packageName = context.getResources().getResourcePackageName(view.getId());

        Log.e(0 + TRACK, TOP_BORDER);
        Log.e(1 + TRACK, String.format(CLASS_FORMAT, HORIZONTAL_LINE, contextClass.getCanonicalName()));
        Log.e(2 + TRACK, String.format(VIEW_NAME_FORMAT, HORIZONTAL_LINE, humanId));
        Log.e(3 + TRACK, String.format(PACK_NAME_FORMAT, HORIZONTAL_LINE, packageName));
        Log.e(4 + TRACK, BOTTOM_BORDER);

    }
}
