package home.smart.fly.animations.bga;

import androidx.recyclerview.widget.RecyclerView;


import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import home.smart.fly.animations.R;

/**
 * Created by rookie on 2017/5/23.
 */

public class ContentAdapter extends BGARecyclerViewAdapter<String> {


    public ContentAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.demo_info_item);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, String model) {
        helper.setText(R.id.title, model);
    }
}
