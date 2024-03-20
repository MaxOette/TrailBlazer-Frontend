package de.max.trailblazerfrontendv1.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val locationsForPolygon = listOf(
        LatLng(48.7787391, 8.8475872), //südwest: Stuttgart Mitte @48.7787391,8.8475872
        LatLng(49.4494176, 11.0743533), //südost: Nbg Lorenzkirche @49.4494176,11.0743533
        LatLng(49.5468979, 10.7137187), //nordost: Rewe Emskirchen @49.5468979,10.7137187
        LatLng(50.0986972,8.4835624)  //nordwest: Frankfurt Airport @50.0986972,8.4835624
    )
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = uiSettings
    ) {
        Polygon(
            points = locationsForPolygon,
            clickable = false,
            fillColor = Color.Red.copy(alpha = 0.5f),
            geodesic = false, //Krümmung der Erdoberfläche wird nicht beachtet
            holes = emptyList(),
            strokeColor = Color.Magenta,
            strokeJointType = JointType.DEFAULT,
            strokePattern = null,
            strokeWidth = 10f,
            tag = "polygon",
            visible = true,
            zIndex = 1f,
            onClick = {}
        )
    }
}
