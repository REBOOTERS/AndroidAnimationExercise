package home.smart.fly.animations.customview.flipview;

import android.graphics.Canvas;

public class RubberBandOverFlipper implements OverFlipper {

	private static final float MAX_OVER_FLIP_DISTANCE = 70;
	private static final float EXPONENTIAL_DECREES = 0.85f;
	
	private float mTotalOverFlip;
	private float mCurrentOverFlip;

	@Override
	public float calculate(float flipDistance, float minFlipDistance,
			float maxFlipDistance) {
		
		float deltaOverFlip;
		if(flipDistance<minFlipDistance) {
			deltaOverFlip = flipDistance - minFlipDistance - mCurrentOverFlip;
		}else {
			deltaOverFlip = flipDistance - maxFlipDistance - mCurrentOverFlip;
		}
		
		mTotalOverFlip += deltaOverFlip;
		
		float sign = Math.signum(mTotalOverFlip);
		
		mCurrentOverFlip = (float) Math.pow(Math.abs(mTotalOverFlip), EXPONENTIAL_DECREES) * sign;
		
		
		if(mCurrentOverFlip < 0) {
			mCurrentOverFlip = Math.max(-MAX_OVER_FLIP_DISTANCE, mCurrentOverFlip);
		}else {
			mCurrentOverFlip = Math.min(MAX_OVER_FLIP_DISTANCE, mCurrentOverFlip);
		}
		
		return mCurrentOverFlip + (mCurrentOverFlip < 0 ? minFlipDistance : maxFlipDistance);
	}

	@Override
	public boolean draw(Canvas c) {
		return false;
	}

	@Override
	public void overFlipEnded() {
		mTotalOverFlip = 0;
		mCurrentOverFlip = 0;
	}

	@Override
	public float getTotalOverFlip() {
		return mTotalOverFlip;
	}

}
