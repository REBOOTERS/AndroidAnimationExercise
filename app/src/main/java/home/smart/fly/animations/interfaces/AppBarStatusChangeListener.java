package home.smart.fly.animations.interfaces;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by rookie on 2017/6/5.
 */

public abstract class AppBarStatusChangeListener implements AppBarLayout.OnOffsetChangedListener {
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    public abstract void onStatusChanged(AppBarLayout appBarLayout, int status);
}
