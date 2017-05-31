package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;


public class PlayActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.translationX)
    SeekBar mTranslationX;
    @BindView(R.id.translationXvalue)
    TextView mTranslationXvalue;
    @BindView(R.id.translationY)
    SeekBar mTranslationY;
    @BindView(R.id.translationYvalue)
    TextView mTranslationYvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        playView();
    }

    private void playView() {
        mTranslationX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewCompat.setTranslationX(mImage, progress);
                mTranslationXvalue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTranslationY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewCompat.setTranslationY(mImage, progress);
                mTranslationYvalue.setText(String.valueOf(progress));
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
