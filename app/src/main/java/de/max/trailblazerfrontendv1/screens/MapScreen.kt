package de.max.trailblazerfrontendv1.screens

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.Util.ViewModelHolder
import de.max.trailblazerfrontendv1.location.LocationService
import de.max.trailblazerfrontendv1.map.MapsViewModel

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val applicationContext = GeneralConstants.applicationContext
    ViewModelHolder.ViewModelHolderObject.mapsViewModel = viewModel

    if (!GeneralConstants.fetchingGps) {
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            applicationContext.startService(this)
        }
        GeneralConstants.fetchingGps = true;
    }

    val smallPolygonLocations = listOf(
        LatLng(48.7787391, 8.8475872), //südwest: links von Stuttgart @48.7787391,8.8475872
        LatLng(49.4494176, 11.0743533), //südost: Nbg Lorenzkirche @49.4494176,11.0743533
        LatLng(49.5468979, 10.7137187), //nordost: Rewe Emskirchen @49.5468979,10.7137187
        LatLng(50.0986972,8.4835624)  //nordwest: Frankfurt Airport @50.0986972,8.4835624
    )

    val germanyLocations = listOf(
        LatLng(47.270111, 5.86633), //südwest
        LatLng(47.270111, 15.04473), //südost
        LatLng(55.092927, 15.04473), //nordost
        LatLng(55.092927, 5.86633)  //nordwest
    )


    val holeList = listOf(
        smallPolygonLocations
    )

    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true)
    }

    val cameraPosition = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(LatLng(UserConstants.userLat, UserConstants.userLng), 14f)
    }

    LaunchedEffect(Unit) {
        viewModel.cameraPosition.collect { newPosition ->
            cameraPosition.animate(CameraUpdateFactory.newCameraPosition(newPosition))
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPosition
    ) {

        Polygon(
            points = germanyLocations,
            clickable = false,
            fillColor = Color.Red.copy(alpha = 0.5f),
            geodesic = false, //false = Krümmung der Erdoberfläche wird nicht beachtet
            holes = holeList,
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
