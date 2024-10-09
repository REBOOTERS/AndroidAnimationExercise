package com.engineer.compose.viewmodel

import android.net.Uri
import java.io.Serializable

data class ImagePreviewPayload(val index: Int, val uris: List<String>) : Serializable

