package com.engineer.imitate.ui.widget.opensource;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;

/**
 * Created by Marksss on 2019/8/13.
 */
public abstract class AbsBannerAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    protected abstract int getCount();

    protected abstract View makeView(Context context);

    protected abstract void bind(View view, int position);
}
