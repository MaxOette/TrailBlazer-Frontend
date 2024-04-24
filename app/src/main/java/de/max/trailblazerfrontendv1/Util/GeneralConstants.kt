package de.max.trailblazerfrontendv1.Util

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class GeneralConstants {
    companion object {
        //Constant variables
        lateinit var applicationContext: Context
        var fetchingGps : Boolean = false
        var forceDarkMode : Boolean = false

        //Constant values
        val cameraBounds : LatLngBounds = LatLngBounds(LatLng(47.270111, 5.86633), LatLng(55.092927, 15.04473))
        val maxZoom : Float = 14f
        val minZoom : Float = 5f
        val defaultZoom : Float = 12f
    }
}