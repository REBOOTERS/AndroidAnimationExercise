package home.smart.fly.animations.webview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import home.smart.fly.animations.R;

/**
 * Created by rookie on 2017/2/13.
 */

public class JsObject {
    private Context mContext;

    public JsObject(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Exit the application ?");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Activity activity = (Activity) mContext;
                activity.finish();
//                System.exit(0);
            }
        });
        builder.setTitle("Exit App");
        builder.setIcon(R.drawable.radar_card_guide_hot);
        builder.setCancelable(false);
        builder.show();
    }
}
