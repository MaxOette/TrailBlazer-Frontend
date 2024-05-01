package de.max.trailblazerfrontendv1.ui.dialog

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import de.max.trailblazerfrontendv1.Util.MessageStrings
import de.max.trailblazerfrontendv1.location.LocationService

@Composable
fun GpsTrackingDisabledDialog(
    showDialog: MutableState<Boolean>,
    applicationContext: Context
) {
    if (showDialog.value) {
        AlertDialog(
            icon = {
                Icon(
                    Icons.Default.LocationOff,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(text = MessageStrings.gpsTrackingDisabledDialogTitle)
            },
            text = {
                Text(text = MessageStrings.gpsTrackingDisabledDialogText)
            },
            onDismissRequest = {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    applicationContext.startService(this)
                }
                showDialog.value = false
            },
            confirmButton = {
                /*
                TextButton(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text(MessageStrings.gpsTrackingDisabledDialogConfirm)
                } */
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text(MessageStrings.gpsTrackingDisabledDialogDismiss)
                }
            }
        )
    }
}