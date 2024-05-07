package de.max.trailblazerfrontendv1.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import de.max.trailblazerfrontendv1.Api.TileApi
import de.max.trailblazerfrontendv1.Api.TileData
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.Util.ViewModelHolder
import de.max.trailblazerfrontendv1.location.LocationService
import de.max.trailblazerfrontendv1.map.MapsViewModel
import de.max.trailblazerfrontendv1.ui.dialog.GpsTrackingDisabledDialog

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    GeneralConstants.viewModel = viewModel
    val applicationContext = GeneralConstants.applicationContext
    if (!GeneralConstants.fetchingGps && GeneralConstants.gpsTrackingEnabled) {
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            applicationContext.startService(this)
        }
        GeneralConstants.fetchingGps = true;
    }
    if (!GeneralConstants.gpsTrackingEnabled) {
        GeneralConstants.fetchingGps = false;
        GpsTrackingDisabledDialog(mutableStateOf(true), applicationContext)
    }

    ViewModelHolder.ViewModelHolderObject.mapsViewModel = viewModel


    val smallPolygonLocations : MutableList<TileData> = mutableListOf()

    var visitedList = UserConstants.testTileData.filter{ (it.opacity == 0) }
        .map { listOf(
            LatLng(it.posUpperRight[0], it.posUpperRight[1]),
            LatLng(it.posLowerRight[0], it.posLowerRight[1]),
            LatLng(it.posLowerLeft[0], it.posLowerLeft[1]),
            LatLng(it.posUpperLeft[0], it.posUpperLeft[1]),
            ) }

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
        CameraPosition.fromLatLngZoom(LatLng(UserConstants.userLat, UserConstants.userLng), GeneralConstants.defaultZoom)
    }

    val polygonData = remember {
        mutableStateOf(emptyList<List<LatLng>>())
    }

    LaunchedEffect(Unit) {
        viewModel.cameraPosition.collect { newPosition ->
            cameraPosition.animate(CameraUpdateFactory.newCameraPosition(newPosition))
            //TODO: Bei der Anfrage um eine Kachel aufzudecken immer erst prüfen, ob gpsTracking in den Settings enabled ist!
            try {
                polygonData.value = TileApi.tileService.getTiles(cameraPosition.position.target.latitude, cameraPosition.position.target.longitude, /* cameraPosition.position.zoom.toInt() */ GeneralConstants.defaultZoom.toInt().toByte()).filter{ (it.opacity == 0) }
                    .map { listOf(
                        LatLng(it.posUpperRight[0], it.posUpperRight[1]),
                        LatLng(it.posLowerRight[0], it.posLowerRight[1]),
                        LatLng(it.posLowerLeft[0], it.posLowerLeft[1]),
                        LatLng(it.posUpperLeft[0], it.posUpperLeft[1]),
                    ) }
                println("--polygonData geholt für lat: " + cameraPosition.position.target.latitude + " mit Elementzahl: " + polygonData.value.size)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }



    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPosition
    ) {
        println("++++++++++ Beginning of Polygon ++++++++")
        if (visitedList.size > 1) {
            visitedList = visitedList.subList(0, 100)
        }

        Polygon(
            points = germanyLocations,
            clickable = false,
            fillColor = Color.DarkGray.copy(alpha = 0.8f),
            geodesic = false, //false = Krümmung der Erdoberfläche wird nicht beachtet
            holes = polygonData.value,
            strokeColor = Color.Magenta,
            strokeJointType = JointType.DEFAULT,
            strokePattern = null,
            strokeWidth = 10f,
            tag = "polygon",
            visible = true,
            zIndex = 1f,
            onClick = {}
        )

        /*
        for (tileData in UserConstants.testTileData) {
            if (tileData.oppacity == 0) {
                GroundOverlay(
                    position = GroundOverlayPosition.create(
                        LatLngBounds(
                            LatLng(
                                tileData.posLowerLeft[0],
                                tileData.posLowerLeft[1]
                            ), LatLng(tileData.posUpperRight[0], tileData.posUpperRight[1])
                        )
                    ), image = BitmapDescriptorFactory.fromResource(
                        R.drawable.transparent_pixel
                    )
                )
            } else {
                GroundOverlay(
                    position = GroundOverlayPosition.create(
                        LatLngBounds(
                            LatLng(
                                tileData.posLowerLeft[0],
                                tileData.posLowerLeft[1]
                            ), LatLng(tileData.posUpperRight[0], tileData.posUpperRight[1])
                        )
                    ), image = BitmapDescriptorFactory.fromResource(
                        R.drawable.grauer_pixel
                    )
                )
            }
        } */
    }
}
