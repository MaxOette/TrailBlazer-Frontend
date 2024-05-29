package de.max.trailblazerfrontendv1

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import de.max.trailblazerfrontendv1.Api.AuthStatusApi
import de.max.trailblazerfrontendv1.Api.RefreshApi
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.Util.datastore.dataStoreReader
import de.max.trailblazerfrontendv1.navigation.AppNavigation
import de.max.trailblazerfrontendv1.screens.MapScreen
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneralConstants.applicationContext = applicationContext

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )

        setContent {
            val darkModeEnabled = dataStoreReader(
                applicationContext = applicationContext,
                key = "dark_mode",
                initial = false
            )
            TrailBlazerFrontendV1Theme(isSystemInDarkTheme() || darkModeEnabled) {
                ApplicationContent()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun ApplicationContent() {
        val locationFinePermissionState = rememberPermissionState(
            permission = Manifest.permission.ACCESS_FINE_LOCATION
        )
        val locationCoarsePermissionState = rememberPermissionState(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (locationFinePermissionState.status.isGranted || locationCoarsePermissionState.status.isGranted) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen()
                }
            }
            AppNavigation(GeneralConstants.applicationContext)
            GeneralConstants.appNavBar = true
        } else {
            NoPermissionScreen()
        }

    }

    private @Composable
    fun NoPermissionScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShareLocation,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Damit du TrailBlazer verwenden kannst, musst du der App den Zugriff auf deine GPS-Positionsdaten gestatten.")
                }
            }
        }

    }


    @Composable
    fun AuthButton() {
        var context = LocalContext.current
        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = AuthStatusApi.authStatusService.getAuthStatus()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, response.string(), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        println("Exception during auth status request: ${e.message}")
                    }
                }
            }
        ) {
            Text("Check Auth Status")
        }
    }

    @Composable
    fun RefreshButton() {

        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = RefreshApi.refreshService.requestRefreshToken()
                        UserConstants.refreshToken = response.string()

                    } catch (e: Exception) {
                        println("Exception during refresh token request: ${e.message}")
                    }
                }
            }
        ) {
            Text("request refresh Token")
        }
    }
}



