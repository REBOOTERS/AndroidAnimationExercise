package home.smart.fly.animations.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.PaletteUtils;
import home.smart.fly.animations.utils.StatusBarUtil;
import home.smart.fly.animations.utils.Tools;
import java8.util.Optional;
import java8.util.function.Function;
import java8.util.function.Supplier;

public class PaletteActivity extends AppCompatActivity {


    private ImageView mImageView;
    private TextView mContent;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton fab;

    private int[] images = new int[]{R.drawable.cat, R.drawable.a5, R.drawable.miui_nine, R.drawable.a6};
    private int index = 0;
    private int max = images.length - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            index++;
            if (index > max) {
                index = 0;
            }

            int id = images[index];
            mImageView.setImageResource(id);
            magic(id);

        });


        mImageView = findViewById(R.id.image);
        mContent = findViewById(R.id.content);
        mAppBarLayout = findViewById(R.id.app_bar);
        mToolbarLayout = findViewById(R.id.toolbar_layout);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {

        });

        magic(R.drawable.cat);
    }

    private void magic(int id) {
        Palette.Builder builder = new Palette.Builder(BitmapFactory.decodeResource(getResources(), id));
        Palette palette = builder.generate();

        Optional.ofNullable(palette)
                .ifPresent(p -> {
                    Palette.Swatch swatch = getSwatch(p);
                    if (swatch != null) {
                        mContent.setTextColor(swatch.getRgb());

                        mToolbarLayout.setExpandedTitleColor(Color.WHITE);
                        mToolbarLayout.setCollapsedTitleTextColor(swatch.getTitleTextColor());
                        mToolbarLayout.setBackgroundColor(swatch.getRgb());
                        mToolbarLayout.setContentScrimColor(swatch.getRgb());
                        mToolbarLayout.setTitle(String.valueOf(swatch.getRgb()));
                        fab.setBackgroundColor(swatch.getRgb());
                        fab.setRippleColor(swatch.getRgb());


                        StatusBarUtil.setColor(PaletteActivity.this, swatch.getRgb(),0);
                    }
                });
    }


    private int getMagicColor(int id) {
        Palette.Builder builder = new Palette.Builder(BitmapFactory.decodeResource(getResources(), id));
        Palette palette = builder.generate();
        return Optional.ofNullable(palette)
                .map(this::getSwatch)
                .map(Palette.Swatch::getRgb)
                .orElseGet(() -> 0);
    }

    private int getMagicColor(Bitmap bitmap) {
        Palette.Builder builder = new Palette.Builder(bitmap);
        Palette palette = builder.generate();
        return Optional.ofNullable(palette)
                .map(this::getSwatch)
                .map(Palette.Swatch::getRgb)
                .orElseGet(() -> 0);
    }

    private Palette.Swatch getSwatch(Palette palette) {
        return PaletteUtils.getSwatch(palette, PaletteUtils.SwatchStyle.LightMuted, PaletteUtils.SwatchStyle.Muted, PaletteUtils.SwatchStyle.DarkMuted,
                PaletteUtils.SwatchStyle.LightVibrant, PaletteUtils.SwatchStyle.Vibrant, PaletteUtils.SwatchStyle.DarkVibrant);
    }

}
