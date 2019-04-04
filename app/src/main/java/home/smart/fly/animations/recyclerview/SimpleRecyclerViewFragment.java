package home.smart.fly.animations.recyclerview;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.adapter.RecyclerViewAdapter;
import home.smart.fly.animations.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleRecyclerViewFragment extends Fragment {

    private View rootView;
    private Context mContext;
    private List<String> pics = new ArrayList<>();

    public SimpleRecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        initData();
        rootView = inflater.inflate(R.layout.fragment_simple_recycler_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(pics);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }


    private void initData() {
        String json = Tools.readStrFromAssets("pics.json", mContext);
        Gson gson = new Gson();
        pics = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

}
