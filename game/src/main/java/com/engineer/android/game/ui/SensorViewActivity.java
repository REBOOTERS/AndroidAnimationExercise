package com.engineer.android.game.ui;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.engineer.android.game.util.DefaultOrientation;
import com.engineer.android.game.util.DefaultOrientationUtil;
import com.engineer.android.game.widget.MyView;

public class SensorViewActivity extends AppCompatActivity {
    //SensorManager对象引用
    SensorManager mySensorManager;
    Sensor sensor;

    MyView mv;
    private SensorEventListener mel = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {//重写onSensorChanged方法
            float[] values = event.values;//获取加速度传感器的测量值
            //若初始姿态是横屏 （指部分Pad设备）
            if (DefaultOrientationUtil.INSTANCE.getOrientatoin(compatActivity) == DefaultOrientation.LANDSCAPE) {
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

    private AppCompatActivity compatActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compatActivity = this;
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
        //获得SensorManager对象
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mv = new MyView(this);
        this.setContentView(mv);

    }

    @Override
    protected void onResume() {//重写onResume方法
        mySensorManager.registerListener(mel, sensor, SensorManager.SENSOR_DELAY_UI);
        mv.mvdt.pauseFlag = false;
        super.onResume();
    }

    @Override
    protected void onPause() {//重写onPause方法
        mySensorManager.unregisterListener(mel);//取消注册监听器
        mv.mvdt.pauseFlag = true;
        super.onPause();
    }
}