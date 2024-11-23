package com.engineer.compose.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val TAG = "ComposeView"


@Preview
@Composable
fun DemoCard() {
    var count by remember { mutableIntStateOf(0) }
    var list by remember { mutableStateOf(ArrayList<String>()) }
    val list2 = remember { mutableStateListOf<String>() }
    val list3 = remember { mutableStateListOf("") }
    val count1 = { mutableIntStateOf(0) }
    val count2 = { mutableStateOf(0) }
    var list4 = remember { mutableStateOf(ArrayList<String>())}




    Log.i(TAG,"count -> ${count::class}")
    Log.i(TAG,"list  -> ${list::class}")
    Log.i(TAG,"list2 -> ${list2::class}")
    Log.i(TAG,"list3 -> ${list3::class}")
    Log.i(TAG,"list4 -> ${list4::class}")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
//        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "$list")
//        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "${list4.value}")
//        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Blue, text = "$count")
        ContentA(list)
        ContentA(list2.toList())
        ContentA(list3.toList())
        ContentA(list4.value)
//        ContentB(count)
//        ContentC(list, count)

        val context = LocalContext.current
        Button(modifier = Modifier.padding(5.dp), onClick = {
            Toast.makeText(context, "you clicked me", Toast.LENGTH_SHORT).show()
            count++
            list.add(count.toString())
            list2.add(count.toString())
            list3.add(count.toString())
            list4.value = ArrayList(list)
        }) {
            Text(text = "click me")
        }
    }
}

@Composable
fun ContentA(list: List<String>) {
    Log.d(TAG, "ContentA() called with: list = $list")
    Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "$list")
}

@Composable
fun ContentB(num: Int) {
    Log.d(TAG, "ContentB() called with: num = $num")
    Text(modifier = Modifier.padding(start = 10.dp), color = Color.Blue, text = num.toString())
}

@Composable
fun ContentD(list: List<String>) {
    Log.d(TAG, "ContentD() called with: list = $list")
    Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "$list")
}


@Composable
fun ContentC(list: ArrayList<String>, num: Int) {
    Log.d(TAG, "ContentC() called with: list = $list, num = $num")
    Column {
        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Red, text = "$list")
        Text(modifier = Modifier.padding(start = 10.dp), color = Color.Blue, text = num.toString())
    }
}