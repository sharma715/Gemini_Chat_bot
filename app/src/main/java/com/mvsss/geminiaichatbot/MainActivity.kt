package com.mvsss.geminiaichatbot
import JSON
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.mvsss.geminiaichatbot.myui.ChatState
import com.mvsss.geminiaichatbot.myui.ChatUiEvent
import com.mvsss.geminiaichatbot.myui.ChatViewModel
import com.mvsss.geminiaichatbot.myui.MyScaffold
import com.mvsss.geminiaichatbot.ui.theme.GeminiAIChatBotTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.*

class MainActivity : ComponentActivity() {


    private val uriState = MutableStateFlow("")
    private val imagePicker =
        registerForActivityResult<PickVisualMediaRequest, Uri>(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                uriState.update { uri.toString() }
            }
        }


    @Composable
    private fun getBitmap(): Bitmap? {
        val uri = uriState.collectAsState().value

        val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(Size.ORIGINAL)
                .build()
        ).state

        if (imageState is AsyncImagePainter.State.Success) {
            Log.d("loadBitmap" , "bitmap is success $uri")
            return imageState.result.drawable.toBitmap()
        }

        if (imageState is AsyncImagePainter.State.Loading) {
            Log.d("loadBitmap" , "bitmap is loading $uri")

        }
        if (imageState is AsyncImagePainter.State.Error) {
            Log.d("loadBitmap" ,  "bitmap is error $uri")

        }

        return null
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {

            GeminiAIChatBotTheme {
               Surface( modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background) {
                   val navController = rememberNavController()
                   NavHost(startDestination = "home", navController = navController){
                       composable("home"){
                               HomeScreen(navController)

                       }
                       composable("about"){
                           About(navController ,this@MainActivity){
                           }
                       }
                   }
               }
            }
        }
    }


    @Composable
    fun HomeScreen(navController: NavController) {

        MyScaffold(navController = navController ,context = this@MainActivity) {
            ChatScreen(paddingValues = it)

        }

    }



    @Composable
    private fun ChatScreen(paddingValues: PaddingValues) {

        val j =  JSON(this@MainActivity)
        getJson.jsonObject= j



        val viewModel = viewModel<ChatViewModel>()
        val chatState = viewModel.chatState.collectAsState().value
        var bitmap = getBitmap()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.Bottom
        ){

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {

                itemsIndexed(chatState.chatList){index, chatItem ->
                    if(chatItem.isFromUser){
                        UserChatItem(chatItem.prompt,chatItem.bitmap,viewModel,chatState,chatItem.hadBitmap,index)
                    }
                    else{
                        ModelResponse(chatItem.prompt)
                    }

                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Column {
                    bitmap?.let {

                        chatState.bitmap = bitmap
                        bitmap = null

                        chatState.bitmap?.let{

                        Image(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 2.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentDescription = "picked image",
                            contentScale = ContentScale.Crop,
                            bitmap = it.asImageBitmap()
                        )
                        }


                    }

                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                imagePicker.launch(
                                    PickVisualMediaRequest
                                        .Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        .build()
                                )
                            },

                        imageVector = Icons.Rounded.AddPhotoAlternate,
                        contentDescription = "Add Photo",
                        tint = MaterialTheme.colorScheme.primary

                        )
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    modifier = Modifier.weight(1f),
                    value = chatState.prompt,
                    onValueChange = {
                        viewModel.onEvent(ChatUiEvent.UpdatePrompt(it))
                    },
                    placeholder = {
                        Text(text = "Type your question here...")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                if( !(chatState.chatList.isEmpty()) && chatState.chatList[0].isFromUser){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                            .animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 350,
                                    easing = LinearOutSlowInEasing
                                )
                            ),
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.primary)
                    uriState.update { "" }
                }

                else{
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                viewModel.onEvent(
                                    ChatUiEvent.SendPrompt(
                                        chatState.prompt,
                                        chatState.bitmap
                                    )
                                )
                                uriState.update { "" }
                            },
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "send prompt",
                        tint = MaterialTheme.colorScheme.primary

                    )

                }



            }
        }


    }


    @Composable
    fun UserChatItem(prompt: String, bitmap: Bitmap? ,viewModel: ChatViewModel,chatState: ChatState ,hadBitmap: Boolean,index : Int) {
        Column ( modifier = Modifier.padding(start = 150.dp , bottom = 7.dp ,end = 4.dp) ) {

            bitmap?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    bitmap = it.asImageBitmap()
                )
            }
            if (hadBitmap){
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.had_bitmap)
                    
                )
            }

            SelectionContainer {

          Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                text = prompt,
//                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontFamily(
                    Font(R.font.poppinsmedium)
                )
            )
            }

            IconButton(onClick = { viewModel.onEvent(ChatUiEvent.SendPrompt(chatState.chatList[index].prompt,chatState.chatList[index].bitmap)) },
                Modifier
                    .size(16.dp)
                    .align(Alignment.End)) {
                Icon(imageVector = Icons.Outlined.Replay, contentDescription = "", tint = MaterialTheme.colorScheme.onBackground)


            }
        }


    }





    @SuppressLint("InvalidColorHexValue")
     @Composable
    fun ModelResponse(prompt: String) {

        Column ( modifier = Modifier.padding(end = 100.dp , bottom = 15.dp ,start = 4.dp) ) {


            SelectionContainer {

            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF322733))
//                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                text = prompt,
//                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(R.font.poppinsmedium)
                ),
                color = Color(0xFFFFFFFF)
            )
            }
        }



    }
}
