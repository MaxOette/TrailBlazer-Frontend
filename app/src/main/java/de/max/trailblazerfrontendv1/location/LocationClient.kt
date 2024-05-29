package de.max.trailblazerfrontendv1.location

import android.location.Location
import de.max.trailblazerfrontendv1.map.MapsViewModel
import kotlinx.coroutines.flow.Flow

interface LocationClient {

    fun getLocationUpdates(interval: Long) : Flow<Location>

    class LocationException(message: String) : Exception()
}