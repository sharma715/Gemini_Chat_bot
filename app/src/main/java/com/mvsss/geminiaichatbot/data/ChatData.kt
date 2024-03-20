package com.mvsss.geminiaichatbot.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {

    const val api_key = "AIzaSyDNg7ew63erFnfQQMGP4t6UisibbTqDNrE"

    val generativeModel = GenerativeModel(apiKey = api_key, modelName = "gemini-pro", generationConfig = generationConfig{temperature = 0.3f})
    val generativeModelImg = GenerativeModel(apiKey = api_key, modelName = "gemini-pro-vision" , generationConfig = generationConfig{temperature = 0.4f})


    suspend fun getResponse(prompt: String): Chat {

        try {

            val answer = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt).text
            }

            return Chat(prompt = answer ?: "error", bitmap = null, isFromUser = false)

        } catch (e: Exception) {
            return Chat(prompt = e.message ?: "error", bitmap = null, isFromUser = false)
        }


    }


    suspend fun getResponse(prompt: String, bitmap: Bitmap): Chat {

        try {

            val input = content {
                image(bitmap)
                text(prompt)
            }

            val answer = withContext(Dispatchers.IO) {
                generativeModelImg.generateContent(input).text
            }
            return Chat(prompt = answer ?: "error", bitmap = null, isFromUser = false)

        } catch (e: Exception) {
            return Chat(prompt = e.message ?: "error", bitmap = null, isFromUser = false)
        }
    }



}