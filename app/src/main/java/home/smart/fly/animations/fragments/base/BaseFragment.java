package home.smart.fly.animations.fragments.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.internal.annotations.Cat;
import home.smart.fly.animations.ui.activity.FullscreenADActivity;
import home.smart.fly.animations.utils.V;

/**
 * @version V1.0
 * @author: Rookie
 * @date: 2018-08-20 10:57
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = "BaseFragment";
    private static final int STEP_OF_EVERY_AD = 6;
    private static final int AD_COUNT = 5;

    protected Context mContext;

    protected List<ItemInfo> demos = new ArrayList<>();

    @Cat
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_custom_views, null);
        InitView();
        AddADItem();
        RecyclerView recyclerView = V.f(rootView, R.id.recyclerView);
        MyAdapter mAdapter = new MyAdapter(demos);
        mAdapter.setAmazingBackground(getBackgroundResId());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fPos = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lPos = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                for (int i = fPos; i <= lPos; i++) {
                    View view = mLinearLayoutManager.findViewByPosition(i);
                    AdImageView adImageView = view.findViewById(R.id.ad_view);
                    if (adImageView != null && adImageView.getVisibility() == View.VISIBLE) {
                        int value = mLinearLayoutManager.getHeight() - view.getTop();
                        adImageView.setDx(value);
                    }
                }
            }
        });
        if (MMKV.defaultMMKV().getBoolean("running",false)) {
            setupSkeleton(recyclerView, mAdapter);
            MMKV.defaultMMKV().putBoolean("running", false);

        }


//        recyclerView.setBackgroundColor(PaletteUtils.getMagicColor(getResources(),getBackgroundResId()));
        Log.e(TAG, "onCreateView: ");
        return rootView;
    }

    private void setupSkeleton(RecyclerView recyclerView, MyAdapter mAdapter) {
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(mAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(500)
                .count(10)
                .load(R.layout.demo_info_skeleton_item)
                .show(); //default count is 10
        recyclerView.postDelayed(skeletonScreen::hide, 1000);
    }

    @Cat
    protected void AddADItem() {
        for (int i = 1; i < AD_COUNT; i++) {
            if (demos.size() > STEP_OF_EVERY_AD * i) {
                demos.add(STEP_OF_EVERY_AD * i, new ItemInfo(R.string.app_name, FullscreenADActivity.class));
            } else {
                break;
            }
        }
    }

    public @DrawableRes
    int getBackgroundResId() {
        return R.drawable.girl;
    }

    protected abstract void InitView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }
}
