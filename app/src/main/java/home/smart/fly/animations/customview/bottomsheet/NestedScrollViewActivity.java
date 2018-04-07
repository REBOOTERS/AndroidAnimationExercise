package home.smart.fly.animations.customview.bottomsheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * Created by cjj on 2016/2/25.
 */
public class NestedScrollViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ns);

        // The View with the BottomSheetBehavior
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变，根据slideOffset可以做一些动画
                Log.i("cjj", "newState--->" + newState);
//                ViewCompat.setScaleX(bottomSheet,1);
//                ViewCompat.setScaleY(bottomSheet,1);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
                Log.i("cjj", "slideOffset=====》" + slideOffset);
//                ViewCompat.setScaleX(bottomSheet,slideOffset);
//                ViewCompat.setScaleY(bottomSheet,slideOffset);
            }
        });

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

    }

}
