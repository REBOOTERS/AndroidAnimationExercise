package home.smart.fly.animationdemo.tradition;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by co-mall on 2016/8/8.
 */
public class TweenedAnimationActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private ImageView img;
    //
    private CheckBox keep;
    private CheckBox loop;
    private CheckBox reverse;
    //
    private RadioGroup selectStyle;
    private RadioButton rb1, rb2, rb3;
    //
    private SeekBar pivotX, pivotY, degree, time;
    private float pxValue, pyValue, deValue;
    private int timeValue;
    private TextView xValue, yValue, dValue, tValue;

    private Animation animation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_tween_animation);
        findViewById(R.id.alpha).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.translate).setOnClickListener(this);
        findViewById(R.id.rotate).setOnClickListener(this);
        findViewById(R.id.stopAnim).setOnClickListener(this);
        findViewById(R.id.img).setOnClickListener(this);
        keep = (CheckBox) findViewById(R.id.keep);
        loop = (CheckBox) findViewById(R.id.loop);
        reverse = (CheckBox) findViewById(R.id.reverse);
        selectStyle = (RadioGroup) findViewById(R.id.selectStyle);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        pivotX = (SeekBar) findViewById(R.id.pivotX);
        pivotY = (SeekBar) findViewById(R.id.pivotY);
        degree = (SeekBar) findViewById(R.id.degree);
        time = (SeekBar) findViewById(R.id.time);
        degree.setProgress(25);
        deValue = 360 * 25 / 100.0f;

        time.setProgress(10);
        timeValue = 100 * 10;

        xValue = (TextView) findViewById(R.id.xvalue);
        yValue = (TextView) findViewById(R.id.yvalue);
        dValue = (TextView) findViewById(R.id.dvalue);
        dValue.setText(String.valueOf(deValue));
        tValue = (TextView) findViewById(R.id.tValue);
        tValue.setText(String.valueOf(timeValue) + " ms");

        img = (ImageView) findViewById(R.id.img);

        //
        pivotX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pxValue = progress / 100.0f;
                xValue.setText(String.valueOf(pxValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pivotY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pyValue = progress / 100.0f;
                yValue.setText(String.valueOf(pyValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        degree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                deValue = 360 * progress / 100.0f;
                dValue.setText(String.valueOf(deValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 10) {
                    progress = 10;
                } else if (progress >= 100) {
                    progress = 100;
                }


                timeValue = 100 * progress;
                tValue.setText(String.valueOf(timeValue) + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alpha:
                AlpahAnimation();
                break;
            case R.id.translate:
                TranslationAnimation();
                break;
            case R.id.scale:
                ScaleAnimation();
                break;
            case R.id.rotate:
                RotateAnimation();
                break;
            case R.id.stopAnim:
                img.clearAnimation();

                if (animation != null) {
                    animation.cancel();
                    animation.reset();
                }


                loop.setChecked(false);
                reverse.setChecked(false);
                keep.setChecked(false);

                pivotX.setProgress(0);
                pivotY.setProgress(0);
                degree.setProgress(25);
                time.setProgress(10);
                break;
            case R.id.img:
                Toast.makeText(mContext, "被点击了", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void TranslationAnimation() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_anim);
        animation.setInterpolator(new LinearInterpolator());

        if (keep.isChecked()) {
            animation.setFillAfter(true);
        } else {
            animation.setFillAfter(false);
        }
        if (loop.isChecked()) {
            animation.setRepeatCount(-1);
        } else {
            animation.setRepeatCount(0);
        }

        if (reverse.isChecked()) {
            animation.setRepeatMode(Animation.REVERSE);
        } else {
            animation.setRepeatMode(Animation.RESTART);
        }
        img.startAnimation(animation);
    }

    /**
     * rotate Animation
     */
    private void RotateAnimation() {
        animation = new RotateAnimation(-deValue, deValue, Animation.RELATIVE_TO_SELF,
                pxValue, Animation.RELATIVE_TO_SELF, pyValue);
        animation.setDuration(timeValue);

        if (keep.isChecked()) {
            animation.setFillAfter(true);
        } else {
            animation.setFillAfter(false);
        }
        if (loop.isChecked()) {
            animation.setRepeatCount(-1);
        } else {
            animation.setRepeatCount(0);
        }

        if (reverse.isChecked()) {
            animation.setRepeatMode(Animation.REVERSE);
        } else {
            animation.setRepeatMode(Animation.RESTART);
        }
        img.startAnimation(animation);
    }

    /**
     * alpha Animation
     */
    private void AlpahAnimation() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha_anim);
        if (keep.isChecked()) {
            animation.setFillAfter(true);
        } else {
            animation.setFillAfter(false);
        }
        if (loop.isChecked()) {
            animation.setRepeatCount(-1);
        } else {
            animation.setRepeatCount(0);
        }

        if (reverse.isChecked()) {
            animation.setRepeatMode(Animation.REVERSE);
        } else {
            animation.setRepeatMode(Animation.RESTART);
        }
        img.startAnimation(animation);
    }

    /**
     * scale Animation
     */
    private void ScaleAnimation() {
//        animation = null;
        if (rb1.isChecked()) {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_anim1);
        } else if (rb2.isChecked()) {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_anim2);
        } else if (rb3.isChecked()) {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_anim3);
        }

        if (keep.isChecked()) {
            animation.setFillAfter(true);
        } else {
            animation.setFillAfter(false);
        }
        if (loop.isChecked()) {
            animation.setRepeatCount(-1);
        } else {
            animation.setRepeatCount(0);
        }


        img.startAnimation(animation);
    }

}
