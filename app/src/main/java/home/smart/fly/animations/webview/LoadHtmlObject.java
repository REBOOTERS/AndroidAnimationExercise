package home.smart.fly.animations.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by Rookie on 2017/8/7.
 */

public class LoadHtmlObject {
    private static final String TAG = "LoadHtmlObject";

    @JavascriptInterface
    public void printSourceCode(String html) {
        Log.e(TAG, "printSourceCode: " + html);
    }
}
