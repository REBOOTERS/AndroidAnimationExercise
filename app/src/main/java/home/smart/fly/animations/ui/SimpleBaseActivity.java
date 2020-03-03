package home.smart.fly.animations.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.transformationlayout.TransformationLayout;
import com.skydoves.transformationlayout.TransitionExtensionKt;

/**
 * @author Rookie
 * @since 03-03-2020
 *
 * 仅仅是为了方便添加  ； TransformationLayout  相关的内容
 */
public class SimpleBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TransformationLayout.Params params = getIntent().getParcelableExtra("TransformationParams");
        TransitionExtensionKt.onTransformationEndContainer(this, params);
        super.onCreate(savedInstanceState);
    }
}
