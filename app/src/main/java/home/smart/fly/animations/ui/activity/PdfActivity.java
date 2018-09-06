package home.smart.fly.animations.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.smart.fly.animations.R;


public class PdfActivity extends AppCompatActivity {

    @BindView(R.id.printHelper)
    Button mPrintHelper;
    @BindView(R.id.genPdf)
    Button mGenPdf;
    @BindView(R.id.webView)
    WebView mWebView;

    private static final String WEB_URL = "http://www.jianshu.com/p/2acac034453b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl(WEB_URL);
    }

    @OnClick({R.id.printHelper, R.id.genPdf})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.printHelper:
                PrintHelper mPrintHelper = new PrintHelper(this);
                mPrintHelper.setColorMode(PrintHelper.SCALE_MODE_FIT);
                Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a5);
                mPrintHelper.printBitmap("print-bitmap", mBitmap);
                break;
            case R.id.genPdf:
                genPdfFile();
                break;
            default:
                break;
        }
    }

    private void genPdfFile() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = mWebView.createPrintDocumentAdapter("my.pdf");
        } else {
            printAdapter = mWebView.createPrintDocumentAdapter();
        }

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        printManager.print(jobName, printAdapter, attributes);


    }
}
