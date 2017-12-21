package home.smart.fly.animations.master;

import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2017/12/10
 * desc   :
 * version: 1.0
 */
public class Tools {
    public static PorterDuff.Mode intToMode(int val) {
        switch (val) {
            default:
            case 0:
                return Mode.CLEAR;
            case 1:
                return Mode.SRC;
            case 2:
                return Mode.DST;
            case 3:
                return Mode.SRC_OVER;
            case 4:
                return Mode.DST_OVER;
            case 5:
                return Mode.SRC_IN;
            case 6:
                return Mode.DST_IN;
            case 7:
                return Mode.SRC_OUT;
            case 8:
                return Mode.DST_OUT;
            case 9:
                return Mode.SRC_ATOP;
            case 10:
                return Mode.DST_ATOP;
            case 11:
                return Mode.XOR;
            case 16:
                return Mode.DARKEN;
            case 17:
                return Mode.LIGHTEN;
            case 13:
                return Mode.MULTIPLY;
            case 14:
                return Mode.SCREEN;
            case 12:
                return Mode.ADD;
            case 15:
                return Mode.OVERLAY;
        }
    }
}
