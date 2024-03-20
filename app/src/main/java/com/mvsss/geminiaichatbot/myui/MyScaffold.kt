package com.mvsss.geminiaichatbot.myui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mvsss.geminiaichatbot.R
import com.mvsss.geminiaichatbot.data.Menu
import com.mvsss.geminiaichatbot.getJson
import kotlinx.coroutines.launch


@Composable
fun MyScaffold(navController: NavController,  context : Context, belowScaffold : @Composable (PaddingValues) -> Unit) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()


    Scaffold (
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,

        topBar = {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .height(49.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ){

                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()

                    }
                }) {

                    Icon(imageVector = Icons.Rounded.Menu,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Add Photo" )
                }

                Spacer(modifier = Modifier.width(18.dp))


                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = "Gemini AI Chat Bot",
                    fontFamily = FontFamily(
                        Font(R.font.poppinsbold)
                    )
                )

                Spacer(modifier = Modifier.width(78.dp))
                

                
                
            }

        },
        drawerContent = {
            DrawerContent(
                items = listOf(
                    Menu(
                        id = "Home",
                        title = "Home",
                        icon = Icons.Default.Home
                    ) ,
                    Menu(
                        id = "Clear",
                        title = "Clear Chat",
                        icon = Icons.Rounded.ClearAll
                    ),
                    Menu(
                        id = "About Page",
                        title = "Contact",
                        icon = Icons.Default.Info
                    )
                ),
                context = context
            ){it->
                 val currentScreen = (navController.currentDestination?.route ?: "home")

                if(it.id.equals("Clear")){
                    getJson.jsonObject?.saveChatState(ChatState())
                    Toast.makeText(context, "Restart the App to clear chat" , Toast.LENGTH_SHORT).show()
                }
                if(it.id.equals("Home") && currentScreen.equals("home")){
                    scope.launch {
                    scaffoldState.drawerState.close()
                    }
                }
               else  if(it.id.equals("About Page") && currentScreen.equals("home")){
                   navController.navigate("about")
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
                else if(it.id.equals("About Page") && currentScreen.equals("about")){
                    scope.launch {
                    scaffoldState.drawerState.close()
                    }
                }
                else if(it.id.equals("Home") && currentScreen.equals("about")){
                    navController.popBackStack()
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }

                }



            }

        },
        backgroundColor = MaterialTheme.colorScheme.background,
        drawerBackgroundColor = MaterialTheme.colorScheme.background,
        drawerShape = MaterialTheme.shapes.extraSmall
    ){
        belowScaffold(it)
    }


}

