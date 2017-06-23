package home.smart.fly.animations.customview.flipview;

import android.graphics.Canvas;

public interface OverFlipper {

	/**
	 * 
	 * @param flipDistance
	 *            the current flip distance
	 * 
	 * @param minFlipDistance
	 *            the minimum flip distance, usually 0
	 * 
	 * @param maxFlipDistance
	 *            the maximum flip distance
	 * 
	 * @return the flip distance after calculations
	 * 
	 */
	float calculate(float flipDistance, float minFlipDistance,
                    float maxFlipDistance);

	/**
	 * 
	 * @param v
	 *            the view to apply any drawing onto
	 * 
	 * @return a boolean flag indicating if the view needs to be invalidated
	 * 
	 */
	boolean draw(Canvas c);

	/**
	 * Triggered from a touch up or cancel event. reset and release state
	 * variables here.
	 */
	void overFlipEnded();

	/**
	 * 
	 * @return the total flip distance the has been over flipped. This is used
	 *         by the onOverFlipListener so make sure to return the correct
	 *         value.
	 */
	float getTotalOverFlip();

}
