package home.smart.fly.animations.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import java8.util.Optional;

/**
 * @authro: Rookie
 * @since: 2019-01-13
 */
public class PaletteUtils {

    private final static Palette.Swatch DEFAULT_SWATCH = new Palette.Swatch(0xFFC8C8C8, 1);

    //region internal
    @Nullable
    private static Palette.Swatch getSwatchInternal(final Palette pPalette, @SwatchStyle final int... pSwatchStyle) {
        for (final int style : pSwatchStyle) {
            switch (style) {
                case SwatchStyle.Vibrant:
                    if (pPalette.getVibrantSwatch() != null) {
                        return pPalette.getVibrantSwatch();
                    } else {
                        break;
                    }
                case SwatchStyle.LightVibrant:
                    if (pPalette.getLightVibrantSwatch() != null) {
                        return pPalette.getLightVibrantSwatch();
                    } else {
                        break;
                    }
                case SwatchStyle.DarkVibrant:
                    if (pPalette.getDarkVibrantSwatch() != null) {
                        return pPalette.getDarkVibrantSwatch();
                    } else {
                        break;
                    }
                case SwatchStyle.Muted:
                    if (pPalette.getMutedSwatch() != null) {
                        return pPalette.getMutedSwatch();
                    } else {
                        break;
                    }
                case SwatchStyle.LightMuted:
                    if (pPalette.getLightMutedSwatch() != null) {
                        return pPalette.getLightMutedSwatch();
                    } else {
                        break;
                    }
                case SwatchStyle.DarkMuted:
                    if (pPalette.getDarkMutedSwatch() != null) {
                        return pPalette.getDarkMutedSwatch();
                    } else {
                        break;
                    }
                default:
                    break;
            }
        }

        return DEFAULT_SWATCH;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SwatchStyle.Vibrant, SwatchStyle.LightVibrant, SwatchStyle.DarkVibrant, SwatchStyle.Muted, SwatchStyle
            .LightMuted, SwatchStyle.DarkMuted})
    public @interface SwatchStyle {

        int Vibrant = 0x01;

        int LightVibrant = 0x02;

        int DarkVibrant = 0x03;

        int Muted = 0x04;

        int LightMuted = 0x05;

        int DarkMuted = 0x06;
    }
    //endregion


    public static int getMagicColor(Resources resources, int id) {
        Palette.Builder builder = new Palette.Builder(BitmapFactory.decodeResource(resources, id));
        Palette palette = builder.generate();
        return Optional.ofNullable(palette)
                .map(PaletteUtils::getSwatch)
                .map(Palette.Swatch::getRgb)
                .orElseGet(() -> 0);
    }

    public static int getMagicColor(Bitmap bitmap) {
        Palette.Builder builder = new Palette.Builder(bitmap);
        Palette palette = builder.generate();
        return Optional.ofNullable(palette)
                .map(PaletteUtils::getSwatch)
                .map(Palette.Swatch::getRgb)
                .orElseGet(() -> 0);
    }

    public static Palette.Swatch getSwatch(Palette palette) {
        return getSwatchInternal(palette, SwatchStyle.LightMuted, SwatchStyle.Muted, SwatchStyle.DarkMuted,
                SwatchStyle.LightVibrant, SwatchStyle.Vibrant, SwatchStyle.DarkVibrant);
    }
}
