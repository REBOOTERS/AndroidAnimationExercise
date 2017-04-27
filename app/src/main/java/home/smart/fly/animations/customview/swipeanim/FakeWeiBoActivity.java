package home.smart.fly.animations.customview.swipeanim;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.StatusBarUtil;

public class FakeWeiBoActivity extends AppCompatActivity implements View.OnClickListener, BDLocationListener {
    private static final String TAG = "FakeWeiBoActivity";
    private Context mContext;
    //head
    private TextView close;
    private TextView district;
    private TextView name, address, phoneNum;

    //bottom
    private LinearLayout movie, meishi, atm, bus, subway;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private ImageView img1, img2, img3, img4, img5;
    private Resources res;
    private int tabselColor;


    private SmartPullView refreshView;
    private RadarScanView radar;

    //
    private LinearLayout head, bottom;
    private TextView hot, cold;
    //
    private LinearLayout card;
    private FrameLayout backFrame;
    private int[] colors;
    private AnimatorSet cardHideAnim;
    private AnimatorSet cardShowAnim;

    //location
    public LocationClient mLocationClient = null;
    //
    private PoiSearch mPoiSearch;
    private PoiNearbySearchOption mNearbySearchOption;
    private String keyWord;
    private LatLng latLng;
    //
    private List<PoiInfo> poiInfos;
    private int index = 0;

