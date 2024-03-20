package com.mvsss.geminiaichatbot.myui

import android.graphics.Bitmap

sealed class ChatUiEvent {
    data class  UpdatePrompt ( val prompt : String) : ChatUiEvent()
    data class SendPrompt (val prompt:String , val bitmap : Bitmap?) : ChatUiEvent()
    data class UpdateBitmap(val bitmap: Bitmap)
}