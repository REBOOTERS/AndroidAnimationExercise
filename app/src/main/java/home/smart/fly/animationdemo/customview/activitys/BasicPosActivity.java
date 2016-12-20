package home.smart.fly.animationdemo.customview.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.BaseActivity;
import home.smart.fly.animationdemo.utils.V;

/**
 * Created by rookie on 2016/12/20.
 */

public class BasicPosActivity extends BaseActivity {
    private Context mContext;
    private TextView posTv;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_pos);
    }

    @Override
    public void initView() {
        posTv = V.f(this, R.id.posTv);
        linearLayout = V.f(this, R.id.linearLayout);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            String pos = "getLeft(): " + linearLayout.getLeft() + "\n"
                    + "getTop(): " + linearLayout.getTop() + "\n"
                    + "getRight(): " + linearLayout.getRight() + "\n"
                    + "getBottom(): " + linearLayout.getBottom() + "\n"
                    + "getX(): " + linearLayout.getX() + "\n"
                    + "getY(): " + linearLayout.getY() + "\n"
                    + "getTranslationX(): " + linearLayout.getTranslationX() + "\n"
                    + "getTranslationY(): " + linearLayout.getTranslationY() + "\n"
                    + "getWidth(): " + linearLayout.getWidth() + "\n"
                    + "getHeight(): " + linearLayout.getHeight() + "\n";


            posTv.setText(pos);
        }
    }
}
