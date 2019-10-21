package home.smart.fly.animations;

import android.content.Context;

import com.engineer.dateview.api.DataView;
import com.engineer.dateview.interfaces.MyActivityLifecycleCallbacks;

/**
 * @author: Rookie
 * @date: 2018-10-12
 * @desc
 */
public class DebugMyApplication extends MyApplication {

    String TAG = "ActLifecycleCallbacks";
    private MyActivityLifecycleCallbacks mMyActivityLifecycleCallbacks;

    /**
     * 监听 Activity 的生命周期
     */
    @Override
    public void logLifeCycleCallBacks() {

        DataView.init(this);

        mMyActivityLifecycleCallbacks = new MyActivityLifecycleCallbacks();
        unregisterActivityLifecycleCallbacks(mMyActivityLifecycleCallbacks);
        registerActivityLifecycleCallbacks(mMyActivityLifecycleCallbacks);
    }


    public Context provideContext() {
        return mMyActivityLifecycleCallbacks.mGodContext;
    }
}
