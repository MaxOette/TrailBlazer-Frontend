package de.max.trailblazerfrontendv1.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = uiSettings,
        onMapLongClick = {
            //TODO ...
        }
    )
}
