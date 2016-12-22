package home.smart.fly.animationdemo.swipeanim.lib;

/**
 * Created by xingzhu on 2016/11/9.
 */

public interface ItemTouchListener {
    void onUpRemoved();

    void onDownRemoved();

    void show();

    void hide(float dy);
}
