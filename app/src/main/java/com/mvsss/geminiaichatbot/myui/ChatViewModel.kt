package com.mvsss.geminiaichatbot.myui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvsss.geminiaichatbot.data.Chat
import com.mvsss.geminiaichatbot.data.ChatData
import com.mvsss.geminiaichatbot.getJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel()  : ViewModel(){

    private val _chatState = MutableStateFlow(getJson.jsonObject?.getChatState() ?: ChatState())
    val chatState = _chatState.asStateFlow()

    fun onEvent ( event : ChatUiEvent){

        when(event){
            is ChatUiEvent.SendPrompt->{
                if(event.prompt.isNotEmpty()){
                    addPrompt(event.prompt ,event.bitmap)
                    if(event.bitmap == null){
                        generateChat(event.prompt)
                        Log.d("thisthat" , "generating with out image")
                    }
                    else{
                        generateChatImg(event.prompt , event.bitmap)
                        Log.d("thisthat" , "generating with image")

                    }

                }
            }

            is ChatUiEvent.UpdatePrompt -> {

                _chatState.update {
                    it.copy(
                        prompt = event.prompt
                    )
                }

            }


        }
    }



    private fun generateChatImg(prompt: String, bitmap: Bitmap) {

        viewModelScope.launch {
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, ChatData.getResponse(prompt,bitmap))
                    },
                    prompt = "",
                    bitmap = null
                )

            }
            getJson.jsonObject?.saveChatState(_chatState.value)


        }

    }

    private fun generateChat(prompt: String) {

        viewModelScope.launch {
                _chatState.update {
                    it.copy(
                        chatList = it.chatList.toMutableList().apply {
                            add(0, ChatData.getResponse(prompt))
                        },
                    prompt = "",
                    bitmap = null
                    )

                }

            getJson.jsonObject?.saveChatState(_chatState.value)
            Log.d("double", "hi from generate function")

        }

    }


    private fun addPrompt(prompt: String, bitmap: Bitmap?) {

        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add( 0, Chat(prompt,bitmap,true))
                },
                prompt = "",
                bitmap = null
            )
        }
        getJson.jsonObject?.saveChatState(_chatState.value)
        Log.d("double", "hi from addPrompt function")


    }




}