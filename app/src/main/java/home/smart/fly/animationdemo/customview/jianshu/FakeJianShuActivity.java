package home.smart.fly.animationdemo.customview.jianshu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.IOException;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.jianshu.helper.Article;
import home.smart.fly.animationdemo.customview.jianshu.helper.WebViewHelper;
import home.smart.fly.animationdemo.utils.Tools;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FakeJianShuActivity extends AppCompatActivity {
    private static final String TAG = "FakeJianShuActivity";
    private static final String URL = "http://www.jianshu.com/p/869fbab4b006";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_image);
        new MyAsyncTask().execute();
//        loadView();
    }

    private void loadView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl(URL);


        WebViewHelper.getInstance().setUpWebView(mWebView, new WebViewHelper.OnGetDataListener() {
            @Override
            public void getDataListener(String text) {
                Tools.saveToSDCard("html.txt", text);
                Intent intent = new Intent(FakeJianShuActivity.this, Activity_Capture.class);
                Article article = new Article(text, TextUtils.isEmpty(WebViewHelper.getInstance().getTitle()) ? "" : "《" + WebViewHelper.getInstance().getTitle() + "》");
                intent.putExtra("data", article);
                startActivity(intent);
            }
        });


    }

    class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        Call mCall;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .url(URL)
                    .method("GET", null)
                    .build();
            mCall = client.newCall(request);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                Response response=mCall.execute();
                result=response.body().string();
                Tools.saveToSDCard("html.txt", result);
//                result = mCall.execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "doInBackground: " + result);
            return result;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jianshu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        WebViewHelper.getInstance().getSelectedData(mWebView);
                    }
                });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
