package de.max.trailblazerfrontendv1.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Hier würde man z.B. den JSON Stile der Karte ändern
 */
class MapsViewModel : ViewModel() {

    var state by mutableStateOf(MapState())

}