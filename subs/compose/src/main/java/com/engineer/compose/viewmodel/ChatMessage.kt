package com.engineer.compose.viewmodel

data class ChatMessage(val sender: String, val text: String, val isMine: Boolean)

class ListState {
    val items: ArrayList<ChatMessage> = ArrayList()
}
