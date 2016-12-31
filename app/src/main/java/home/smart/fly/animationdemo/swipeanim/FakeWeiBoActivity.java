package home.smart.fly.animationdemo.swipeanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.StatusBarUtil;

public class FakeWeiBoActivity extends AppCompatActivity implements View.OnClickListener, BDLocationListener {
    private static final String TAG = "FakeWeiBoActivity";
    private Context mContext;
    //head
    private TextView close;
    private TextView district;
    private TextView name, address;


    private SmartPullView refreshView;
    private RadarScanView radar;

    //
    private LinearLayout head, bottom;
    private TextView hot, cold;
    //
    private LinearLayout card;
    private AnimatorSet cardHideAnim;
    private AnimatorSet cardShowAnim;

    //location
    public LocationClient mLocationClient = null;
    //
    private PoiSearch mPoiSearch;
    private PoiNearbySearchOption mNearbySearchOption;
    private PoiCitySearchOption mCitySearchOption;
    private String keyWord;
    private LatLng latLng;
    //
    private List<PoiInfo> poiInfos;
    private int index = 0;


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


        mLocationClient.start();
        index = 0;


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        radar.startAnim();
    }

    private void InitView() {
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
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);


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
//                card.setImageResource(R.drawable.scenes);

                if (poiInfos != null && poiInfos.size() > 0) {
                    if (index < poiInfos.size()) {
                        name.setText(poiInfos.get(index).name);
                        address.setText(poiInfos.get(index).address);
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
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
            default:
                break;
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
        keyWord = "电影";
        mNearbySearchOption = new PoiNearbySearchOption().keyword(keyWord);
        mCitySearchOption = new PoiCitySearchOption().keyword(keyWord);

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        Log.e(TAG, "onReceiveLocation: current Thread name " + Thread.currentThread().getName());

        district.setText(bdLocation.getAddress().district);

        String city = bdLocation.getCity();
        latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        mNearbySearchOption.location(latLng).pageNum(1).pageCapacity(10).sortType(PoiSortType.distance_from_near_to_far);
        mCitySearchOption.city(city).pageNum(1).pageCapacity(10);
//        mPoiSearch.searchNearby(mNearbySearchOption);
        mPoiSearch.searchInCity(mCitySearchOption);
    }

    @Subscribe
    public void onPoiResultEvent(PoiResult poiResult) {
        poiInfos = poiResult.getAllPoi();
        name.setText(poiInfos.get(0).name);
        address.setText(poiInfos.get(0).address);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }
    }
}
