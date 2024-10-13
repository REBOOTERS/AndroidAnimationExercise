package com.engineer.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.engineer.compose.ui.ui.theme.ComposeAppTheme

/**
 * https://developer.android.google.cn/jetpack/compose/tutorial
 */
private const val TAG = "MainComposeActivity"

class MainComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    MessageCard(Message("lucy", "hello world"))
                }
            }
        }
    }
}
