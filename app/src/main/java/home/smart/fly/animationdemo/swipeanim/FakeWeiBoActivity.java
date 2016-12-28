package home.smart.fly.animationdemo.swipeanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.StatusBarUtil;

public class FakeWeiBoActivity extends AppCompatActivity implements View.OnClickListener, BDLocationListener {
    private static final String TAG = "FakeWeiBoActivity";
    //head
    private TextView close;
    private TextView district;


    private SmartPullView refreshView;

    //
    private LinearLayout head, bottom;
    //
    private ImageView card;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_fake_wei_bo);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.background), 0);
        InitView();
        InitAnim();
        initLocationAndSearch();

        mLocationClient.start();


    }


    private void InitView() {
        refreshView = (SmartPullView) findViewById(R.id.refreshView);
        refreshView.setOnHeaderRefreshListener(new MyHeadListener());
        refreshView.setOnFooterRefreshListener(new MyFooterListener());
        refreshView.setOnPullListener(new MyPullListener());
        //
        head = (LinearLayout) findViewById(R.id.head);
        bottom = (LinearLayout) findViewById(R.id.bottom);
        close = (TextView) findViewById(R.id.close);
        close.setOnClickListener(this);
        district = (TextView) findViewById(R.id.district);

        card = (ImageView) findViewById(R.id.card);


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
            }
        });


    }


    class MyHeadListener implements SmartPullView.OnHeaderRefreshListener {

        @Override
        public void onHeaderRefresh(SmartPullView view) {
            refreshView.onHeaderRefreshComplete();
            cardAnimActions();
        }


    }

    class MyFooterListener implements SmartPullView.OnFooterRefreshListener {

        @Override
        public void onFooterRefresh(SmartPullView view) {
            refreshView.onFooterRefreshComplete();
            cardAnimActions();
        }
    }


    private void cardAnimActions() {

        cardHideAnim.start();
        cardHideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                card.setImageResource(R.drawable.scenes);
                cardShowAnim.start();
            }
        });

    }


    class MyPullListener implements SmartPullView.OnPullListener {

        @Override
        public void pull() {
            head.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
        }

        @Override
        public void pullDone() {
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
        latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        mNearbySearchOption.location(latLng).pageNum(1).pageCapacity(10).sortType(PoiSortType.distance_from_near_to_far);
        mCitySearchOption.city(city).pageNum(1).pageCapacity(10);
        mPoiSearch.searchNearby(mNearbySearchOption);
//        mPoiSearch.searchInCity(mCitySearchOption);
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
