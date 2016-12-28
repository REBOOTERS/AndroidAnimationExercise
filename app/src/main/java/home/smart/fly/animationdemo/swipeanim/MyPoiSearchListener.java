package home.smart.fly.animationdemo.swipeanim;

import android.util.Log;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;

/**
 * Created by rookie on 2016/12/28.
 */

public class MyPoiSearchListener implements OnGetPoiSearchResultListener {
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        Log.e("onGetPoiResult", "the poiResult " + poiResult.describeContents());
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
