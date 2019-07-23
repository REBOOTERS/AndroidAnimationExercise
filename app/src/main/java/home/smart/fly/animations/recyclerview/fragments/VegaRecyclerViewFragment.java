package home.smart.fly.animations.recyclerview.fragments;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.adapter.SimpleRecyclerViewAdapter;
import org.jetbrains.annotations.NotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class VegaRecyclerViewFragment extends BaseListFragment {


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_vega_recycler_view;
    }

    @NotNull
    @Override
    public RecyclerView.Adapter<?> getCustomAdapter() {
        return new SimpleRecyclerViewAdapter(datas);
    }
}
