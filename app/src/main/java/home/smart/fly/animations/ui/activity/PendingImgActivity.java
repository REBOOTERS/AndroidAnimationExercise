package home.smart.fly.animations.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.PhotoView;

import home.smart.fly.animations.R;

public class PendingImgActivity extends AppCompatActivity {
    private static final String TAG = "PendingImgActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_img);
        PhotoView mImageView = (PhotoView) findViewById(R.id.img);
        mImageView.setImageResource(R.drawable.name);

        mImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });

        mImageView.setOnViewDragListener(new OnViewDragListener() {
            @Override
            public void onDrag(float dx, float dy) {
//                Log.e(TAG, "onDrag: dx= " + dx);
                Log.e(TAG, "onDrag: dy= " + dy);
            }
        });


//        if (getIntent() != null) {
//            ImageBean mBean = (ImageBean) getIntent().getSerializableExtra("bean");
//            String url = mBean.getFilepath();
//            Glide.with(this).load(url).into(mImageView);
//            Log.e("onCreate", "longitude=" + mBean.getLongitude());
//            Log.e("onCreate", "latitude=" + mBean.getLatitude());
//        }
    }
}
