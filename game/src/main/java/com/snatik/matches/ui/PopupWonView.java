package com.snatik.matches.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engineer.android.game.R;
import com.snatik.matches.common.Music;
import com.snatik.matches.common.Shared;
import com.snatik.matches.events.ui.BackGameEvent;
import com.snatik.matches.events.ui.NextGameEvent;
import com.snatik.matches.model.GameState;
import com.snatik.matches.utils.Clock;
import com.snatik.matches.utils.Clock.OnTimerCount;
import com.snatik.matches.utils.FontLoader;
import com.snatik.matches.utils.FontLoader.Font;

public class PopupWonView extends RelativeLayout {

	private TextView mTime;
	private TextView mScore;
	private ImageView mStar1;
	private ImageView mStar2;
	private ImageView mStar3;
	private ImageView mNextButton;
	private ImageView mBackButton;
	private Handler mHandler;

	public PopupWonView(Context context) {
		this(context, null);
	}

	public PopupWonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.popup_won_view, this, true);
		mTime = (TextView) findViewById(R.id.time_bar_text);
		mScore = (TextView) findViewById(R.id.score_bar_text);
		mStar1 = (ImageView) findViewById(R.id.star_1);
		mStar2 = (ImageView) findViewById(R.id.star_2);
		mStar3 = (ImageView) findViewById(R.id.star_3);
		mBackButton = (ImageView) findViewById(R.id.button_back);
		mNextButton = (ImageView) findViewById(R.id.button_next);
		FontLoader.setTypeface(context, new TextView[] { mTime, mScore }, Font.GROBOLD);
		setBackgroundResource(R.drawable.level_complete);
		mHandler = new Handler();
		
		mBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared.eventBus.notify(new BackGameEvent());
			}
		});

		mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared.eventBus.notify(new NextGameEvent());
			}
		});
	}

	public void setGameState(final GameState gameState) {
		int min = gameState.remainedSeconds / 60;
		int sec = gameState.remainedSeconds - min * 60;
		mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
		mScore.setText("" + 0);
		
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				animateScoreAndTime(gameState.remainedSeconds, gameState.achievedScore);
				animateStars(gameState.achievedStars);
			}
		}, 500);
	}

	private void animateStars(int start) {
		switch (start) {
		case 0:
			mStar1.setVisibility(View.GONE);
			mStar2.setVisibility(View.GONE);
			mStar3.setVisibility(View.GONE);
			break;
		case 1:
			mStar2.setVisibility(View.GONE);
			mStar3.setVisibility(View.GONE);
			mStar1.setAlpha(0f);
			animateStar(mStar1, 0);
			break;
		case 2:
			mStar3.setVisibility(View.GONE);
			mStar1.setVisibility(View.VISIBLE);
			mStar1.setAlpha(0f);
			animateStar(mStar1, 0);
			mStar2.setVisibility(View.VISIBLE);
			mStar2.setAlpha(0f);
			animateStar(mStar2, 600);
			break;
		case 3:
			mStar1.setVisibility(View.VISIBLE);
			mStar1.setAlpha(0f);
			animateStar(mStar1, 0);
			mStar2.setVisibility(View.VISIBLE);
			mStar2.setAlpha(0f);
			animateStar(mStar2, 600);
			mStar3.setVisibility(View.VISIBLE);
			mStar3.setAlpha(0f);
			animateStar(mStar3, 1200);
			break;
		default:
			break;
		}
	}
	
	private void animateStar(final View view, int delay) {
		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
		alpha.setDuration(100);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(alpha, scaleX, scaleY);
		animatorSet.setInterpolator(new BounceInterpolator());
		animatorSet.setStartDelay(delay);
		animatorSet.setDuration(600);
		view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animatorSet.start();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Music.showStar();
			}
		}, delay);
	}

	private void animateScoreAndTime(final int remainedSeconds, final int achievedScore) {
		final int totalAnimation = 1200;

		Clock.getInstance().startTimer(totalAnimation, 35, new OnTimerCount() {

			@Override
			public void onTick(long millisUntilFinished) {
				float factor = millisUntilFinished / (totalAnimation * 1f); // 0.1
				int scoreToShow = achievedScore - (int) (achievedScore * factor);
				int timeToShow = (int) (remainedSeconds * factor);
				int min = timeToShow / 60;
				int sec = timeToShow - min * 60;
				mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
				mScore.setText("" + scoreToShow);
			}

			@Override
			public void onFinish() {
				mTime.setText(" " + String.format("%02d", 0) + ":" + String.format("%02d", 0));
				mScore.setText("" + achievedScore);
			}
		});

	}

}
