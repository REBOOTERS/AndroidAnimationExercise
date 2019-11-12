package com.engineer.android.game.ui;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.engineer.android.game.util.DefaultOrientation;
import com.engineer.android.game.util.DefaultOrientationUtil;
import com.engineer.android.game.widget.SensorView;

public class SensorViewActivity extends AppCompatActivity {
    private static final String TAG = "SensorViewActivity";
    //SensorManager对象引用
    SensorManager mySensorManager;
    Sensor sensor;

    SensorView mv;
    private SensorEventListener mel = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {//重写onSensorChanged方法
            float[] values = event.values;//获取加速度传感器的测量值
            Log.e(TAG, "onSensorChanged: " + values[0]);
            Log.e(TAG, "onSensorChanged: " + values[1]);
            Log.e(TAG, "onSensorChanged: " + values[2]);
            //若初始姿态是横屏 （指部分Pad设备）
            if (DefaultOrientationUtil.getOrientation(SensorViewActivity.this) == DefaultOrientation.LANDSCAPE) {
                mv.dx = values[1];//为dx赋值
                mv.dy = (-1) * values[0];//为dy赋值
                mv.dz = values[2];//为dz赋值
            } else {//若原始姿态是竖屏 （指手机及另一部分Pad设备）
                mv.dx = values[0];//为dx赋值
                mv.dy = values[1];//为dy赋值
                mv.dz = values[2];//为dz赋值
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //获得SensorManager对象
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mv = new SensorView(this);
        this.setContentView(mv);

    }

    @Override
    protected void onResume() {
        mySensorManager.registerListener(mel, sensor, SensorManager.SENSOR_DELAY_UI);
        mv.mvdt.pauseFlag = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        mySensorManager.unregisterListener(mel);
        mv.mvdt.pauseFlag = true;
        super.onPause();
    }
}