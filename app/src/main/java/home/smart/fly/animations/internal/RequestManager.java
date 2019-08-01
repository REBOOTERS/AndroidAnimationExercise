package home.smart.fly.animations.internal;

import android.util.Log;
import org.jetbrains.annotations.Nullable;

/**
 * @author rookie
 * @since 07-29-2019
 */
public class RequestManager {

    public void load(String string) {
        Log.e("zyq", string);
    }


    private void load1(String string, String a) {
        Log.e("zyq", a);
        Log.e("zyq", string);
    }

    public String load2() {
        DarkWorld darkWorld = new DarkWorld();
        darkWorld.log();
        darkWorld.test();
        int a = darkWorld.a;

        Log.e("zyq", "111");
        return "111";


    }

}
