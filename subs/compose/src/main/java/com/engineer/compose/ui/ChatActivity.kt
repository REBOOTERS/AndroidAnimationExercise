package com.engineer.compose.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.engineer.compose.R
import com.engineer.compose.ui.preview.ChatMessageItemPre
import com.engineer.compose.ui.preview.ChatUIWithKeyboard
import com.engineer.compose.ui.preview.ChatUIWithKeyboardPre
import com.engineer.compose.ui.preview.provideTestChat
import com.engineer.compose.ui.ui.theme.ChatPop
import com.engineer.compose.ui.ui.theme.ComposeAppTheme
import com.engineer.compose.uitls.KeyBoardUtil
import com.engineer.compose.viewmodel.ChatMessage
import com.engineer.compose.viewmodel.ChatViewModel

class ChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    ChatScreen(chatUIWithKeyboard = ChatUIWithKeyboard {
                        KeyBoardUtil.hideKeyboard(window.decorView)
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
    @PreviewParameter(ChatUIWithKeyboardPre::class, 1) chatUIWithKeyboard: ChatUIWithKeyboard
) {
    var inputValue by remember { mutableStateOf("") }
    val msg by viewModel.messageList.observeAsState(ArrayList<ChatMessage>())
    val msg1 by viewModel.messages.collectAsState()
    val temp = provideTestChat()
    val kk by viewModel.kkk.observeAsState("11")

    viewModel.messageList.observe(LocalLifecycleOwner.current) {
        if (it.size > 1) {
            Log.e("ChatListPage", "list = ${it[1].text}")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = kk, modifier = Modifier.size(1.dp))
        LazyColumn(

        ) {
            items(msg.size, key = { it }, contentType = {msg[it].sender}) { index ->
                ChatMessageItem(message = msg[index])
            }
        }
        // 消息列表
        MessageList(messages = msg1, modifier = Modifier.weight(1f))
        // 输入框和发送按钮
        InputArea(inputValue = inputValue, viewModel = viewModel, messageText = {
            inputValue = it
        }) {
            chatUIWithKeyboard.hideKeyboard()
        }
    }
}

@Composable
fun InputArea(
    inputValue: String,
    viewModel: ChatViewModel,
    messageText: (String) -> Unit,
    hideKeyboard: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value = inputValue,
            onValueChange = { messageText(it) },
            modifier = Modifier.weight(1f),
            label = { Text(text = "输入消息") },
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                if (inputValue.isNotEmpty()) {
                    sendMessage(viewModel, inputValue)
                    messageText("")
                    hideKeyboard()
                }
            })
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (inputValue.isNotEmpty()) {
                sendMessage(viewModel, inputValue)
                messageText("")
                hideKeyboard()
            }
        }) {
            Text("Send")
        }
    }
}

@Composable
fun MessageList(messages: List<ChatMessage>, modifier: Modifier) {
    Log.d("TAG_TAG", "msg $messages")
    LazyColumn(
        modifier = modifier
    ) {
        items(messages.size, key = { messages[it].text }) { index ->
            ChatMessageItem(message = messages[index])
        }
    }

}

@Preview
@Composable
fun ChatMessageItem(@PreviewParameter(ChatMessageItemPre::class, 2) message: ChatMessage) {
    Log.d("TAG_TAG", message.text)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (!message.isMine) {
            Spacer(modifier = Modifier.width(8.dp))
            ProfilePicture(R.drawable.user_bot, modifier = Modifier.align(Alignment.Top))
        }
        Spacer(modifier = Modifier.width(8.dp))
        // 聊天内容气泡
        ChatBubble(
            message = message.text,
            isMe = message.isMine,
            modifier = Modifier
                .align(Alignment.Top)
                .weight(1f)
        )
        if (message.isMine) {
            Spacer(modifier = Modifier.width(8.dp))
            // 自己的头像
            ProfilePicture(R.drawable.user_me, modifier = Modifier.align(Alignment.Top))
        }
    }
}

@Composable
fun ChatBubble(message: String, isMe: Boolean, modifier: Modifier = Modifier) {
    val color = if (isMe) ChatPop else Color.LightGray
    val shape = RoundedCornerShape(
        topStart = if (isMe) 8.dp else 0.dp,
        topEnd = if (isMe) 0.dp else 8.dp,
        bottomEnd = 8.dp,
        bottomStart = 8.dp
    )

    Surface(
        color = color, shape = shape, modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = message, modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ProfilePicture(resId: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(resId),
            contentDescription = "null",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}


fun sendMessage(viewModel: ChatViewModel, message: String) {
    viewModel.query(message)
//    viewModel.queryFlow(message)
}
