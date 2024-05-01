package de.max.trailblazerfrontendv1.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.location.LocationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.maps.android.compose.MapType
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.datastore.DataStoreSingleton
import de.max.trailblazerfrontendv1.map.MapsViewModel
import kotlinx.coroutines.MainScope

@Composable
fun SettingsScreen(applicationContext: Context) {
    val dataStore = DataStoreSingleton.DataStoreManager.getDataStore(applicationContext)

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
            /*
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
            } */

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "App-Einstellungen",
            )
            AppSettingsCard(applicationContext, dataStore)

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Kartenstil",
            )
            MapSettingsCard(applicationContext, dataStore)
        }
    }
}

/*
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
} */

/*
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
} */

@Composable
fun AppSettingsCard(applicationContext: Context, dataStore: DataStore<Preferences>) {
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
            GPSTrackingSwitch(applicationContext)
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
                Text(text = "Darkmode erzwingen", modifier = Modifier.padding(start = 8.dp))
            }
            DarkModeSwitch(applicationContext, dataStore)
        }
    }
}

/*
@Composable
fun GPSTrackingSwitch(applicationContext: Context, dataStore: DataStore<Preferences>) {
    val GPS_TRACKING_KEY = booleanPreferencesKey("gps_tracking")

    val gpsTrackingEnabledFlow: Flow<Boolean> = dataStore.data
        .catch { exception  ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[GPS_TRACKING_KEY] ?: false
        }

    //val gpsTrackingEnabled by gpsTrackingEnabledFlow.collectAsState(initial = true)
    var gpsTrackingEnabled = remember { GeneralConstants.gpsTrackingEnabled }
    val scope = MainScope()

    Switch(
        checked = gpsTrackingEnabled,
        onCheckedChange = { isChecked ->
            gpsTrackingEnabled = isChecked
            GeneralConstants.gpsTrackingEnabled = isChecked
            scope.launch {
                dataStore.edit { settings ->
                    settings[GPS_TRACKING_KEY] = isChecked
                }
            }
            if (isChecked) {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                    applicationContext.startService(this)
                }
            } else {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    applicationContext.startService(this)
                }
            }
        },
    )
} */

@Composable
fun GPSTrackingSwitch(applicationContext: Context) {
    var checked by remember { mutableStateOf(GeneralConstants.gpsTrackingEnabled) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
            GeneralConstants.gpsTrackingEnabled = it
            if (it) {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                    applicationContext.startService(this)
                }
            } else {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    applicationContext.startService(this)
                }
            }
        },
    )
}

@Composable
fun DarkModeSwitch(applicationContext: Context, dataStore: DataStore<Preferences>) {

    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    val darkModeEnabledFlow: Flow<Boolean> = dataStore.data
        .catch { exception  ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    val darkModeEnabled by darkModeEnabledFlow.collectAsState(initial = false)

    val scope = MainScope()

    Switch(
        checked = darkModeEnabled,
        onCheckedChange = { isChecked ->
            scope.launch {
                dataStore.edit { settings ->
                    settings[DARK_MODE_KEY] = isChecked
                }
            }
        },
    )
}

@Composable
fun MapSettingsCard(applicationContext: Context, dataStore: DataStore<Preferences>) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(top = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            var selectedOption = remember { mutableIntStateOf(GeneralConstants.mapType.value) }

            // Erste Spalte
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.kartenstil_flat), contentDescription = "Kartenstil Flach", modifier = Modifier.size(80.dp))
                Text(text = "Flach")
                RadioButton(selected = selectedOption.value == 1, onClick = {
                    selectedOption.intValue = 1
                    GeneralConstants.mapType = MapType.NORMAL
                    GeneralConstants.viewModel.updateMapType(MapType.NORMAL)
                })
            }

            // Zweite Spalte
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.kartenstil_terrain), contentDescription = "Kartenstil Gelände", modifier = Modifier.size(80.dp))
                Text(text = "Gelände")
                RadioButton(selected = selectedOption.value == 3, onClick = {
                    selectedOption.intValue = 3
                    GeneralConstants.mapType = MapType.TERRAIN
                    GeneralConstants.viewModel.updateMapType(MapType.TERRAIN)
                })

            }

            // Dritte Spalte
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.kartenstil_sattelite), contentDescription = "Kartenstil Satellit", modifier = Modifier.size(80.dp))
                Text(text = "Satellit")
                RadioButton(selected = selectedOption.value == 4, onClick = {
                    selectedOption.intValue = 4
                    GeneralConstants.mapType = MapType.HYBRID
                    GeneralConstants.viewModel.updateMapType(MapType.HYBRID)
                })

            }
        }
    }
}
