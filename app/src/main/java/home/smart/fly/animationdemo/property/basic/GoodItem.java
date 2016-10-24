package home.smart.fly.animationdemo.property.basic;

import android.graphics.Bitmap;

/**
 * @className: GoodsModel
 * @classDescription: 商品实体类
 * @author: leibing
 * @createTime: 2016/09/28
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
