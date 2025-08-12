package com.engineer.compose.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.engineer.compose.R
import com.engineer.compose.viewmodel.ImagePreviewPayload
import kotlin.math.roundToInt

private const val TAG = "ComposeView"

data class Message(val author: String, val body: String)

@Composable
fun SelectImageButton() {
//    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current
    val pickImagesLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents(),
            onResult = { uris ->
                Log.i("tag", "uris = $uris")
                val intent = Intent(context, GalleryActivity::class.java)
                val list = uris.map { it.toString() }
                val payload = ImagePreviewPayload(2, list)
                intent.putExtra("payload", payload)
                context.startActivity(intent)
            })
    Button(
        modifier = Modifier.padding(5.dp),
        onClick = { pickImagesLauncher.launch("image/*") },
    ) {
        Text(text = "Pick Images from Gallery")
    }

}

@Composable
fun SelectVideoButton() {
    val context = LocalContext.current
    val pickVideoLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("video_uri", uri.toString())
            context.startActivity(intent)
        }
    }
    Button(
        modifier = Modifier.padding(5.dp),
        onClick = { pickVideoLauncher.launch("video/*") },
    ) {
        Text(text = "Pick Video")
    }
}

@Composable
fun MessageCard(msg: Message) {
    var text by rememberSaveable { mutableStateOf("") }
    var count by remember { mutableIntStateOf(0) }
    val list by remember { mutableStateOf(ArrayList<String>()) }
    val kk = remember { mutableStateListOf<String>() }

    ScrollableList(list)
    ScrollableList(kk.toList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CircularProgressIndicator()
//        NetImage()
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {
            SelectImageButton()
            SelectVideoButton()
            Button(modifier = Modifier.padding(5.dp), onClick = {
                Toast.makeText(context, "you clicked me", Toast.LENGTH_SHORT).show()
                count++
                list.add(count.toString())
                if (kk.toList().size > 3) {
                    kk[3] = count.toString()
                } else {
                    kk.add(count.toString())
                }
            }) {
                Text(text = "click me")
            }
            Button(modifier = Modifier.padding(5.dp), onClick = {
                context.startActivity(Intent(context, ChatActivity::class.java))
            }) {
                Text(text = "open Chat")
            }
            Button(modifier = Modifier.padding(5.dp), onClick = {
                context.startActivity(Intent(context, GalleryActivity::class.java))
            }) {
                Text(text = "open gallery")
            }
        }
        Text(modifier = Modifier.padding(start = 10.dp), text = "$count")
        Text(modifier = Modifier.padding(start = 10.dp), text = "$list")
        Content1(list)
        Content11(kk.toList())
        Content2(count)
        Content3(list, count)
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = "null",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
                    Text(
                        text = msg.body,
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        OutlinedTextField(value = text,
            onValueChange = { text = it },
            label = { Text("输入消息") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }
        Box(modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .requiredSize(100.dp)
            .background(Color.Cyan)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                })
            .draggable(orientation = Orientation.Vertical, state = rememberDraggableState { delta ->
                offsetY += delta
            })
            .clickable {
                Toast
                    .makeText(context, "hh", Toast.LENGTH_SHORT)
                    .show()
            })
        Text(
            modifier = Modifier.padding(5.dp),
            text = "State可以让Compose感知到界面上有状态发生了变化，从而对界面上相关联的Composable函数进行重组。不仅如此，State还可以让Compose能够精准只更新那些状态有变化的控件，而那些状态没有变化的控件在重组的时候则会跳过执行。",
        )
    }
}

@Composable
fun Content1(list: List<String>) {
    Log.d(TAG, "Content1() called with: list = $list")
    Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "$list")
}

@Composable
fun Content11(list: List<String>) {
    Log.d(TAG, "Content11() called with: list = $list")
    Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "$list")
}

@Composable
fun Content2(num: Int) {
    Log.d(TAG, "Content2() called with: num = $num")
    Text(modifier = Modifier.padding(start = 10.dp), color = Color.Blue, text = num.toString())
}

@Composable
fun Content3(list: ArrayList<String>, num: Int) {
    Log.d(TAG, "Content3() called with: list = $list, num = $num")
    Column {
        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Green, text = num.toString())
        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Green, text = "$list")
    }
}

@Composable
fun ScrollableList(msg: List<String>) {
    Log.d(TAG, "ScrollableList() called with: msg = $msg")
    LazyColumn {
        items(msg.size, key = { it }) { i ->
            Text(
                text = "item_${msg[i]}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(20.dp)
            )
        }
    }
}

@Composable
fun NetImage() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.profile_picture)
    val painter = BitmapPainter(bitmap.asImageBitmap(), srcSize = IntSize(100, 100))
    AsyncImage(
        modifier = Modifier.padding(8.dp),
        placeholder = painter,
        model = "https://t7.baidu.com/it/u=1102661851,2954934733&fm=193&f=GIF",
        contentDescription = "net img"
    )
}

@Preview
@Composable
fun Pre() {
    Column {
        val msg = Message("mike", "where are you from")
        MessageCard(msg)
    }


}