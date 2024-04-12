package de.max.trailblazerfrontendv1

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import de.max.trailblazerfrontendv1.Api.AuthStatusApi
import de.max.trailblazerfrontendv1.Api.HealthApi
import de.max.trailblazerfrontendv1.Api.RefreshApi
import de.max.trailblazerfrontendv1.location.LocationService
import de.max.trailblazerfrontendv1.screens.MapScreen
import de.max.trailblazerfrontendv1.navigation.AppNavigation
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )
        setContent {
            TrailBlazerFrontendV1Theme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigateToLoginButton()
                    CheckHealthButton()
                    RefreshButton()
                    AuthButton()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MapScreen()
                    }
                }
                AppNavigation()


//            TrailBlazerFrontendV1Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MapScreen()
//                }
//            }
            }
        }
    }

    @Composable
    fun AuthButton(){
        var context = LocalContext.current
        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = AuthStatusApi.authStatusService.getAuthStatus()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, response.string(), Toast.LENGTH_SHORT).show()
                        }
                    }catch(e: Exception){
                        println("Exception during auth status request: ${e.message}")
                    }
                }
            }
        ){
            Text("Check Auth Status")
        }
    }
    @Composable
    fun RefreshButton(){

        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO){
                    try {
                        val response = RefreshApi.refreshService.requestRefreshToken()
                        //Constants.refreshToken = response.string()
                    } catch (e: Exception) {
                        // Handle any exceptions that occur during the refresh token request
                        // The coroutineExceptionHandler will handle specific types of exceptions
                        println("Exception during refresh token request: ${e.message}")
                    }
                }
            }
        ){
            Text("request refresh Token")
        }
    }
    @Composable
    fun CheckHealthButton() {
        var health: Boolean = false
        var context = LocalContext.current
        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    val response = HealthApi.healthService.getHealth()
                    health = response
                    withContext(Dispatchers.Main) {
                        generateToast(health, context)
                    }
                }


            }
        )
        {
            Text("check health")
        }
    }

    fun generateToast(health: Boolean, context: Context){

        if(health == true) {
            Toast.makeText(context, "Hähnchen mit Brocolli", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Big Mac", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun NavigateToLoginButton() {
        val context = LocalContext.current
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Go to Login")
        }

    }
}

