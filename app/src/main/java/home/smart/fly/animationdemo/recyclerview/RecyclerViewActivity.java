package home.smart.fly.animationdemo.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animationdemo.R;

public class RecyclerViewActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView mRecyclerView;

    private List<String> datas = new ArrayList<>();
    private RecyclerViewAdapter mAdpater;

    private View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_recycler_view);
        InitDatas();
        InitView();
    }

    private void InitDatas() {
        for (int i = 0; i < 50; i++) {
            datas.add("This is item " + i);
        }


    }

    private void InitView() {
        header = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_header, null);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdpater = new RecyclerViewAdapter(datas);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdpater);
    }
}
