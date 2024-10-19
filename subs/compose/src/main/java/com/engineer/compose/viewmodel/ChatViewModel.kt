package com.engineer.compose.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

        val subThread = true
        if (subThread) {
            Thread {
                sendData(sb, mockResponse, subThread)
            }.start()
        } else {
            sendData(sb, mockResponse, subThread)
        }

//

    }

    private fun sendData(sb: StringBuffer, mockResponse: CharArray, subThread: Boolean) {

        for (c in mockResponse) {
            val history = _messageList.value ?: ArrayList()
            val lastMsg = history.last()
            sb.append(c)
            kkk.postValue(sb.toString())
            if (lastMsg.sender == "Bot") {
                val newMsg = ChatMessage("Bot", sb.toString(), false)
                history[history.size - 1] = newMsg
            } else {
                val newMsg = ChatMessage("Bot", sb.toString(), false)
                history.add(newMsg)
            }
            Log.d(TAG, "history $history")

            if (subThread) {
                _messageList.postValue(history)
                Thread.sleep(10)
            } else {
                _messageList.value = history
            }
        }
    }
}