package home.smart.fly.animations.sugar.viewholder;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhihu.android.sugaradapter.Layout;
import com.zhihu.android.sugaradapter.SugarHolder;

import home.smart.fly.animations.R;
import home.smart.fly.animations.sugar.bean.Item;

/**
 * @authro: Rookie
 * @since: 2018-12-27
 */
@Layout(R.layout.small_info_item)
public final class SmallItemHolder extends SugarHolder<Item> {


    private RelativeLayout mItemshell;
    private TextView mTitle;
    private TextView mDesc;

    public SmallItemHolder(@NonNull View view) {
        super(view);
        mItemshell = view.findViewById(R.id.itemshell);
        mTitle = view.findViewById(R.id.title);
        mDesc = view.findViewById(R.id.desc);
    }

    @Override
    protected void onBindData(@NonNull Item data) {
        mTitle.setText(data.getTitle());
        mDesc.setText(data.getSubTitle());
        if (data.getSubTitle().startsWith("/")) {
            mDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_red));
        } else {
            mDesc.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tab_indicator_text));
        }
    }
}