    //permissions
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private String[] permissons = {Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        mContext = this;
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_fake_wei_bo);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.background), 0);
        InitView();
        InitAnim();
        initLocationAndSearch();
        //EventBus
        EventBus.getDefault().register(mContext);
        index = 0;


        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //没有定位权限则请求
            ActivityCompat.requestPermissions(this, permissons, MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            mLocationClient.start();
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        radar.startAnim();
    }

    private void InitView() {
        res = mContext.getResources();
        tabselColor = res.getColor(R.color.tabsel);

        refreshView = (SmartPullView) findViewById(R.id.refreshView);
        refreshView.setOnHeaderRefreshListener(new MyHeadListener());
        refreshView.setOnFooterRefreshListener(new MyFooterListener());
        refreshView.setOnPullListener(new MyPullListener());
        radar = (RadarScanView) findViewById(R.id.radar);
        //
        head = (LinearLayout) findViewById(R.id.head);
        bottom = (LinearLayout) findViewById(R.id.bottom);
        hot = (TextView) findViewById(R.id.hot);
        cold = (TextView) findViewById(R.id.cold);
        close = (TextView) findViewById(R.id.close);
        close.setOnClickListener(this);
        district = (TextView) findViewById(R.id.district);

        card = (LinearLayout) findViewById(R.id.card);
        backFrame = (FrameLayout) findViewById(R.id.frameback);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        //
        movie = (LinearLayout) findViewById(R.id.movie);
        meishi = (LinearLayout) findViewById(R.id.meishi);
        atm = (LinearLayout) findViewById(R.id.atm);
        bus = (LinearLayout) findViewById(R.id.bus);
        subway = (LinearLayout) findViewById(R.id.subway);
        movie.setOnClickListener(this);
        meishi.setOnClickListener(this);
        atm.setOnClickListener(this);
        bus.setOnClickListener(this);
        subway.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        colors = new int[]{0xff0099cc, 0xffff4444, 0xff99cc00};
    }

    private void InitAnim() {
        cardHideAnim = new AnimatorSet();
        ObjectAnimator hideAnim = ObjectAnimator.ofFloat(card, "alpha", 1.0f, 0.0f);
        ObjectAnimator hideAnimX = ObjectAnimator.ofFloat(card, "scaleX", 1.0f, 0f);
        ObjectAnimator hideAnimY = ObjectAnimator.ofFloat(card, "scaleY", 1.0f, 0f);
        cardHideAnim.playTogether(hideAnimX, hideAnimY, hideAnim);
        cardHideAnim.setDuration(300);

        cardShowAnim = new AnimatorSet();
        final ObjectAnimator showAnimX = ObjectAnimator.ofFloat(card, "scaleX", 0f, 1.0f);
        final ObjectAnimator showAnimY = ObjectAnimator.ofFloat(card, "scaleY", 0f, 1.0f);
        final ObjectAnimator showAnim = ObjectAnimator.ofFloat(card, "alpha", 0f, 1.0f);
        cardShowAnim.playTogether(showAnimX, showAnimY, showAnim);
        cardShowAnim.setDuration(500);

        cardShowAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                head.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.VISIBLE);
                hot.setVisibility(View.VISIBLE);
                cold.setVisibility(View.VISIBLE);
            }
        });


    }


    class MyHeadListener implements SmartPullView.OnHeaderRefreshListener {

        @Override
        public void onHeaderRefresh(SmartPullView view) {
            refreshView.onHeaderRefreshComplete();
            index = index + 1;
            cardAnimActions();
        }


    }

    class MyFooterListener implements SmartPullView.OnFooterRefreshListener {

        @Override
        public void onFooterRefresh(SmartPullView view) {
            refreshView.onFooterRefreshComplete();
            index = index + 1;
            cardAnimActions();
        }
    }


    private void cardAnimActions() {

        cardHideAnim.start();
        cardHideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onAnimationEnd: the index is " + index);
                backFrame.setBackgroundColor(colors[index % 3]);
                if (poiInfos != null && poiInfos.size() > 0) {
                    if (index < poiInfos.size()) {
                        name.setText(poiInfos.get(index).name);
                        address.setText(poiInfos.get(index).address);
                        phoneNum.setText(poiInfos.get(index).phoneNum);
                    }
                }
                cardShowAnim.start();
            }
        });

    }


    class MyPullListener implements SmartPullView.OnPullListener {

        @Override
        public void pull() {
            head.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            hot.setVisibility(View.INVISIBLE);
            cold.setVisibility(View.INVISIBLE);
        }

        @Override
        public void pullDone() {
            hot.setVisibility(View.VISIBLE);
            cold.setVisibility(View.VISIBLE);
            head.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        int index = 0;
        switch (v.getId()) {
            case R.id.close:
                index = -1;
                finish();
                break;
            case R.id.movie:
                index = 1;
                break;
            case R.id.meishi:
                index = 2;
                break;
            case R.id.atm:
                index = 3;
                break;
            case R.id.bus:
                index = 4;
                break;
            case R.id.subway:
                index = 5;
                break;
            default:
                break;
        }

        if (index > 0) {
            selectedTab(index);
        }


    }

    private void initLocationAndSearch() {

        //location
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(this);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //search

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new MyPoiSearchListener());
        mNearbySearchOption = new PoiNearbySearchOption()
                .radius(5000)
                .pageNum(1)
                .pageCapacity(20)
                .sortType(PoiSortType.distance_from_near_to_far);

    }


    private void resetTabs() {
        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);

        img1.setImageResource(R.drawable.movie);
        img2.setImageResource(R.drawable.meishi);
        img3.setImageResource(R.drawable.atm);
        img4.setImageResource(R.drawable.busstatioin);
        img5.setImageResource(R.drawable.subway);
    }

    private void selectedTab(int index) {
        resetTabs();
        switch (index) {
            case 1:
                tv1.setTextColor(tabselColor);
                img1.setImageResource(R.drawable.movies);
                keyWord = tv1.getText().toString();
                break;
            case 2:
                img2.setImageResource(R.drawable.meishis);
                tv2.setTextColor(tabselColor);
                keyWord = tv2.getText().toString();
                break;
            case 3:
                img3.setImageResource(R.drawable.atms);
                tv3.setTextColor(tabselColor);
                keyWord = tv3.getText().toString();
                break;
            case 4:
                img4.setImageResource(R.drawable.buss);
                tv4.setTextColor(tabselColor);
                keyWord = tv4.getText().toString();
                break;
            case 5:
                img5.setImageResource(R.drawable.subways);
                tv5.setTextColor(tabselColor);
                keyWord = tv5.getText().toString();
                break;
            default:
                break;
        }

        if (refreshView.getVisibility() == View.VISIBLE) {
            refreshView.setVisibility(View.GONE);
            radar.setVisibility(View.VISIBLE);
            radar.startAnim();
        }

        if (latLng != null && mNearbySearchOption != null && keyWord != null) {
            mNearbySearchOption.location(latLng).keyword(keyWord);
            mPoiSearch.searchNearby(mNearbySearchOption);
        }
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Log.e(TAG, "onReceiveLocation: " + bdLocation);
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        district.setText(bdLocation.getAddress().district);
        latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        movie.performClick();
    }


    @Subscribe
    public void onPoiResultEvent(PoiResult poiResult) {

        if (poiResult != null && poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
            poiInfos = poiResult.getAllPoi();
            name.setText(poiInfos.get(0).name);
            address.setText(poiInfos.get(0).address);
            phoneNum.setText(poiInfos.get(0).phoneNum);

            index = 1;

            if (refreshView.getVisibility() == View.GONE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        radar.stopAnim();
                        radar.setVisibility(View.GONE);
                        refreshView.setVisibility(View.VISIBLE);
                        cardShowAnim.start();
                    }
                }, 3000);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    radar.stopAnim();
                    radar.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                }
            }, 2000);

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.start();
            } else {
                Toast.makeText(mContext, "没有定位权限", Toast.LENGTH_SHORT).show();
                radar.stopAnim();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }

        EventBus.getDefault().unregister(mContext);
    }


}
