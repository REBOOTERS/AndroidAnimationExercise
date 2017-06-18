package home.smart.fly.animations.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import home.smart.fly.animations.R;

/**
 * Created by co-mall on 2017/6/1.
 */

public class EasySeekBar extends LinearLayout {

    private String defaultLabel;
    private SeekBar mSeekBar;


    private boolean isNegate;

    private onProgressChangeListener listener;

    public EasySeekBar(Context context) {
        this(context, null);
    }

    public EasySeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasySeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.easy_seekbar_layout, this, false);
        TextView labelTv = (TextView) view.findViewById(R.id.label);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView value = (TextView) view.findViewById(R.id.value);
        labelTv.setText(defaultLabel);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isNegate) {
                    progress = -progress;
                }
                value.setText(String.valueOf(progress));
                if (listener != null) {
                    listener.onChangedValue(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addView(view);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.EasySeekBar);
        defaultLabel = mTypedArray.getString(R.styleable.EasySeekBar_label);
        isNegate = mTypedArray.getBoolean(R.styleable.EasySeekBar_isNegate, false);
        mTypedArray.recycle();
    }

    public interface onProgressChangeListener {
        void onChangedValue(int value);
    }


    public void setMax(int value) {
        mSeekBar.setMax(value);
    }

    public void setProgresss(int value) {
        mSeekBar.setProgress(value);
    }

    public void setOnProgressChangedListener(onProgressChangeListener listener) {
        this.listener = listener;
    }

    public boolean isNegate() {
        return isNegate;
    }

    public void setNegate(boolean negate) {
        isNegate = negate;
    }
}
