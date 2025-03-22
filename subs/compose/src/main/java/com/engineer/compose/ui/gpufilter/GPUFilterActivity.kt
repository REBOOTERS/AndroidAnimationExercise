package com.engineer.compose.ui.gpufilter

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineer.compose.ui.gpufilter.ui.theme.AndroidAnimationExerciseTheme
import com.pixpark.gpupixel.GPUPixel
import com.pixpark.gpupixel.GPUPixelSourceImage
import com.pixpark.gpupixel.filter.GPUPixelFilter

class GPUFilterActivity : ComponentActivity() {
    private lateinit var gpuPixelSourceImage: GPUPixelSourceImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val url = intent.getStringExtra("url") ?: ""

        gpuPixelSourceImage = GPUPixelSourceImage(BitmapFactory.decodeFile(url))

        setContent {
            AndroidAnimationExerciseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Log.i("", innerPadding.toString())
                    PhotoFilterScreen(url) {
                        Log.i("zyq", "filter is $it")
                        val result = it.replace(" ", "")
//                        val gpuFilter = GPUPixelFilter(result)
//                        gpuPixelSourceImage.removeAllTargets()

//                        gpuPixelSourceImage.addTarget(gpuFilter)
                    }
                }
            }
        }
        GPUPixel.setContext(this)
    }
}

@Composable
fun PhotoFilterScreen(url: String, cb: (String) -> Unit) {
    var selectedFilter by remember { mutableStateOf(Filter("Color Invert Filter", "Inverts image colors")) }
    val imageModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
        .clip(RoundedCornerShape(8.dp))

    Column(modifier = Modifier.fillMaxSize()) {
//        TopAppBar(
//            title = { Text("photo filter") },
//            navigationIcon = {
//                IconButton(onClick = { /* 返回逻辑 */ }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                }
//            },
//            backgroundColor = MaterialTheme.colors.primary,
//            contentColor = Color.White
//        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Gray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            val bitmap = BitmapFactory.decodeFile(url)
            val bitmapPainter = BitmapPainter((bitmap.asImageBitmap()))
            Image(
                painter = bitmapPainter,
                contentDescription = "Filtered Image",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            val filters = listOf("Blur", "Brightness", "CircleCrop", "ColorFilter", "Contrast")
            items(filters) { filter ->
                FilterItem(filter.name, isSelected = selectedFilter == filter) {
                    selectedFilter = filter
                    cb(selectedFilter.name)
                }
            }
        }
    }
}

data class Filter(val name: String, val desc: String)

val filters = listOf(
    Filter("Color Invert Filter", "Inverts image colors"),
    Filter("Color Matrix Filter", "Applies color matrix transformation"),
    Filter("Crosshatch Filter", "Creates crosshatch drawing effect"),
    Filter("Emboss Filter", "Creates embossed effect"),
    Filter("Glass Sphere Filter", "Spherical glass distortion effect"),
    Filter("Halftone Filter", "Creates halftone pattern effect"),
    Filter("Pixellation Filter", "Creates pixelated effect"),
    Filter("Posterize Filter", "Reduces image to limited colors"),
    Filter("Sketch Filter", "Creates sketch-like effect"),
    Filter("Smooth Toon Filter", "Cartoon effect with smoothing"),
    Filter("Sphere Refraction Filter", "Spherical refraction effect"),
    Filter("Toon Filter", "Cartoon/comic effect")
)

@Composable
fun FilterItem(filterName: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.Blue else Color.LightGray), contentAlignment = Alignment.Center
        ) {
            Text(filterName.first().toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Text(filterName, fontSize = 12.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidAnimationExerciseTheme {
        PhotoFilterScreen("") {}
    }
}