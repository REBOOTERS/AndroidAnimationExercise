package home.smart.fly.animationdemo.swipeanim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.swipeanim.lib.ItemRemovedListener;
import home.smart.fly.animationdemo.swipeanim.lib.SwipeCardLayoutManager;
import home.smart.fly.animationdemo.swipeanim.lib.SwipeCardRecyclerView;

public class SwipeAnimActivity extends AppCompatActivity {
    private SwipeCardRecyclerView mRecyclerView;
    private SmartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_anim);
        mRecyclerView = (SwipeCardRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new SwipeCardLayoutManager());
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        mAdapter = new SmartAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRemovedListener(new ItemRemovedListener() {
            @Override
            public void onUpRemoved() {
                Toast.makeText(SwipeAnimActivity.this, list.get(list.size() - 1) + " was right removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownRemoved() {
                Toast.makeText(SwipeAnimActivity.this, list.get(list.size() - 1) + " was left removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
