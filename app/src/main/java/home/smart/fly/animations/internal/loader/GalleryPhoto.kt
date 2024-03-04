package home.smart.fly.animations.internal.loader

import android.net.Uri

data class GalleryPhoto(
    val uri: Uri, val path: String, val size: String, val mimeType: String, val dateAdd: String
)
