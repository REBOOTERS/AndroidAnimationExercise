package home.smart.fly.animations.recyclerview.customlayoutmanager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by rookie on 2017/5/27.
 */

public class MyLinearLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
}
