package home.smart.fly.animationdemo.customview.genimage.helper;

import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;


public class WebViewHelper {
    private List<String> mlistPath = new ArrayList<>();
    private static WebViewHelper webViewHelper;
    private WebViewHelper(){}
    private String title;

    public static WebViewHelper getInstance(){
        if (webViewHelper == null){
            webViewHelper = new WebViewHelper();
        }
        return webViewHelper;
    }

    public String getTitle(){
        return title;
    }
    public void clear(){
        mlistPath.clear();
        mlistPath = null;
        webViewHelper = null;
    }

    public List<String> getAllListPath(){
        return mlistPath;
    }

    public void setUpWebView(WebView mWebView,OnGetDataListener onGetDataListener){
        WebSettings mSettings = mWebView.getSettings();
        mSettings.setJavaScriptEnabled(true);//开启javascript
        mSettings.setDomStorageEnabled(true);//开启DOM
        mSettings.setDefaultTextEncodingName("utf-8");//设置字符编码
        //设置web页面
        mSettings.setUseWideViewPort(true);// 调整到适合webview大小
        mSettings.setLoadWithOverviewMode(true);// 调整到适合webview大小
        mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mSettings.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new WebAppInterface(onGetDataListener), "JSInterface");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onLoadResource(WebView view, String url) {
                if(url.toLowerCase().contains(".jpg")
                        ||url.toLowerCase().contains(".png")
                        ||url.toLowerCase().contains(".gif")){
                    if (mlistPath == null)
                        mlistPath = new ArrayList<String>();
                    mlistPath.add(url);
                }
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                title = view.getTitle();
            }
        });

    }

    public void getSelectedData(WebView webView) {
//        String js = "(function getSelectedText() {" +
//                "var txt;" +
//                "if (window.getSelection) {" +
//                "txt = window.getSelection().toString();" +
//                "} else if (window.document.getSelection) {" +
//                "txt = window.document.getSelection().toString();" +
//                "} else if (window.document.selection) {" +
//                "txt = window.document.selection.createRange().text;" +
//                "}" +
//                "JSInterface.getText(txt);" +
//                "})()";

        String js = "(function getSelectedText() {" +
                "var txt;" +
                "if (window.getSelection) {" +
                "var range=window.getSelection().getRangeAt(0);" +
                "var container = window.document.createElement('div');" +
                "container.appendChild(range.cloneContents());" +
                "txt = container.innerHTML;" +
                "} else if (window.document.getSelection) {" +
                "var range=window.getSelection().getRangeAt(0);" +
                "var container = window.document.createElement('div');" +
                "container.appendChild(range.cloneContents());" +
                "txt = container.innerHTML;" +
                "} else if (window.document.selection) {" +
                "txt = window.document.selection.createRange().htmlText;" +
                "}" +
                "JSInterface.getText(txt);" +
                "})()";

        // calling the js function
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:" + js, null);
        } else {
            webView.loadUrl("javascript:" + js);
        }
        webView.clearFocus();
    }

   static class WebAppInterface {
        OnGetDataListener onGetDataListener;
        WebAppInterface(OnGetDataListener onGetDataListener) {
            this.onGetDataListener = onGetDataListener;
        }
        @JavascriptInterface
        public void getText(String text) {
            onGetDataListener.getDataListener(text);
        }
    }
    public interface OnGetDataListener{
        void getDataListener(String text);
    }

}
