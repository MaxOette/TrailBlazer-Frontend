package de.max.trailblazerfrontendv1.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.location.CameraPositionUpdater
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapsViewModel : ViewModel(), CameraPositionUpdater {

    var state by mutableStateOf(MapState())

    private val _cameraPosition = MutableStateFlow(
        CameraPosition.fromLatLngZoom(LatLng(UserConstants.userLat, UserConstants.userLng), 6f)
    )
    val cameraPosition: StateFlow<CameraPosition> = _cameraPosition.asStateFlow()

    override fun updateCameraPosition(newPosition: CameraPosition) {
        _cameraPosition.value = newPosition
    }

}