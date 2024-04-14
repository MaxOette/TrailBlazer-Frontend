package de.max.trailblazerfrontendv1.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.max.trailblazerfrontendv1.location.LocationService

@Composable
fun SettingsScreen(applicationContext: Context) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Settings Screen"
            )

            Button(onClick = { Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                applicationContext.startService(this)
                }
            }) {
                Text(text = "Start GPS Tracking")
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            Button(onClick = { Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                applicationContext.startService(this)
                }
            } ) {
                Text(text = "Stop GPS Tracking")
            }
        }

    }
}