package home.smart.fly.animations.fragments;

import com.alibaba.android.arouter.facade.annotation.Route;

import home.smart.fly.animations.R;
import home.smart.fly.animations.ui.activity.PullToScaleActivity;
import home.smart.fly.animations.ui.activity.ScrollingActivity;
import home.smart.fly.animations.ui.activity.BitmapMeshActivity;
import home.smart.fly.animations.ui.activity.CollegeActivity;
import home.smart.fly.animations.ui.activity.FlowLayoutActivity;
import home.smart.fly.animations.ui.activity.MyDrawingBoardActivity;
import home.smart.fly.animations.ui.activity.PlayActivity;
import home.smart.fly.animations.ui.activity.FakeDoubanMovie;
import home.smart.fly.animations.ui.activity.ViewDragHelperActivity;
import home.smart.fly.animations.ui.activity.ViewTransformActivity;
import home.smart.fly.animations.ui.activity.WavaAnimActivity;
import home.smart.fly.animations.ui.activity.demos.AnimationsDemo;
import home.smart.fly.animations.ui.activity.transtions.StackViewActivity;
import home.smart.fly.animations.ui.activity.transtions.SwitcherActivity;
import home.smart.fly.animations.fragments.base.BaseFragment;
import home.smart.fly.animations.fragments.base.ItemInfo;
import home.smart.fly.animations.fragments.base.RoutePaths;
import home.smart.fly.animations.master.ui.MasterFilterActivity;
import home.smart.fly.animations.master.ui.MasterPaintActivity;


/**
 * Created by rookie on 2016/8/12.
 */
@Route(path = RoutePaths.VIEWS)
public class ViewsFragment extends BaseFragment {

    @Override
    protected void InitView() {
        demos.add(new ItemInfo(R.string.profile, FakeDoubanMovie.class));
        demos.add(new ItemInfo(R.string.app_name, CollegeActivity.class));
        demos.add(new ItemInfo(R.string.view_drag, ViewDragHelperActivity.class));
        demos.add(new ItemInfo(R.string.profile, ScrollingActivity.class));
        demos.add(new ItemInfo(R.string.pull_to_scale, PullToScaleActivity.class));
        demos.add(new ItemInfo(R.string.master, MasterPaintActivity.class));
        demos.add(new ItemInfo(R.string.filter, MasterFilterActivity.class));
        demos.add(new ItemInfo(R.string.view_transform, ViewTransformActivity.class));
        demos.add(new ItemInfo(R.string.bitmap_mesh, BitmapMeshActivity.class));
        demos.add(new ItemInfo(R.string.view_property, PlayActivity.class));
        demos.add(new ItemInfo(R.string.besier, MyDrawingBoardActivity.class));
        demos.add(new ItemInfo(R.string.waveAnim, WavaAnimActivity.class));
        demos.add(new ItemInfo(R.string.app_name, AnimationsDemo.class));
        demos.add(new ItemInfo(R.string.flowlayout, FlowLayoutActivity.class));
        demos.add(new ItemInfo(R.string.switcher, SwitcherActivity.class));
        demos.add(new ItemInfo(R.string.stackView, StackViewActivity.class));
    }

    @Override
    protected int getBackgroundResId() {
        return R.drawable.lufei;
    }
}
