package com.engineer.compose.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.engineer.compose.R
import com.engineer.compose.ui.ui.theme.MiniAppTheme
import com.engineer.compose.viewmodel.ImagePreviewPayload

/**
 * https://developer.android.google.cn/jetpack/compose/tutorial
 */
private const val TAG = "MainComposeActivity"

class MainComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    MessageCard(Message("lucy", "hello world"))
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun SelectImageButton() {
//    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current
    val pickImagesLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents(), onResult = { uris ->
            Log.i(TAG, "uris = $uris")
            val intent = Intent(context, GalleryActivity::class.java)
            val list = uris.map { it.toString() }
            val payload = ImagePreviewPayload(2, list)
            intent.putExtra("payload", payload)
            context.startActivity(intent)
        })
    Button(
        onClick = { pickImagesLauncher.launch("image/*") },
    ) {
        Text(text = "Pick Images from Gallery")
    }

}

@Composable
fun MessageCard(msg: Message) {
    var text by rememberSaveable { mutableStateOf("") }
    val count = remember { mutableIntStateOf(0) }
    Column {
        CircularProgressIndicator()
        NetImage()
        val context = LocalContext.current
        Button(onClick = {
            Toast.makeText(context, "you clicked me", Toast.LENGTH_SHORT).show()
            count.intValue++
        }) {
            Text(text = "click me")
        }
        Button(onClick = {
            context.startActivity(Intent(context, ChatActivity::class.java))
        }) {
            Text(text = "open Chat")
        }
        Button(onClick = {
            context.startActivity(Intent(context, GalleryActivity::class.java))
        }) {
            Text(text = "open gallery")
        }
        SelectImageButton()
        Text(text = "${count.intValue}")
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
                Text(text = msg.author)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = msg.body)
            }
        }

        OutlinedTextField(value = text,
            onValueChange = { text = it },
            label = { Text("输入消息") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )
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
    val msg = Message("mike", "where are you from")
    MessageCard(msg)
}