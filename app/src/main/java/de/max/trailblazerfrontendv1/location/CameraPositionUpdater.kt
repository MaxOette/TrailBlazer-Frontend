package de.max.trailblazerfrontendv1.location

import com.google.android.gms.maps.model.CameraPosition

interface CameraPositionUpdater {
    fun updateCameraPosition(newPosition: CameraPosition)
}