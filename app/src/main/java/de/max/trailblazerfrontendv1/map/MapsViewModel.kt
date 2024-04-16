package de.max.trailblazerfrontendv1.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import de.max.trailblazerfrontendv1.Util.UserConstants

/**
 * Hier würde man z.B. den JSON Style der Karte ändern
 */
class MapsViewModel : ViewModel() {

    var state by mutableStateOf(MapState())

    var cameraPosition = mutableStateOf(CameraPosition.fromLatLngZoom(LatLng(UserConstants.userLat, UserConstants.userLng), 14f))

    // MutableState for camera position
    //val cameraPositionState = mutableStateOf(UserConstants.cameraPosition)

    // Update camera position when UserConstants.cameraPosition changes
    fun updateCameraPosition(newPosition: CameraPosition) {
        cameraPosition.value = newPosition
    }

}