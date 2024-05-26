package de.max.trailblazerfrontendv1.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.MessageStrings

@Composable
fun GpsTrackingDisabledDialog(
    showDialog: MutableState<Boolean>,
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
                GeneralConstants.dialogAck = true
                showDialog.value = false
            },
            confirmButton = {
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        GeneralConstants.dialogAck = true
                        showDialog.value = false
                    }
                ) {
                    Text(MessageStrings.gpsTrackingDisabledDialogDismiss)
                }
            }
        )
    }
}