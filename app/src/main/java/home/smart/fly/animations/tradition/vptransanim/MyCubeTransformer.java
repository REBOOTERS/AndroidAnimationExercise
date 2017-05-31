package home.smart.fly.animations.tradition.vptransanim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by co-mall on 2017/5/31.
 */

public class MyCubeTransformer implements ViewPager.PageTransformer {
    private int maxRotation = 90;

    public MyCubeTransformer() {
    }

    public MyCubeTransformer(int maxRotation) {
        this.maxRotation = maxRotation;
    }

    @Override
    public void transformPage(View page, float position) {
        if (position < -1.0f) {
            page.setPivotX(page.getMeasuredWidth());
            page.setPivotY(page.getMeasuredHeight());
            page.setRotationY(0);
        } else if (position <= 0.0f) {
            page.setPivotX(page.getMeasuredWidth());
            page.setPivotY(page.getMeasuredHeight() / 2);
            page.setRotationY(position * maxRotation);
        } else if (position <= 1.0f) {
            page.setPivotX(0);
            page.setPivotY(page.getMeasuredHeight() / 2);
            page.setRotationY(position * maxRotation);
        } else {
            page.setPivotX(page.getMeasuredWidth());
            page.setPivotY(page.getMeasuredHeight());
            page.setRotationY(0);
        }
    }
}
