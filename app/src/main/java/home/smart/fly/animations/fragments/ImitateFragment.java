package home.smart.fly.animations.fragments;

import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

import home.smart.fly.animations.R;
import home.smart.fly.animations.activity.ClockViewActivity;
import home.smart.fly.animations.activity.FakeFootballActivity;
import home.smart.fly.animations.activity.FlipViewActivity;
import home.smart.fly.animations.activity.IModeActivity;
import home.smart.fly.animations.activity.LottieAnimationViewActivity;
import home.smart.fly.animations.activity.MatisseDemoActivity;
import home.smart.fly.animations.activity.MySwipeMenuActivity;
import home.smart.fly.animations.activity.PhenixDemoActivity;
import home.smart.fly.animations.activity.PhotoBrowse;
import home.smart.fly.animations.activity.PolygonViewActivity;
import home.smart.fly.animations.activity.PullRecyclerViewActivity;
import home.smart.fly.animations.activity.SwipeFinishActivity;
import home.smart.fly.animations.activity.ViewsActivity;
import home.smart.fly.animations.activity.jianshu.FakeJianShuActivity;
import home.smart.fly.animations.activity.jianshu.JianShuHeadActivity;
import home.smart.fly.animations.customview.swipeanim.FakeWeiBoActivity;
import home.smart.fly.animations.customview.wheel.WheelViewActivity;
import home.smart.fly.animations.fragments.base.BaseFragment;
import home.smart.fly.animations.fragments.base.ItemInfo;
import home.smart.fly.animations.fragments.base.RoutePaths;

@Route(path = RoutePaths.IMITATE)
public class ImitateFragment extends BaseFragment {


    @Override
    public void InitView() {
        demos.add(new ItemInfo(R.string.fake_football, FakeFootballActivity.class));
        demos.add(new ItemInfo(R.string.app_name, ViewsActivity.class));
        demos.add(new ItemInfo(R.string.lottie_anim, LottieAnimationViewActivity.class));
        demos.add(new ItemInfo(R.string.jianshu, FakeJianShuActivity.class));
        demos.add(new ItemInfo(R.string.clockView, ClockViewActivity.class));
        demos.add(new ItemInfo(R.string.wheelView, WheelViewActivity.class));
        demos.add(new ItemInfo(R.string.polygonView, PolygonViewActivity.class));
        demos.add(new ItemInfo(R.string.imode, IModeActivity.class));
        demos.add(new ItemInfo(R.string.jianshuhead, JianShuHeadActivity.class));
        demos.add(new ItemInfo(R.string.fake_weibo, FakeWeiBoActivity.class));
        demos.add(new ItemInfo(R.string.swipemenu, MySwipeMenuActivity.class));
        demos.add(new ItemInfo(R.string.pullzoom, PullRecyclerViewActivity.class));
        demos.add(new ItemInfo(R.string.flipView, FlipViewActivity.class));
        demos.add(new ItemInfo(R.string.swipeFinish, SwipeFinishActivity.class));
        demos.add(new ItemInfo(R.string.Matisse, MatisseDemoActivity.class));
        demos.add(new ItemInfo(R.string.PreviewOne, PhotoBrowse.class));
        demos.add(new ItemInfo(R.string.PreviewTwo, PhenixDemoActivity.class));

        Toast.makeText(mContext, getResources().getString(R.string.toast), Toast.LENGTH_SHORT).show();
    }



}
