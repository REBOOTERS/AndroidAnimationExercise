package com.engineer.imitate.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.engineer.imitate.R;
import com.engineer.imitate.ui.widget.ShadowLayout;

public class StarShowActivity extends AppCompatActivity {
    private ShadowLayout ShadowLayout;
    private SeekBar skbar_x;
    private SeekBar skbar_y;
    private SeekBar skbar_limit;
    private SeekBar skbar_corner;
    private int alpha;
    private SeekBar skbar_alpha;
    private int red;
    private SeekBar skbar_red;
    private int green;
    private SeekBar skbar_green;
    private int blue;
    private SeekBar skbar_blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starshow);
        ShadowLayout = findViewById(R.id.ShadowLayout);
        skbar_x = findViewById(R.id.skbar_x);
        skbar_y = findViewById(R.id.skbar_y);
        skbar_limit = findViewById(R.id.skbar_limit);
        skbar_corner = findViewById(R.id.skbar_corner);
        skbar_alpha = findViewById(R.id.skbar_alpha);
        skbar_red = findViewById(R.id.skbar_red);
        skbar_green = findViewById(R.id.skbar_green);
        skbar_blue = findViewById(R.id.skbar_blue);


        skbar_corner.setMax((int) (ShadowLayout.getmCornerRadius() * 3));
        skbar_corner.setProgress((int) ShadowLayout.getmCornerRadius());
        skbar_corner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ShadowLayout.setmCornerRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_limit.setMax((int) (ShadowLayout.getmShadowLimit() * 3));
        skbar_limit.setProgress((int) ShadowLayout.getmShadowLimit());
        skbar_limit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ShadowLayout.setmShadowLimit(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_x.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ShadowLayout.setMDx(progress - 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_y.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ShadowLayout.setMDy(progress - 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha = progress;
                ShadowLayout.setmShadowColor(Color.argb(alpha, red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress;
                ShadowLayout.setmShadowColor(Color.argb(alpha, red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress;
                ShadowLayout.setmShadowColor(Color.argb(alpha, red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbar_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress;
                ShadowLayout.setmShadowColor(Color.argb(alpha, red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

}
