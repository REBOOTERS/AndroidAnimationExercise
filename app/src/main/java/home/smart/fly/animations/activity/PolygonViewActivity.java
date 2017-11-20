package home.smart.fly.animations.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.polygon.Label;
import home.smart.fly.animations.customview.polygon.PolygonView;

public class PolygonViewActivity extends AppCompatActivity {
    private List<Label> mLabels = new ArrayList<>();
    private PolygonView mPolygonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_view);
        mPolygonView = (PolygonView) findViewById(R.id.polygon);

        mLabels = new ArrayList<>();
        mLabels.add(new Label("速度", 90));
        mLabels.add(new Label("射门", 93));
        mLabels.add(new Label("传球", 82));
        mLabels.add(new Label("盘带", 90));
        mLabels.add(new Label("防守", 33));
        mLabels.add(new Label("力量", 80));


        mPolygonView.setLabels(mLabels);
    }
}
