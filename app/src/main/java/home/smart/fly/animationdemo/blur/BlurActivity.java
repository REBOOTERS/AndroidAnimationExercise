package home.smart.fly.animationdemo.blur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.BaseActivity;

/**
 * Created by co-mall on 2016/8/19.
 */
public class BlurActivity extends BaseActivity {
    /**
     * 原始图片控件
     */
    private ImageView mOriginImg;

    /**
     * 模糊后的图片控件
     */
    private ImageView mBluredImage;

    /**
     * 进度条SeekBar
     */
    private SeekBar mSeekBar;

    /**
     * 显示进度的文字
     */
    private TextView mProgressTv;

    /**
     * 透明度
     */
    private int mAlpha;

    /**
     * 原始图片
     */
    private Bitmap mTempBitmap;

    /**
     * 模糊后的图片
     */
    private Bitmap mFinalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);

        // 初始化视图
        initViews();

        // 获取图片
        mTempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.phone);
        mFinalBitmap = BlurImage.blur(this, mTempBitmap);

        // 填充模糊后的图像和原图
        mBluredImage.setImageBitmap(mFinalBitmap);
        mOriginImg.setImageBitmap(mTempBitmap);

        // 处理seekbar滑动事件
        setSeekBar();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mBluredImage = (ImageView) findViewById(R.id.activity_main_blured_img);
        mOriginImg = (ImageView) findViewById(R.id.activity_main_origin_img);
        mSeekBar = (SeekBar) findViewById(R.id.activity_main_seekbar);
        mProgressTv = (TextView) findViewById(R.id.activity_main_progress_tv);
    }

    /**
     * 处理seekbar滑动事件
     */
    private void setSeekBar() {
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                float value = (float) (255.0f - mAlpha * 2.55);
                Log.e("seek", "the valus is " + value);
                mOriginImg.setAlpha(value);
                mProgressTv.setText(String.valueOf(mAlpha));
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
