package home.smart.fly.animations.master.adapter;

import androidx.recyclerview.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import home.smart.fly.animations.R;
import home.smart.fly.animations.master.Tools;

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2017/12/10
 * desc   :
 * version: 1.0
 */
public class ModeSelectorAdapter extends BGARecyclerViewAdapter<Integer> {


    public ModeSelectorAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.mode_selector_item);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Integer model) {
        helper.setText(R.id.item, String.valueOf(Tools.intToMode(model)));
    }




}
