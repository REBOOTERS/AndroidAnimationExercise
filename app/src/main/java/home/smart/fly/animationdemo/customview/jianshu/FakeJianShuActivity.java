package home.smart.fly.animationdemo.customview.jianshu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.jianshu.helper.Article;

public class FakeJianShuActivity extends AppCompatActivity {
    private static final String TAG = "FakeJianShuActivity";
    private static final String URL = "http://www.jianshu.com/p/869fbab4b006";
    //    private static final String URL = "http://www.jianshu.com/p/8cc7b39e0e7d";
    private WebView mWebView;

    private String text,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_image);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mWebView = (WebView) findViewById(R.id.webView);
        new MyAsyncTask().execute(URL);

    }


    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            //文档对象，用来接收html页面
            String result = "";
            try {
                //获取指定网址的页面内容
                Document document = Jsoup.connect(URL).timeout(50000).get();
                Elements div = document.getElementsByClass("show-content");
                Element element = div.get(0);
                Elements imgs=element.getElementsByTag("img");
                for (Element ele_img : imgs) {
                    ele_img.attr("style","max-width:100%;height:auto;");
                }
                result = div.toString();
//                Log.e(TAG, "test: " + div.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            text=s;
            title = "测试";
            mWebView.loadDataWithBaseURL("", s, "text/html", "UTF-8", "");
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
                Intent intent = new Intent(FakeJianShuActivity.this, Activity_Capture.class);
                Article article=new Article(text,title);
                intent.putExtra("data", article);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
