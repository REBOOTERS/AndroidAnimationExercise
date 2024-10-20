package com.engineer.compose.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val TAG = "ChatViewModel"

    val handler = Handler(Looper.getMainLooper())

    private val _messageList = MutableLiveData<ArrayList<ChatMessage>>()


    val messageList: LiveData<ArrayList<ChatMessage>> = _messageList

    val kkk = MutableLiveData<String>()
    fun query(userQuery: String) {
        val history = _messageList.value ?: ArrayList()
        val userMsg = ChatMessage("IAM四十二", userQuery, true)
        history.add(userMsg)
        _messageList.value = history

        mockRequestResult()

    }

    val chatBuffer = StringBuilder()
    fun queryResult(response: String) {
        val history = _messageList.value ?: ArrayList()
        val lastMsg = history.last()
        chatBuffer.append(response)
        if (lastMsg.sender == "Bot") {
            val newMsg = ChatMessage("Bot", chatBuffer.toString(), false)
            history[history.size - 1] = newMsg
        } else {
            val newMsg = ChatMessage("Bot", chatBuffer.toString(), false)
            history.add(newMsg)
        }
        Log.d(TAG, "history $history")

        _messageList.postValue(history)
    }

    private fun mockRequestResult() {
        val mockResponse =
            "天空呈现蓝色的原因主要与光的散射有关。当太阳光进入大气层后，大气中的气体分子和悬浮微粒会对阳光进行散射".toCharArray()
        val sb = StringBuffer()
        sendData(sb, mockResponse)
    }

    private fun sendData(sb: StringBuffer, mockResponse: CharArray) {
        viewModelScope.launch {
            for (c in mockResponse) {
                val history = _messageList.value ?: ArrayList()
                val lastMsg = history.last()
                sb.append(c)
                kkk.postValue(sb.toString())
                if (lastMsg.sender == "Bot") {
                    val newMsg = ChatMessage("Bot", sb.toString(), false)
                    history[history.size -1 ] = newMsg

                    _messageList.value = ArrayList(history)

                } else {
                    val newMsg = ChatMessage("Bot", sb.toString(), false)
                    history.add(newMsg)
                    _messageList.value = history
                }
//                _messageList.postValue(history)

                delay(10)
                Log.d(TAG, "history ${_messageList.value}")
            }
        }


    }

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun addMessage(message: ChatMessage) {
        _messages.value = _messages.value + message
    }

    // 更新特定 index 的消息
    fun updateMessageAt(index: Int, newMessage: ChatMessage) {
        _messages.value = _messages.value.toMutableList().apply {
            if (index in indices) {
                this[index] = newMessage
            }
        }
    }

    // 模拟流式添加消息
    fun startReceivingMessages() {
        var i = 0
        val mockResponse =
            "天空呈现蓝色的原因主要与光的散射有关。当太阳光进入大气层后，大气中的气体分子和悬浮微粒会对阳光进行散射".toCharArray()
        val sb = StringBuffer()



        viewModelScope.launch {
            for (c in mockResponse) {
                val history = _messages.value ?: ArrayList()
                val lastMsg = history.last()
                sb.append(c)
                kkk.postValue(sb.toString())
                if (lastMsg.sender == "Bot") {
                    val newMsg = ChatMessage("Bot", sb.toString(), false)
                    updateMessageAt(history.size - 1, newMsg)
                } else {
                    val newMsg = ChatMessage("Bot", sb.toString(), false)
                    addMessage(newMsg)
                }
                Log.d(TAG, "history $history")
                delay(20)
            }


        }
    }

    fun queryFlow(userQuery: String) {
        val userMsg = ChatMessage("IAM四十二", userQuery, true)
        addMessage(userMsg)
        startReceivingMessages()
    }
}