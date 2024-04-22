package de.max.trailblazerfrontendv1.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.LogoutAPI
import de.max.trailblazerfrontendv1.LoginActivity
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.location.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(applicationContext: Context) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp)
        ) {
            Text(
                text = "Optionen",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StartTrackingButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    applicationContext
                )
                StopTrackingButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    applicationContext
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "App-Einstellungen",
            )
            AppSettingsCard()

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Karteneinstellungen",
            )
            AppSettingsCard()
        }



    }
}

@Composable
fun StartTrackingButton(modifier: Modifier, applicationContext: Context) {
    Button(
        modifier = modifier,
        onClick = {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                applicationContext.startService(this)
            }
        }
    ) {
        Text("Start GPS Tracking")
    }
}

@Composable
fun StopTrackingButton(modifier: Modifier, applicationContext: Context) {
    Button(
        modifier = modifier,
        onClick = {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                applicationContext.startService(this)
            }
        }
    ) {
        Text("Stop GPS Tracking")
    }
}

@Composable
fun AppSettingsCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(top = 8.dp),


    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
            ) {
                Icon(
                    Icons.Default.ShareLocation,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = "GPS-Tracking", modifier = Modifier.padding(start = 8.dp))
            }
            Switch(
                checked = true,
                onCheckedChange = {
                    //checked = it
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row() {
                Icon(
                    Icons.Default.DarkMode,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = "Darkmode", modifier = Modifier.padding(start = 8.dp))
            }
            Switch(
                checked = false,
                onCheckedChange = {
                    //checked = it
                }
            )
        }
    }
}