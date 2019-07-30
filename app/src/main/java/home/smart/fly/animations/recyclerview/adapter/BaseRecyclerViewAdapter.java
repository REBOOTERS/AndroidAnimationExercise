package home.smart.fly.animations.recyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author rookie
 * @since 07-24-2019
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> datas;

    BaseRecyclerViewAdapter(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
