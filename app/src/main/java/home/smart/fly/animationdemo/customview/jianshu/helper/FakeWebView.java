package home.smart.fly.animationdemo.customview.jianshu.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.net.URL;

/**
 * Created by rookie on 2017-03-14.
 */

public class FakeWebView extends WebView {

    private WebView webView;

    public FakeWebView(Context context) {
        super(context);
        Init(context);
    }


    public FakeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public FakeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        webView = this;
        webView.setPadding(10, 10, 10, 10);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return true;
            }

            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response = null;
                for (String path : WebViewHelper.getInstance().getAllListPath()) {
                    if (path.toLowerCase().contains(url.replace("file://", "").toLowerCase())) {
                        try {
                            response = new WebResourceResponse("image/png", "UTF-8", new URL(path).openStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return response;
            }
        });
    }

    /**
     * 外部进行初始化
     *
     * @param strData 基本内容
     * @param userInfo 用户信息
     * @param userName 用户名称
     * @param other 其他信息
     */
    private boolean isFirstLoad = false;

    /**
     * 初始化页面
     *
     * @param strData
     * @param userInfo
     * @param userName
     * @param other
     */
    public void init(final String strData, final String userInfo, final String userName, final String other) {
        if (Build.VERSION.SDK_INT >= 21) {
            isFirstLoad = true;
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        if (isFirstLoad) {
                            isFirstLoad = false;
                            Log.e("TAG", "onProgressChanged");
                            changeDay(strData, userInfo, userName, other);
                        }
                    }
                }
            });
            webView.loadUrl("file:///android_asset/generate_pic.html");
        } else {
            isFirstLoad = true;
            webView.setVisibility(View.INVISIBLE);

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        changeDay(strData, userInfo, userName, other);
                        if (!isFirstLoad)
                            webView.setVisibility(View.VISIBLE);
                    }
                }
            });
            webView.loadUrl("file:///android_asset/generate_pic.html");
        }

    }

    /**
     * 白
     *
     * @param strData 内容
     */
    public void changeDay(String strData, String userInfo, String userName, String other) {
        if (userInfo == null)
            userInfo = "";
        if (strData == null)
            strData = "";
        if (userName == null)
            userName = "";
        if (other == null)
            other = "";
        strData += "<br /><br />\n" +
                "<span style=\"font-size: small;color: gray;line-height:150%;\">" + userInfo + "</span>\n" +
                "<br /><br />\n" +
                "<hr style=\"margin: auto;border:0;background-color:gray;height:1px;\"/>\n" +
                "<br />\n" +
                "<p style=\"color: orangered;font-size: x-small;text-align: center;letter-spacing: 0.5px;\">由<strong>" + userName + "</strong>发送 " + other + "</p>";

        webView.loadUrl("javascript:changeContent(\"" + strData.replace("\n", "\\n").replace("\"", "\\\"").replace("'", "\\'") + "\")");
        webView.setBackgroundColor(Color.WHITE);
        if (Build.VERSION.SDK_INT < 21) {
            if (isFirstLoad) {
                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirstLoad = false;
                        webView.reload();//为解决部分手机打开不显示图片问题
                    }
                }, 500);
            }
        }
    }

    /**
     * 夜
     *
     * @param strData  基本内容
     * @param userInfo 用户信息
     * @param userName 用户名称
     * @param other    其他信息
     */
    public void changeNight(String strData, String userInfo, String userName, String other) {
        if (userInfo == null)
            userInfo = "";
        if (strData == null)
            strData = "";
        if (userName == null)
            userName = "";
        if (other == null)
            other = "";
        strData = "<div style=\"color: gray;display: inline;\">" + strData + "</div>";
        strData += "<br /><br />\n" +
                "\t\t<span style=\"font-size: small;color: gray;line-height: 150%;\">" + userInfo + "</span>\n" +
                "\t\t<br /><br />\n" +
                "\t\t<hr style=\"margin: auto;border:0;background-color:gray;height:0.5px;\"/>\n" +
                "\t\t<br />\n" +
                "\t\t<p style=\"color: orangered;font-size: x-small;text-align: center;letter-spacing: 0.5px;\">由<strong>" + userName + "</strong>发送 " + other + "</p>";

        webView.loadUrl("javascript:changeContent(\"" + strData.replace("\n", "\\n").replace("\"", "\\\"").replace("'", "\\'") + "\")");
        webView.setBackgroundColor(Color.parseColor("#263238"));
    }

    /**
     * 截屏
     *
     * @return
     */
    public Bitmap getScreen() {
        Bitmap bmp = Bitmap.createBitmap(webView.getWidth(), 1, Bitmap.Config.ARGB_8888);
        int rowBytes = bmp.getRowBytes();
        bmp = null;

        if (rowBytes * webView.getHeight() >= getAvailMemory()) {
            return null;
        }
        bmp = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        webView.draw(canvas);
        return bmp;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        webView.setVisibility(View.GONE);
        this.removeView(webView);
        webView.destroy();
        webView = null;
    }

    private long getAvailMemory() {// 获取android当前可用内存大小
        return Runtime.getRuntime().maxMemory();
    }

}
