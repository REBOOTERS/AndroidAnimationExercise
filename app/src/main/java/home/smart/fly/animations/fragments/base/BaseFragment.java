package home.smart.fly.animations.fragments.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.V;

/**
 * @version V1.0
 * @author: Rookie
 * @date: 2018-08-20 10:57
 */
public abstract class BaseFragment extends Fragment {
    protected  final String TAG = "BaseFragment";

    protected Context mContext;

    protected List<ItemInfo> demos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_custom_views, null);
        InitView();
        RecyclerView recyclerView = V.f(rootView, R.id.recyclerView);
        MyAdapter mAdapter = new MyAdapter(demos);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    protected abstract void InitView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
