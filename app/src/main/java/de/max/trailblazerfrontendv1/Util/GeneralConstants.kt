package de.max.trailblazerfrontendv1.Util

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class GeneralConstants {
    companion object {
        val cameraBounds : LatLngBounds = LatLngBounds(LatLng(47.270111, 5.86633), LatLng(55.092927, 15.04473))
        val maxZoom : Float = 14f;
        val minZoom : Float = 5f;

    }
}