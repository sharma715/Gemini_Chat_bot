import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.mvsss.geminiaichatbot.data.Chat
import com.mvsss.geminiaichatbot.data.ChatData
import com.mvsss.geminiaichatbot.myui.ChatState

class JSON(private var context: Context) {

     private var newChatState : ChatState = ChatState()

    init {
        newChatState = getChatState() ?: ChatState()
    }

    fun saveChatState( chatState: ChatState) {
        val gson = Gson()
        try{
            var latestChat : Chat = chatState.chatList[0]

            if(latestChat.bitmap == null){
                Log.d("double" ,"inside if")
                newChatState.chatList.add(0,latestChat)
            }
            else{
                Log.d("double" ,"inside else")
                newChatState.chatList.add(0,latestChat.copy(bitmap = null, hadBitmap = true))

            }
                val json = gson.toJson(newChatState)
                val sharedPreferences = context.getSharedPreferences("chat_state", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("chat_state", json).apply()


        }
        catch ( e : IndexOutOfBoundsException){
            val json = gson.toJson(chatState)
            val sharedPreferences = context.getSharedPreferences("chat_state", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("chat_state", json).apply()

        }
        catch(e : Exception){
            Log.d("json error",e.toString())
        }

    }

    fun getChatState(): ChatState? {

        try{
            val gson = Gson()
            val sharedPreferences = context.getSharedPreferences("chat_state", Context.MODE_PRIVATE)
            val json = sharedPreferences.getString("chat_state", null)
            return gson.fromJson(json, ChatState::class.java)

        }
        catch( e: Exception){
            Log.d("json error" ,e.toString())
            return null
        }

    }
}
