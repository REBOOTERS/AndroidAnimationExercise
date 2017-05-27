package home.smart.fly.animations.recyclerview.linearlayoutmanager;

import android.support.v7.widget.RecyclerView;

/**
 * Created by co-mall on 2017/5/27.
 */

public class MyLinearLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
}
