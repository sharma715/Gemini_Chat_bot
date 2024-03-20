package com.mvsss.geminiaichatbot.data

import android.graphics.Bitmap

data class Chat (
    val prompt : String,
    var bitmap : Bitmap? = null,
    val isFromUser : Boolean = false,
    var hadBitmap: Boolean = false
)