package home.smart.fly.animations.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import java8.util.Optional;

public class LaunchOtherAppActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.webView)
    WebView mWebView;
    private static final String LOCAL_URL = "file:///android_asset/launch_other_app.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_other_app);
        ButterKnife.bind(this);
        mWebView.loadUrl(LOCAL_URL);

        findViewById(R.id.url1).setOnClickListener(this);
        findViewById(R.id.url2).setOnClickListener(this);
        findViewById(R.id.url3).setOnClickListener(this);
        findViewById(R.id.url4).setOnClickListener(this);
        findViewById(R.id.url5).setOnClickListener(this);
        findViewById(R.id.custom_scheme_and_host).setOnClickListener(this);
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
    public void onClick(View v) {
        TextView textView = findViewById(v.getId());
        String result = textView.getText().toString().trim();

        Uri uri = Uri.parse(result);

        StringBuffer sb = new StringBuffer();
        sb.append("uri==").append(uri).append("\n");
        sb.append("scheme==").append(uri.getScheme()).append("\n");
        sb.append("host==").append(uri.getHost()).append("\n");
        sb.append("path==").append(uri.getPath()).append("\n");
        sb.append("pathSegment==").append(Arrays.toString(uri.getPathSegments().toArray())).append("\n");
        sb.append("query==").append(uri.getQuery()).append("\n");

        if (uri.isHierarchical()) {
            Optional.ofNullable(uri.getQueryParameterNames())
                    .ifPresent(strings -> sb.append("QueryParameterNames==").append(strings.toString()).append("\n"));
        }


        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();

        if (v.getId() == R.id.custom_scheme_and_host) {
            Intent intent = new Intent();
            intent.setAction("my_action");
            intent.setData(Uri.parse(result));
            ActivityInfo activityInfo = intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY);
            Optional.ofNullable(activityInfo)
                    .ifPresent( activityInfo1 -> {
                        startActivity(intent);});
        }

    }
}
