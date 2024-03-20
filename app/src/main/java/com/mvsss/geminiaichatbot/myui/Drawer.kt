package com.mvsss.geminiaichatbot.myui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.mvsss.geminiaichatbot.R
import com.mvsss.geminiaichatbot.data.Menu

@Composable
fun DrawerContent(items:List<Menu>, context : Context, onItemClick : (Menu) -> Unit) {
    Image( painter = painterResource(id = R.drawable.drawer_img), contentDescription = "")
    LazyColumn (modifier = Modifier.padding(12.dp)){
        items(items){item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable {
                        onItemClick(item)
                    }
            ){
                Icon(imageVector = item.icon, contentDescription = "", tint = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = item.title, color = MaterialTheme.colorScheme.onBackground, fontFamily = FontFamily(
                    Font(R.font.poppinsmedium)
                ))

            }


        }

    }
}