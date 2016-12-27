package home.smart.fly.animationdemo.swipeanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.utils.StatusBarUtil;

public class FakeWeiBoActivity extends AppCompatActivity implements View.OnClickListener {
    //head
    private TextView close;


    private SmartPullView refreshView;

    //
    private LinearLayout head, bottom;
    //
    private ImageView card;
    private AnimatorSet cardHideAnim;
    private AnimatorSet cardShowAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_wei_bo);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.background), 0);
        InitView();
        InitAnim();
    }


    private void InitView() {
        refreshView = (SmartPullView) findViewById(R.id.refreshView);
        refreshView.setOnHeaderRefreshListener(new MyHeadListener());
        refreshView.setOnFooterRefreshListener(new MyFooterListener());
        refreshView.setOnPullListener(new MyPullListener());
        //
        head = (LinearLayout) findViewById(R.id.head);
        bottom = (LinearLayout) findViewById(R.id.bottom);
        close = (TextView) findViewById(R.id.close);
        close.setOnClickListener(this);

        card = (ImageView) findViewById(R.id.card);


    }

    private void InitAnim() {


        cardHideAnim = new AnimatorSet();
        ObjectAnimator hideAnim = ObjectAnimator.ofFloat(card, "alpha", 1.0f, 0.0f);
        ObjectAnimator hideAnimX = ObjectAnimator.ofFloat(card, "scaleX", 1.0f, 0f);
        ObjectAnimator hideAnimY = ObjectAnimator.ofFloat(card, "scaleY", 1.0f, 0f);
        cardHideAnim.playTogether(hideAnimX, hideAnimY, hideAnim);
        cardHideAnim.setDuration(300);

        cardShowAnim = new AnimatorSet();
        final ObjectAnimator showAnimX = ObjectAnimator.ofFloat(card, "scaleX", 0f, 1.0f);
        final ObjectAnimator showAnimY = ObjectAnimator.ofFloat(card, "scaleY", 0f, 1.0f);
        final ObjectAnimator showAnim = ObjectAnimator.ofFloat(card, "alpha", 0f, 1.0f);
        cardShowAnim.playTogether(showAnimX, showAnimY, showAnim);
        cardShowAnim.setDuration(500);

        cardShowAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                head.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.VISIBLE);
            }
        });


    }


    class MyHeadListener implements SmartPullView.OnHeaderRefreshListener {

        @Override
        public void onHeaderRefresh(SmartPullView view) {
            refreshView.onHeaderRefreshComplete();
            cardAnimActions();
        }


    }

    class MyFooterListener implements SmartPullView.OnFooterRefreshListener {

        @Override
        public void onFooterRefresh(SmartPullView view) {
            refreshView.onFooterRefreshComplete();
            cardAnimActions();
        }
    }


    private void cardAnimActions() {

        cardHideAnim.start();
        cardHideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                card.setImageResource(R.drawable.scenes);
                cardShowAnim.start();
            }
        });

    }


    class MyPullListener implements SmartPullView.OnPullListener {

        @Override
        public void pull() {
            head.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
        }

        @Override
        public void pullDone() {
            head.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
            default:
                break;
        }
    }
}
