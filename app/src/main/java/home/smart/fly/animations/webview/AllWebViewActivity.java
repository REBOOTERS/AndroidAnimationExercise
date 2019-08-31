package home.smart.fly.animations.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.WebViewMenuActivity;
import home.smart.fly.animations.ui.activity.jianshu.helper.Constant;
import home.smart.fly.animations.utils.FileUtil;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.TT;
import home.smart.fly.animations.utils.Tools;
import home.smart.fly.animations.utils.V;

public class AllWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AllWebViewActivity";

    private static final String WEB_URL = "https://www.baidu.com";
    private static final String PDF_URL = "http://ei-test.51fapiao.cn:9080/FPFX/actions/dd05d5e72d35f0dac23f6362f05f85cb834110";
    private static final String ERROR_URL = "https://www.badu.com";
    private static final String TWXQ = "file:///android_asset/twxq_1.html";
    private static final String JIANSHU = "file:///android_asset/a.html";
    private static final String LOCAL_URL = "file:///android_asset/index.html";
    private static final String ALI_PAY_URL = "file:///android_asset/launch_alipay_app.html";
    private static final String THREE_D_URL = "file:///android_asset/keyframe.html";
    private static final String WEIXIN_PAY_URL = "http://wechat.66card.com/vcweixin/common/toTestH5Weixin?company=c4p ";

    private Context mContext;
    private WebView mWebView;
    private Button mButton;

    private LinearLayout tools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_all_web_view);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.cpb_green), 0);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("WebView");
        mWebView = V.f(this, R.id.webview);
        mButton = V.f(this, R.id.button);
        mButton.setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);


        setUpWebView();
        loadData();
    }

    private void setUpWebView() {
        tools = (LinearLayout) findViewById(R.id.tools);
        tools.setVisibility(View.VISIBLE);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false);
        //
        mWebView.addJavascriptInterface(new JsObject(mContext), "myObj");
        mWebView.addJavascriptInterface(new LoadHtmlObject(), "myHtml");
        //

        mWebView.setWebViewClient(new MyWebViewClient(mContext));
        mWebView.setWebChromeClient(new MyWebChromeClient());
        WebView.setWebContentsDebuggingEnabled(true);
    }

    private void loadData() {
        mWebView.loadUrl(LOCAL_URL);
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men_web_load_url, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //<editor-fold desc="OptionMenu Item Click Event">
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }

                break;
            case R.id.local:
                mWebView.loadUrl(LOCAL_URL);
                tools.setVisibility(View.VISIBLE);
                break;
            case R.id.jianshu:
                mWebView.loadUrl(Constant.URL);
                tools.setVisibility(View.GONE);
                break;
            case R.id.jianshu_local:
//                mWebView.loadUrl(JIANSHU);
                String text = Tools.readStrFromAssets("a.html",this);

                mWebView.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");

                tools.setVisibility(View.GONE);
                break;
            case R.id.twxq:
                mWebView.loadUrl(TWXQ);
                tools.setVisibility(View.GONE);
                break;
            case R.id.pdf:
                mWebView.loadUrl(PDF_URL);
                tools.setVisibility(View.VISIBLE);
                break;
            case R.id.net:
                mWebView.loadUrl(WEB_URL);
                tools.setVisibility(View.VISIBLE);
                break;
            case R.id.error:
                mWebView.loadUrl(ERROR_URL);
                tools.setVisibility(View.GONE);
                break;
            case R.id.weixinpay:
                mWebView.loadUrl(WEIXIN_PAY_URL);
                tools.setVisibility(View.GONE);
                break;
            case R.id.alipay:
                mWebView.loadUrl(ALI_PAY_URL);
                tools.setVisibility(View.GONE);
                break;
            case R.id.nested:
                Intent mIntent = new Intent(mContext, NestedWebViewActivity.class);
                startActivity(mIntent);
                break;
            case R.id.menu:
                startActivity(new Intent(mContext, WebViewMenuActivity.class));
                break;
            case R.id.galaxy:
                startActivity(new Intent(mContext, FullscreenPage.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //</editor-fold>

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String time = Tools.getCurrentTime();
                String version = Tools.getAppVersion(mContext);
                String name = Tools.getName(mContext);
                String currentUrl = mWebView.getUrl();
                String info = "Application Info: \n\n version: " + version
                        + "\n name: " + name
                        + "\n curTime: " + time
                        + "\n curUrl: " + currentUrl;
                mWebView.loadUrl("javascript:showAlert('" + info + "')");
                break;
            case R.id.save:
                Picture snapShot = mWebView.capturePicture();
                Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                snapShot.draw(canvas);
                if (!TextUtils.isEmpty(FileUtil.savaBitmap2SDcard(mContext, bmp, "1111"))) {
                    TT.showSToast(mContext, "success");
                }
                break;
            default:
                break;
        }
    }
}
