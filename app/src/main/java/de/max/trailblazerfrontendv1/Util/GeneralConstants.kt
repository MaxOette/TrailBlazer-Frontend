package de.max.trailblazerfrontendv1.Util

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.MapType
import de.max.trailblazerfrontendv1.map.MapsViewModel

class GeneralConstants {
    companion object {
        //Constant variables
        lateinit var applicationContext: Context
        lateinit var viewModel : MapsViewModel
        var fetchingGps : Boolean = false
        var gpsTrackingEnabled : Boolean = true
        var dialogAck : Boolean = false
        var appNavBar : Boolean = false
        var mapType : MapType = MapType.TERRAIN
        var manualSearch : Boolean = false
        var volatileZoom : Float = 14f

        //Constant values
        val cameraBounds : LatLngBounds = LatLngBounds(LatLng(47.270111, 5.86633), LatLng(55.092927, 15.04473))
        val maxZoom : Float = 14f
        val minZoom : Float = 5f
        val defaultZoom : Float = 14f

    }
}