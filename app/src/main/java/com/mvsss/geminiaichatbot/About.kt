package com.mvsss.geminiaichatbot


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mvsss.geminiaichatbot.ui.theme.Lightblue

import com.mvsss.geminiaichatbot.data.Menu
import com.mvsss.geminiaichatbot.myui.MyScaffold


@Composable
fun About(navController: NavController, context : Context, onDrawerItemClicked : (Menu) -> Unit) {

    MyScaffold(navController = navController, context = context, )  {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                ContactItem("Email",Icon = painterResource(id = R.drawable.ic_gmail), value = "mvsss7150@gmail.com") {
                    sendEmail(context, "mvsss7150@gmail.com")
                }
                ContactItem("LinkedIn", "Sharma715" ,Icon = painterResource(id = R.drawable.ic_linkedin) ) {
                    openUrl(context, "https://www.linkedin.com/in/mangipudi-v-srinivasa-subrahmanya-sarma-63782a255/")
                }
                ContactItem("GitHub", "Sharma715",Icon = painterResource(id = R.drawable.ic_github)) {
                    openUrl(context, "https://github.com/sharma715")
                }
            }
        Column(modifier = Modifier
            .padding(end = 9.dp, bottom = 5.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
            Text(text = "This App is developed based on Google's Gemini API", fontSize = 14.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground)

        }
        }


    }

fun sendEmail(context: android.content.Context, email: String) {

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Composable
fun ContactItem(label: String, value: String, Icon : Painter, onClick: () -> Unit) {

    Row (horizontalArrangement = Arrangement.Center){
        

        Icon(painter = Icon, "", tint = Color.Unspecified, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label : ",
            fontSize = 18.sp,
            modifier = Modifier.clickable(onClick = onClick),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily(Font(R.font.poppinsmedium)),


        )
        Text(
            text = value,
            color = Lightblue,
            fontSize = 18.sp,
            style  = TextStyle( color =  Lightblue, textDecoration =  TextDecoration.Underline),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable(onClick = onClick),
            fontFamily = FontFamily(Font(R.font.poppinsmedium))

        )
    }


}

