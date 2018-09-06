package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.FlipAdapter;
import home.smart.fly.animations.customview.flipview.FlipView;
import home.smart.fly.animations.customview.flipview.OverFlipMode;
import home.smart.fly.animations.utils.Tools;

public class FlipViewActivity extends AppCompatActivity implements FlipAdapter.Callback,
        FlipView.OnFlipListener, FlipView.OnOverFlipListener {

    @BindView(R.id.flip_view)
    FlipView mFlipView;
    @BindView(R.id.empty_view)
    TextView mEmptyView;

    private FlipAdapter mAdapter;

    private List<String> pics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_view);
        ButterKnife.bind(this);

        String json = Tools.readStrFromAssets("pics.json", this);
        Gson gson = new Gson();
        pics = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());


        mFlipView = (FlipView) findViewById(R.id.flip_view);
        mAdapter = new FlipAdapter(this,pics);
        mAdapter.setCallback(this);
        mFlipView.setAdapter(mAdapter);
        mFlipView.setOnFlipListener(this);
        mFlipView.peakNext(false);
        mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mFlipView.setEmptyView(findViewById(R.id.empty_view));
        mFlipView.setOnOverFlipListener(this);
    }

    @Override
    public void onPageRequested(int page) {
        mFlipView.smoothFlipTo(page);
    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
        Log.e("pageflip", "Page: " + position);
        if (position > mFlipView.getPageCount() - 3 && mFlipView.getPageCount() < 30) {
            mAdapter.addItems(5);
        }
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode,
                           boolean overFlippingPrevious, float overFlipDistance,
                           float flipDistancePerPage) {
        Log.e("overflip", "overFlipDistance = " + overFlipDistance);
    }
}
