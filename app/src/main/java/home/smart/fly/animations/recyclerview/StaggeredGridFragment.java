package home.smart.fly.animations.recyclerview;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.adapter.StaggeredRecyclerViewAdapter;
import home.smart.fly.animations.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class StaggeredGridFragment extends Fragment {
    private Context mContext;
    private View mView;

    private List<String> pics = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        initData();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_grid_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.gridView);
        StaggeredRecyclerViewAdapter mAdapter = new StaggeredRecyclerViewAdapter(pics);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mView;
    }

    private void initData() {
        String json = Tools.readStrFromAssets("pics.json", mContext);
        Gson gson=new Gson();
        pics=gson.fromJson(json,new TypeToken<List<String>>(){}.getType());
    }

}
