package home.smart.fly.animations.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Rookie on 2017/10/19.
 */

public class MyWebChromeClient extends WebChromeClient {
    private static final String TAG = "MyWebChromeClient";
    public MyWebChromeClient() {
        super();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.e(TAG, "onProgressChanged: newProgress=" + newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        Log.e(TAG, "onReceivedTitle: title=" + title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        super.onReceivedTouchIconUrl(view, url, precomposed);
        Log.e(TAG, "onReceivedTouchIconUrl: url=" + url);
        Log.e(TAG, "onReceivedTouchIconUrl: precomposed=" + precomposed);
    }


    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.e(TAG, "onJsAlert: url=" + url);
        Log.e(TAG, "onJsAlert: message=" + message);
        Log.e(TAG, "onJsAlert: result=" + result.toString());
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.e(TAG, "onJsConfirm: url=" + url);
        Log.e(TAG, "onJsConfirm: message=" + message);
        Log.e(TAG, "onJsConfirm: result=" + result.toString());
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        return super.onJsPrompt(view, url, message, defaultValue, result);
    }


    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
        Log.e(TAG, "getVisitedHistory: callback=" + callback.toString());
    }
}
