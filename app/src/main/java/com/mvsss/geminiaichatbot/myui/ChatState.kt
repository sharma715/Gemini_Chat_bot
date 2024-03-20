package com.mvsss.geminiaichatbot.myui

import android.app.VoiceInteractor.Prompt
import android.graphics.Bitmap
import com.mvsss.geminiaichatbot.data.Chat

data class ChatState(
    val chatList  : MutableList<Chat> = mutableListOf(),
    var bitmap : Bitmap? = null,
    val prompt: String = ""
    )