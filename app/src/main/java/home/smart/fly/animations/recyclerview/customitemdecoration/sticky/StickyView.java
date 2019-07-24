package home.smart.fly.animations.recyclerview.customitemdecoration.sticky;

import android.view.View;

/**
 * Created by cpf on 2018/1/16.
 *
 *  获取吸附View相关的信息
 */

public interface StickyView {

    /**
     * 是否是吸附view
     * @param view
     * @return
     */
    boolean isStickyView(View view);

    /**
     * 得到吸附view的itemType
     * @return
     */
    int getStickViewType();
}
