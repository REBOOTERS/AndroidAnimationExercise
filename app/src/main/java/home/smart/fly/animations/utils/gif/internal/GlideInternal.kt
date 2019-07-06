package home.smart.fly.animations.utils.gif.internal

import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.FutureTarget
import org.jetbrains.annotations.Nullable
import java.io.File

/**
 * @author rookie
 * @since 07-06-2019
 */

object GlideInternal {

    internal fun load(context: Context, @Nullable string: String?): FutureTarget<GifDrawable> {
        return Glide.with(context).asGif().load(string).submit()
    }

    internal fun load(context: Context, @Nullable uri: Uri?): FutureTarget<GifDrawable> {
        return Glide.with(context).asGif().load(uri).submit()
    }

    internal fun load(context: Context, @Nullable file: File?): FutureTarget<GifDrawable> {
        return Glide.with(context).asGif().load(file).submit()
    }

    internal fun load(context: Context, @RawRes @DrawableRes @Nullable resourceId: Int?): FutureTarget<GifDrawable> {
        return Glide.with(context).asGif().load(resourceId).submit()
    }
}