package home.smart.fly.animations.customview.views.pulltorefreshview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by co-mall on 2017/4/26.
 */

public abstract class BaseHeaderAdapter {
    protected LayoutInflater mInflater;

    public BaseHeaderAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public abstract View getHeaderView();

    public abstract void pullViewToRefresh();

    public abstract void releaseViewToRefresh();

    public abstract void headerRefreshing();

    public abstract void headerRefreshComplete();


}
