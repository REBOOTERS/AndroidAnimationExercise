package home.smart.fly.animationdemo.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animationdemo.R;


public class RecyclerViewClickActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Context mContext;
    private List<String> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_click);
        InitView();
    }

    private void InitView() {
        datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("this is data " + i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext);
        MyAdapter adapter = new MyAdapter(datas);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }


}
