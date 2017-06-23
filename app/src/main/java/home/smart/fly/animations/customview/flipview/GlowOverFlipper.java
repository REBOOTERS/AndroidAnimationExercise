package home.smart.fly.animations.customview.flipview;

import android.graphics.Canvas;
import android.support.v4.widget.EdgeEffectCompat;

public class GlowOverFlipper implements OverFlipper {
	
	private EdgeEffectCompat mTopEdgeEffect;
	private EdgeEffectCompat mBottomEdgeEffect;
	private FlipView mFlipView;
	private float mTotalOverFlip;
	
	public GlowOverFlipper(FlipView v) {
		mFlipView = v;
		mTopEdgeEffect = new EdgeEffectCompat(v.getContext());
		mBottomEdgeEffect = new EdgeEffectCompat(v.getContext());
	}

	@Override
	public float calculate(float flipDistance, float minFlipDistance,
			float maxFlipDistance) {
		float deltaOverFlip = flipDistance - (flipDistance < 0 ? minFlipDistance : maxFlipDistance);
		
		mTotalOverFlip += deltaOverFlip;
		
		if (deltaOverFlip > 0) {
			mBottomEdgeEffect.onPull(deltaOverFlip
					/ (mFlipView.isFlippingVertically() ? mFlipView.getHeight() : mFlipView.getWidth()));
		} else if (deltaOverFlip < 0) {
			mTopEdgeEffect.onPull(-deltaOverFlip
					/ (mFlipView.isFlippingVertically() ? mFlipView.getHeight() : mFlipView.getWidth()));
		}
		return flipDistance < 0 ? minFlipDistance : maxFlipDistance;
	}

	@Override
	public boolean draw(Canvas c) {
		return drawTopEdgeEffect(c) | drawBottomEdgeEffect(c);
	}

	private boolean drawTopEdgeEffect(Canvas canvas) {
		boolean needsMoreDrawing = false;
		if (!mTopEdgeEffect.isFinished()) {
			canvas.save();
			if (mFlipView.isFlippingVertically()) {
				mTopEdgeEffect.setSize(mFlipView.getWidth(), mFlipView.getHeight());
				canvas.rotate(0);
			} else {
				mTopEdgeEffect.setSize(mFlipView.getHeight(), mFlipView.getWidth());
				canvas.rotate(270);
				canvas.translate(-mFlipView.getHeight(), 0);
			}
			needsMoreDrawing = mTopEdgeEffect.draw(canvas);
			canvas.restore();
		}
		return needsMoreDrawing;
	}

	private boolean drawBottomEdgeEffect(Canvas canvas) {
		boolean needsMoreDrawing = false;
		if (!mBottomEdgeEffect.isFinished()) {
			canvas.save();
			if (mFlipView.isFlippingVertically()) {
				mBottomEdgeEffect.setSize(mFlipView.getWidth(), mFlipView.getHeight());
				canvas.rotate(180);
				canvas.translate(-mFlipView.getWidth(), -mFlipView.getHeight());
			} else {
				mBottomEdgeEffect.setSize(mFlipView.getHeight(), mFlipView.getWidth());
				canvas.rotate(90);
				canvas.translate(0, -mFlipView.getWidth());
			}
			needsMoreDrawing = mBottomEdgeEffect.draw(canvas);
			canvas.restore();
		}
		return needsMoreDrawing;
	}

	@Override
	public void overFlipEnded() {
		mTopEdgeEffect.onRelease();
		mBottomEdgeEffect.onRelease();
		mTotalOverFlip = 0;
	}

	@Override
	public float getTotalOverFlip() {
		return mTotalOverFlip;
	}

}
