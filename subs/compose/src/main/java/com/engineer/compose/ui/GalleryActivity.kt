package com.engineer.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.engineer.compose.ui.ui.theme.MiniAppTheme
import com.engineer.compose.viewmodel.ImagePreviewPayload
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

class GalleryActivity : ComponentActivity() {
    private var payload: ImagePreviewPayload? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val temp = intent?.getSerializableExtra("payload")
        if (temp is ImagePreviewPayload) {
            payload = temp
        }


        setContent {
            MiniAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    val sampleImages = if (payload != null) payload!!.uris else listOf(
                        "https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif",
                        "https://media.giphy.com/media/l0HlNaQ6gWfllcjDO/giphy.gif",
                        "https://media.giphy.com/media/xT0GqFzHj1U25N3Rhe/giphy.gif",
                        "https://media.giphy.com/media/l0HUqsz2jdQYElRm0/giphy.gif",
                        "https://media.giphy.com/media/3oEduQAsYcJKQH2XsI/giphy.gif",
                        "https://via.placeholder.com/600/92c952",
                        "https://img.zcool.cn/community/010a6b5922a03eb5b3086ed4b5cd3a.gif",
                        "https://via.placeholder.com/600/24f355",
                        "https://via.placeholder.com/600/d32776",
                        "https://via.placeholder.com/600/f66b97"
                    )
                    ViewPagerExample(sampleImages, payload?.index ?: 0, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPagerExample(imageUrls: List<String>, initialPage: Int = 0, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(initialPage = initialPage)
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).crossfade(true).components {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    Column {
        HorizontalPager(
            count = imageUrls.size, state = pagerState, modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
//                val painter = rememberAsyncImagePainter(model = imageUrls[page])
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context).data(imageUrls[page])
                        .size(Size.ORIGINAL) // Set the size to ORIGINAL to load the full resolution
                        .build(), imageLoader = imageLoader
                )
//                Image(
//                    painter = painter,
//                    contentDescription = "Image $page",
//                    contentScale = ContentScale.Inside,
//                    modifier = Modifier.fillMaxSize()
//                )
                Image(
                    painter = painter,
                    contentDescription = "Image $page",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9.0f / 16)
//                        .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                )
            }
        }

        // Optional: Add some pager indicators or tabs here
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
        PageIndicator(
            currentPage = pagerState.currentPage, pageCount = pagerState.pageCount
        )
    }
}

@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${currentPage + 1} / $pageCount",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: com.google.accompanist.pager.PagerState,
    modifier: Modifier = Modifier,
) {
    val indicatorColor = Color.Gray
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = modifier
    ) {
        for (i in 0 until pagerState.pageCount) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (i == pagerState.currentPage) 12.dp else 8.dp)
                    .background(color = indicatorColor, shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewPagerPreview() {
    val sampleImages = listOf(
        "https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif",
        "https://media.giphy.com/media/l0HlNaQ6gWfllcjDO/giphy.gif",
        "https://media.giphy.com/media/xT0GqFzHj1U25N3Rhe/giphy.gif",
        "https://media.giphy.com/media/l0HUqsz2jdQYElRm0/giphy.gif",
        "https://media.giphy.com/media/3oEduQAsYcJKQH2XsI/giphy.gif",
        "https://via.placeholder.com/600/92c952",
        "https://img.zcool.cn/community/010a6b5922a03eb5b3086ed4b5cd3a.gif",
        "https://via.placeholder.com/600/24f355",
        "https://via.placeholder.com/600/d32776",
        "https://via.placeholder.com/600/f66b97"
    )
    ViewPagerExample(sampleImages, 2)
}