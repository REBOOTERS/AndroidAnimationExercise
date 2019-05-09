package home.smart.fly.animations;

import android.app.Activity;
import android.util.Log;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

/**
 * @author: zhuyongging
 * @since: 2019-05-09
 */
@RunWith(RobolectricTestRunner.class)
public class AppStartActivityUITest {
    private static final String TAG = "AppStartActivityUITest";
    private Activity mActivity;

    @Before
    public void setup() {
        mActivity = Robolectric.buildActivity(AppStartActivity.class).create().get();
        Log.e(TAG, "setup: " + mActivity);
    }


}
