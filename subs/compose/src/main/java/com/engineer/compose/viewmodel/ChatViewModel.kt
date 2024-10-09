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
            "天空呈现蓝色的原因主要与光的散射有关。当太阳光进入大气层后，大气中的气体分子和悬浮微粒会对阳光进行散射。散射的强度与光的波长有关，波长较短的紫光散射强度最大，其次是蓝光，而波长较长的红光和黄光的散射强度较弱。\n" +
                    "\n" +
                    "由于蓝光在散射过程中受到的散射强度较大，因此更容易向四面八方散射，这使得我们看到的天空呈现出蓝色。同时，由于人眼对紫光并不敏感，因此即使紫光散射强度最大，我们看到的天空也不会是紫色，而是蓝色。\n" +
                    "\n" +
                    "此外，天空的颜色也会随着时间和地点的变化而有所不同。在日出和日落时，由于阳光需要穿过更厚的大气层，散射效果更加显著，天空可能会呈现出橙色或红色。而在晴朗的天气下，由于大气中尘埃和水滴较少，散射作用相对较弱，天空则会呈现出深蓝色。\n" +
                    "\n" +
                    "总之，天空之所以是蓝色的，主要是由于大气对阳光中蓝光的强烈散射作用所致。".toCharArray()
        val sb = StringBuilder()

        Thread {
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

                _messageList.postValue(history)

                Thread.sleep(10)
            }
        }.start()
    }
}