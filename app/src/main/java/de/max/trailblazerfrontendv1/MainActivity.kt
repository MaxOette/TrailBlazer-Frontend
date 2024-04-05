package de.max.trailblazerfrontendv1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

