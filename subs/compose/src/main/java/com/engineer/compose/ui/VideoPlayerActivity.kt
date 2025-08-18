package com.engineer.compose.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.engineer.compose.ui.ui.theme.ComposeAppTheme
import kotlinx.coroutines.delay
import androidx.core.net.toUri
import java.util.Locale

class VideoPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val videoUri = intent.getStringExtra("video_uri")
        setContent {
            ComposeAppTheme {
                if (videoUri != null) {
                    VideoPlayer(videoUri.toUri())
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(videoUri: Uri) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var videoSize by remember { mutableStateOf(IntSize.Zero) }
    var duration by remember { mutableIntStateOf(0) }
    var progress by remember { mutableIntStateOf(0) }

    val activity = LocalActivity.current

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            progress = mediaPlayer.currentPosition
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                SurfaceView(context).apply {
                    holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            mediaPlayer.apply {
                                setDataSource(context, videoUri)
                                setDisplay(holder)
                                prepareAsync()
                                setOnPreparedListener {
                                    videoSize = IntSize(it.videoWidth, it.videoHeight)
                                    duration = it.duration
                                    isPlaying = true
                                    it.isLooping = true
                                    it.start()
                                }
                            }
                        }

                        override fun surfaceChanged(
                            holder: SurfaceHolder,
                            format: Int,
                            width: Int,
                            height: Int
                        ) {
                        }

                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                            isPlaying = false
                            mediaPlayer.stop()
                            mediaPlayer.release()
                        }
                    })
                }
            },
            modifier = Modifier
                .aspectRatio(if (videoSize.height > 0) videoSize.width.toFloat() / videoSize.height.toFloat() else 1f)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = formatTime(progress), color = Color.White)
                IconButton(
                    onClick = {
                        if (isPlaying) {
                            mediaPlayer.pause()
                        } else {
                            mediaPlayer.start()
                        }
                        isPlaying = !isPlaying
                    }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White
                    )
                }
                Text(text = formatTime(duration), color = Color.White)
            }
        }
    }

    DisposableEffect(Unit) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            mediaPlayer.release()
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

private fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}