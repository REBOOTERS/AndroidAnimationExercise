package home.smart.fly.animations.bga;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import home.smart.fly.animations.R;
import home.smart.fly.animations.adapter.SchoolBeanShell;
import home.smart.fly.animations.utils.T;
import home.smart.fly.animations.utils.Tools;


public class BgaAllActivity extends AppCompatActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate,
        BGABanner.Adapter<ImageView, String>,
        BGABanner.Delegate<ImageView, String> {
    private Context mContext;
    private BGABanner mBGABanner;
    private List<String> locations = new ArrayList<>();
    private ContentAdapter mContentAdapter;

    private BGARefreshLayout mBGARefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_bga_all);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mBGARefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        mBGARefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder mRefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        mBGARefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mContentAdapter = new ContentAdapter(mRecyclerView);
        mContentAdapter.addHeaderView(getHeadView());
        mRecyclerView.setAdapter(mContentAdapter.getHeaderAndFooterAdapter());
        initData();
    }

    private void initData() {
        String json = Tools.readStrFromAssets("school.json", mContext);
        Gson mGson = new Gson();
        List<SchoolBeanShell> mBeanShells = mGson.fromJson(json, new TypeToken<ArrayList<SchoolBeanShell>>() {
        }.getType());

        for (int i = 0; i < mBeanShells.size(); i++) {
            locations.add(mBeanShells.get(i).getName());
        }
        mContentAdapter.setData(locations);

        json = Tools.readStrFromAssets("pics.json", mContext);
        List<String> pics = mGson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        mBGABanner.setData(pics.subList(0, 4), locations.subList(0, 4));

    }

    private View getHeadView() {
        View headView = View.inflate(mContext, R.layout.bag_banner_layout, null);
        mBGABanner = (BGABanner) headView.findViewById(R.id.banner);
        mBGABanner.setAdapter(this);
        mBGABanner.setDelegate(this);
        mBGABanner.setTransitionEffect(TransitionEffect.Default);
        return headView;
    }


    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
        Glide.with(mContext)
                .load(model)
                .into(itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
        T.showSToast(mContext, "you click " + (position + 1));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_banner_style, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TransitionEffect mTransitionEffect = TransitionEffect.Default;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.alpha:
                mTransitionEffect = TransitionEffect.Alpha;
                break;
            case R.id.rotate:
                mTransitionEffect = TransitionEffect.Rotate;
                break;
            case R.id.cube:
                mTransitionEffect = TransitionEffect.Cube;
                break;
            case R.id.flip:
                mTransitionEffect = TransitionEffect.Flip;
                break;
            case R.id.accordion:
                mTransitionEffect = TransitionEffect.Accordion;
                break;
            case R.id.zoomFade:
                mTransitionEffect = TransitionEffect.ZoomFade;
                break;
            case R.id.fade:
                mTransitionEffect = TransitionEffect.Fade;
                break;
            case R.id.stack:
                mTransitionEffect = TransitionEffect.Stack;
                break;
            default:
                mTransitionEffect = TransitionEffect.Default;
                break;
        }
        mBGABanner.setTransitionEffect(mTransitionEffect);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBGARefreshLayout.endRefreshing();
            }
        }, 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBGARefreshLayout.endLoadingMore();
            }
        }, 1000);
        return true;
    }
}
