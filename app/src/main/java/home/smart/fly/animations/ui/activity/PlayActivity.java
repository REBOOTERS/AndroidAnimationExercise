package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.EasySeekBar;


public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.rotateX)
    EasySeekBar mRotateX;
    @BindView(R.id.width)
    TextView mWidth;
    @BindView(R.id.height)
    TextView mHeight;
    @BindView(R.id.translationX)
    EasySeekBar mTranslationX;
    @BindView(R.id.translationY)
    EasySeekBar mTranslationY;
    @BindView(R.id.translationZ)
    EasySeekBar mTranslationZ;
    @BindView(R.id.PivotX)
    EasySeekBar mPivotX;
    @BindView(R.id.PivotY)
    EasySeekBar mPivotY;
    @BindView(R.id.negate)
    CheckBox mNegate;
    @BindView(R.id.rotateY)
    EasySeekBar mRotateY;
    @BindView(R.id.scaleX)
    EasySeekBar mScaleX;
    @BindView(R.id.scaleY)
    EasySeekBar mScaleY;
    @BindView(R.id.scrollX)
    EasySeekBar mScrollX;
    @BindView(R.id.scrollY)
    EasySeekBar mScrollY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        playView();
    }

    private void playView() {


        mTranslationX.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mTranslationX.setNegate(mNegate.isChecked());
                ViewCompat.setTranslationX(mImage, value);
            }
        });
        mTranslationY.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mTranslationY.setNegate(mNegate.isChecked());
                ViewCompat.setTranslationY(mImage, value);
            }
        });
        mTranslationZ.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mTranslationZ.setNegate(mNegate.isChecked());
                ViewCompat.setTranslationZ(mImage, value);
            }
        });
        mPivotX.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                ViewCompat.setPivotX(mImage, value);
            }
        });
        mPivotY.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                ViewCompat.setPivotY(mImage, value);
            }
        });


        mRotateX.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mRotateX.setNegate(mNegate.isChecked());
                ViewCompat.setRotationX(mImage, value);
            }
        });
        mRotateY.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mRotateY.setNegate(mNegate.isChecked());
                ViewCompat.setRotationY(mImage, value);
            }
        });
        mScaleX.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                float scale = value / 100.0f;
                ViewCompat.setScaleX(mImage, scale);
            }
        });
        mScaleY.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                float scale = value / 100.0f;
                ViewCompat.setScaleY(mImage, scale);
            }
        });

        mScrollX.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mScrollX.setNegate(mNegate.isChecked());
                mImage.scrollTo(value, 0);

            }
        });

        mScrollY.setOnProgressChangedListener(new EasySeekBar.onProgressChangeListener() {
            @Override
            public void onChangedValue(int value) {
                mScrollY.setNegate(mNegate.isChecked());
                mImage.scrollTo(0, value);

            }
        });


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.e(TAG, "getPivotX " + mImage.getPivotX());
        Log.e(TAG, "getPivotY " + mImage.getPivotY());
        Log.e(TAG, "getWidth " + mImage.getWidth());
        Log.e(TAG, "getHeight " + mImage.getHeight());
        Log.e(TAG, "getMeasuredWidth " + mImage.getMeasuredWidth());
        Log.e(TAG, "getMeasuredHeight " + mImage.getMeasuredHeight());
        Log.e(TAG, "getElevation " + ViewCompat.getElevation(mImage));
        Log.e(TAG, "getX: " + mImage.getX());
        Log.e(TAG, "getY: " + mImage.getY());
        Log.e(TAG, "getScrollX: " + mImage.getScrollX());
        Log.e(TAG, "getScrollY: " + mImage.getScrollY());

        mWidth.setText("ImageWidth= " + mImage.getMeasuredWidth());
        mHeight.setText("ImageHeight= " + mImage.getMeasuredHeight());
        mScrollX.setMax(mImage.getMeasuredWidth());
        mScrollY.setMax(mImage.getMeasuredHeight());
        mTranslationX.setMax(mImage.getMeasuredWidth());
        mTranslationY.setMax(mImage.getMeasuredHeight());
        mPivotX.setMax(mImage.getMeasuredWidth());
        mPivotY.setMax(mImage.getMeasuredHeight());
        mRotateX.setMax(360);
        mRotateY.setMax(360);

        //
        mScaleX.setMax(120);
        mScaleY.setMax(120);
        mScaleX.setProgresss(100);
        mScaleY.setProgresss(100);
    }
}
