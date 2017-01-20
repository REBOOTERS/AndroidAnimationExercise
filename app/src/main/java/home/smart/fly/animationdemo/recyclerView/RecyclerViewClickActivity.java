package home.smart.fly.animationdemo.recyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animationdemo.R;


public class RecyclerViewClickActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private Context mContext;
    private List<String> datas;
    private MyAdapter adapter;

    private SwipeRefreshLayout mSwipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_click);
        InitView();
    }


    private void InitView() {
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(this);
        datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("this is data " + i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        adapter = new MyAdapter(datas);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                datas.clear();
                for (int i = 0; i < 10; i++) {
                    datas.add("This is Data " + i * i);
                }
                adapter.notifyDataSetChanged();
                mSwipe.setRefreshing(false);
            }
        }, 1500);
    }
}
