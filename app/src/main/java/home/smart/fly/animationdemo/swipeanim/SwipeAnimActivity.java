package home.smart.fly.animationdemo.swipeanim;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.swipeanim.lib.ItemTouchListener;
import home.smart.fly.animationdemo.swipeanim.lib.SwipeCardLayoutManager;
import home.smart.fly.animationdemo.swipeanim.lib.SwipeCardRecyclerView;
import home.smart.fly.animationdemo.utils.DpConvert;

public class SwipeAnimActivity extends AppCompatActivity {
    private Context mContext;
    private SwipeCardRecyclerView mRecyclerView;
    private SmartAdapter mAdapter;

    private static final String TAG = "SwipeAnimActivity";

    private float mBorder;

    //datas
    private List<String> list;

    private TextView upText;
    private TextView downText;
    private RelativeLayout head;
    private RelativeLayout bottom;


    private ImageView upImg;
    private ImageView downImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_swipe_anim);
        InitDatas();
        InitViews();
    }

    private void InitViews() {
        upImg = (ImageView) findViewById(R.id.upImg);
        upText = (TextView) findViewById(R.id.upText);
        downText = (TextView) findViewById(R.id.downText);
        downImg = (ImageView) findViewById(R.id.downImg);
        head = (RelativeLayout) findViewById(R.id.head);
        bottom = (RelativeLayout) findViewById(R.id.bottom);

        mRecyclerView = (SwipeCardRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new SwipeCardLayoutManager());
        mAdapter = new SmartAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRemovedListener(new ItemTouchListener() {
            @Override
            public void onUpRemoved() {
                Toast.makeText(SwipeAnimActivity.this, list.get(list.size() - 1) + " was up removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownRemoved() {
                Toast.makeText(SwipeAnimActivity.this, list.get(list.size() - 1) + " was down removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void hide(int dy) {
                hideViews();
                if (dy > 0) {
                    Log.e(TAG, "hide---down " + dy);
                    upImg.setVisibility(View.VISIBLE);
                    float alpha = dy / mBorder - 0.1f;
                    upImg.setAlpha(alpha);
                    upImg.setY(dy - upImg.getHeight() / 2);


                } else {
                    Log.e(TAG, "hide---up " + dy);
                    downImg.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void show() {
                Log.e(TAG, "show");
                showViews();
            }
        });
    }

    private void showViews() {
        head.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);
        upText.setVisibility(View.VISIBLE);
        downText.setVisibility(View.VISIBLE);
        upImg.setVisibility(View.GONE);
        downImg.setVisibility(View.GONE);

    }

    private void hideViews() {

        head.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
        downText.setVisibility(View.GONE);
        upText.setVisibility(View.GONE);

    }

    private void InitDatas() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }

        mBorder = DpConvert.dip2px(mContext, 120);
        Log.e(TAG, "InitDatas: " + mBorder);
    }
}
