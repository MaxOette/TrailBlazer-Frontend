package de.max.trailblazerfrontendv1.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import de.max.trailblazerfrontendv1.Api.LogoutAPI
import de.max.trailblazerfrontendv1.LoginActivity
import de.max.trailblazerfrontendv1.MainActivity
import de.max.trailblazerfrontendv1.Util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Profile Screen"
        )
        LogoutButton()
    }
}

@Composable
fun LogoutButton(){
    val context = LocalContext.current
    Button(
        onClick = {
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    LogoutAPI.logoutService.logout()

                    Constants.accessToken = ""
                    Constants.refreshToken = ""
                    Constants.email = ""

                    withContext(Dispatchers.Main) {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        (context as Activity).finish()
                    }

                }
            }catch(e: Exception){
                println("Error during logout : ${e.message}")
            }
        }
    ){
        Text("logout")
    }

}