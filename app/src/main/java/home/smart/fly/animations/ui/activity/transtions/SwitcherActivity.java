package home.smart.fly.animations.ui.activity.transtions;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;

public class SwitcherActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory,
        View.OnTouchListener {

    @BindView(R.id.imageSwitcher)
    ImageSwitcher mImageSwitcher;

    // 要显示的图片在图片数组中的Index
    private int pictureIndex;
    // 左右滑动时手指按下的X坐标
    private float touchDownX;
    // 左右滑动时手指松开的X坐标
    private float touchUpX;

    private int[] arrayPictures = {R.drawable.a5, R.drawable.a6, R.drawable.cat};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);
        ButterKnife.bind(this);
        mImageSwitcher.setFactory(this);
        mImageSwitcher.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 取得左右滑动时手指按下的X坐标 
            touchDownX = event.getX();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 取得左右滑动时手指松开的X坐标 
            touchUpX = event.getX();
            // 从左往右，看前一张 
            if (touchUpX - touchDownX > 100) {
                // 取得当前要看的图片的index 
                pictureIndex = pictureIndex == 0 ? arrayPictures.length - 1
                        : pictureIndex - 1;
                // 设置图片切换的动画 
                mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.slide_in_left));
                mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.slide_out_right));
                // 设置当前要看的图片 
                mImageSwitcher.setImageResource(arrayPictures[pictureIndex]);
                // 从右往左，看下一张 
            } else if (touchDownX - touchUpX > 100) {
                // 取得当前要看的图片的index 
                pictureIndex = pictureIndex == arrayPictures.length - 1 ? 0
                        : pictureIndex + 1;
                // 设置图片切换的动画
                // 由于Android没有提供slide_out_left和slide_in_right，所以仿照slide_in_left和slide_out_right编写了slide_out_left和slide_in_right
                mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.slide_out_left));
                mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.slide_in_right));
                // 设置当前要看的图片 
                mImageSwitcher.setImageResource(arrayPictures[pictureIndex]);
            }
            return true;
        }
        return false;
    }

    @Override
    public View makeView() {
        ImageView mImageView = new ImageView(this);
        mImageView.setImageResource(arrayPictures[pictureIndex]);
        return mImageView;
    }
}
