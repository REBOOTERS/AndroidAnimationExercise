package home.smart.fly.animationdemo.customview.swipeanim;

import android.util.Log;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by rookie on 2016/12/28.
 */

public class MyPoiSearchListener implements OnGetPoiSearchResultListener {
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        Log.e("onGetPoiResult", "the poiResult " + poiResult.describeContents());
        EventBus.getDefault().post(poiResult);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        Log.e("onGetPoiDetailResult", "--------->" + poiDetailResult);
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        Log.e("onGetPoiIndoorResult", "--------->" + poiIndoorResult);
    }
}
