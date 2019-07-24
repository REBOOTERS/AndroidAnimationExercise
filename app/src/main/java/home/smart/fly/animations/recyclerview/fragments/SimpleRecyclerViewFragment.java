package home.smart.fly.animations.recyclerview.fragments;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.DataFactory;
import home.smart.fly.animations.recyclerview.adapter.SimpleRecyclerViewAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleRecyclerViewFragment extends BaseListFragment<String> {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_simple_recycler_view;
    }


    @NotNull
    @Override
    public RecyclerView.Adapter<?> getCustomAdapter() {
        return new SimpleRecyclerViewAdapter(datas);
    }

    @NotNull
    @Override
    public ArrayList<String> loadDatas() {
        return DataFactory.INSTANCE.initDefaultData(getContext());
    }
}
