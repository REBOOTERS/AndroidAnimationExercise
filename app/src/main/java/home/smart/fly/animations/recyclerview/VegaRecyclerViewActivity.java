package home.smart.fly.animations.recyclerview;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.recyclerview.adapter.RecyclerViewAdapter;
import home.smart.fly.animations.recyclerview.customlayoutmanager.VegaLayoutManager;
import home.smart.fly.animations.utils.Tools;

public class VegaRecyclerViewActivity extends AppCompatActivity {

    private Context mContext;
    private List<String> pics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vega_recycler_view);

        mContext = this;
        initData();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(pics);
        mRecyclerView.setLayoutManager(new VegaLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        String json = Tools.readStrFromAssets("pics.json", mContext);
        Gson gson = new Gson();
        pics = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }
}
