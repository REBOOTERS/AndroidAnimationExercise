package home.smart.fly.animations.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.widget.PullZoomView;
import com.lzy.widget.manager.ExpandLinearLayoutManager;

import java.util.ArrayList;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.ColorUtil;

public class PullRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_recyclerview);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new ExpandLinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());

        Intent intent = getIntent();
        float sensitive = intent.getFloatExtra("sensitive", 1.5f);
        int zoomTime = intent.getIntExtra("zoomTime", 500);
        boolean isParallax = intent.getBooleanExtra("isParallax", true);
        boolean isZoomEnable = intent.getBooleanExtra("isZoomEnable", true);
        PullZoomView pzv = (PullZoomView) findViewById(R.id.pzv);
        pzv.setIsParallax(isParallax);
        pzv.setIsZoomEnable(isZoomEnable);
        pzv.setSensitive(sensitive);
        pzv.setZoomTime(zoomTime);
        pzv.setOnScrollListener(new PullZoomView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                Log.e("TAG","onScroll   t:" + t + "  oldt:" + oldt);
            }

            @Override
            public void onHeaderScroll(int currentY, int maxY) {
                Log.e("TAG","onHeaderScroll   currentY:" + currentY + "  maxY:" + maxY);
            }

            @Override
            public void onContentScroll(int l, int t, int oldl, int oldt) {
                Log.e("TAG","onContentScroll   t:" + t + "  oldt:" + oldt);
            }
        });
        pzv.setOnPullZoomListener(new PullZoomView.OnPullZoomListener() {
            @Override
            public void onPullZoom(int originHeight, int currentHeight) {
                Log.e("TAG","onPullZoom  originHeight:" + originHeight + "  currentHeight:" + currentHeight);
            }

            @Override
            public void onZoomFinish() {
                Log.e("TAG","onZoomFinish");
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.SimpleViewHolder> {

        private ArrayList<String> strings;

        public MyAdapter() {
            strings = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                strings.add("条目：" + i);
            }
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SimpleViewHolder(View.inflate(getApplicationContext(), android.R.layout.simple_list_item_1, null));
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        protected class SimpleViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }

            public void bindData(int position) {
                textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                textView.setText(strings.get(position));
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(ColorUtil.generateBeautifulColor());
            }
        }
    }
}
