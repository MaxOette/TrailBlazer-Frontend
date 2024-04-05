package de.max.trailblazerfrontendv1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import de.max.trailblazerfrontendv1.Api.HealthApi
//import dagger.hilt.android.AndroidEntryPoint
import de.max.trailblazerfrontendv1.presentation.MapScreen
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrailBlazerFrontendV1Theme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigateToLoginButton()
                    CheckHealthButton()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MapScreen()
                    }
                }

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
    fun CheckHealthButton(){
        val health : Boolean
        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = HealthApi.healthService.getHealth()
                        if(response == true)
v
                        }
                    catch (e: Exception) {
                        // Handle network errors
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



        @Composable
        fun Greeting(name: String, modifier: Modifier = Modifier) {
            Surface {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                }
            }

            Text(
                text = "Hello $name!",
                modifier = modifier
            )
        }


    }
}