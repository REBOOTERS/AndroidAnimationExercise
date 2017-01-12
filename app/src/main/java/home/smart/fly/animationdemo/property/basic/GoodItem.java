package home.smart.fly.animationdemo.property.basic;

import android.graphics.Bitmap;

/**
 */
public class GoodItem {


    // 商品图
    private Bitmap mGoodsBitmap;

    public GoodItem(Bitmap mGoodsBitmap) {
        this.mGoodsBitmap = mGoodsBitmap;
    }

    public Bitmap getmGoodsBitmap() {
        return mGoodsBitmap;
    }

    public void setmGoodsBitmap(Bitmap mGoodsBitmap) {
        this.mGoodsBitmap = mGoodsBitmap;
    }
}
