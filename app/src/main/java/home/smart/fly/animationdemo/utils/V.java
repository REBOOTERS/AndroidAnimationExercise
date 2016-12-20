
/**
 * 
 */
package home.smart.fly.animationdemo.utils;

import android.app.Activity;
import android.view.View;

/**
 * @author rookie 简化findViewById 的一种思路
 */
public class V {

	/**
	 * activity.findViewById()
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static <T extends View> T f(Activity context, int id) {
		return (T) context.findViewById(id);
	}

	/**
	 * rootView.findViewById()
	 * 
	 * @param rootView
	 * @param id
	 * @return
	 */

	public static <T extends View> T f(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

}
