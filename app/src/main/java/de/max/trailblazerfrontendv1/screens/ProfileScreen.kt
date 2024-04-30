package de.max.trailblazerfrontendv1.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.FriendApi
import de.max.trailblazerfrontendv1.Api.LogoutAPI
import de.max.trailblazerfrontendv1.LoginActivity
import de.max.trailblazerfrontendv1.MainActivity
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.location.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        ElevatedCardExample()
    }


    //LogoutButton()

}

//        Text(
//            text = "Profil",
//            modifier = Modifier
//                .padding(all = 30.dp)
//                .padding(top = 70.dp),
//            fontWeight = FontWeight.Bold,
//            fontSize = 36.sp
//        )
@Composable
fun ElevatedCardExample() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 8.dp, start = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Column(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                //Todo Anbinden des Usernames sobald verfügbar
                Text(
                    text = "Martin123",
                    modifier = Modifier.padding(start = 24.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
                Text(
                    text = UserConstants.email,
                    modifier = Modifier.padding(start = 24.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditProfileButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 4.dp)
            )
            LogoutButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, end = 8.dp)
            )
        }
    }
    Text(
        text = "Freunde",
        modifier = Modifier.padding(top = 16.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    )
    LazyColumn(

    ) {
        item {
            GetFriendsButton()
        }
    }
}

//	•`_´•

@Composable
fun GetFriendsButton(){
    Button(
        onClick = {
            GlobalScope.launch(Dispatchers.IO) {
                val response = FriendApi.friendService.getFriends();
            }
        }
    ){
        Text("get freunde test")
    }
}


@Composable
fun EditProfileButton(modifier: Modifier) {
    Button(
        modifier = modifier,
        onClick = {

        }
    ) {
//        Icon(
//            Icons.Default.BorderColor,
//            contentDescription = "",
//            tint = MaterialTheme.colorScheme.surface
//        ) // 	( 0 _ 0 )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Bearbeiten"
        )

    }
}

@Composable
fun LogoutButton(modifier: Modifier) {
    val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick = {
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    LogoutAPI.logoutService.logout()
                    Intent(GeneralConstants.applicationContext, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP
                        GeneralConstants.applicationContext.startService(this)
                    }
                    UserConstants.accessToken = ""
                    UserConstants.refreshToken = ""
                    UserConstants.email = ""

                    withContext(Dispatchers.Main) {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        (context as Activity).finish()
                    }

                }
            } catch (e: Exception) {
                println("Error during logout : ${e.message}")
            }
        }
    ) {
        Text("Abmelden")
    }

}