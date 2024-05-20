package de.max.trailblazerfrontendv1.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import de.max.trailblazerfrontendv1.Api.TileApi
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.Util.ViewModelHolder
import de.max.trailblazerfrontendv1.location.LocationService
import de.max.trailblazerfrontendv1.map.MapsViewModel
import de.max.trailblazerfrontendv1.ui.dialog.GpsTrackingDisabledDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    if (!GeneralConstants.fetchingGps && !GeneralConstants.dialogAck) {
        GpsTrackingDisabledDialog(mutableStateOf(true), applicationContext)
    }

    ViewModelHolder.ViewModelHolderObject.mapsViewModel = viewModel


    //val smallPolygonLocations: MutableList<TileData> = mutableListOf()

    //UserConstants.testTileData.filter { (it.opacity == 0) }
    //    .map {
    //        listOf(
    //            LatLng(it.posUpperRight[0], it.posUpperRight[1]),
    //            LatLng(it.posLowerRight[0], it.posLowerRight[1]),
    //            LatLng(it.posLowerLeft[0], it.posLowerLeft[1]),
    //            LatLng(it.posUpperLeft[0], it.posUpperLeft[1]),
    //        )
    //    }

    val germanyMapBounds = listOf(
        LatLng(47.270111, 5.86633), //südwest
        LatLng(47.270111, 15.04473), //südost
        LatLng(55.092927, 15.04473), //nordost
        LatLng(55.092927, 5.86633)  //nordwest
    )


    //val holeList = listOf(
    //    smallPolygonLocations
    //)

    //val uiSettings = remember {
    //    MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = false)
    //}

    val cameraPosition = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
            LatLng(UserConstants.userLat, UserConstants.userLng),
            GeneralConstants.volatileZoom
        )
    }

    val polygonData = remember {
        mutableStateOf(emptyList<List<LatLng>>())
    }

    LaunchedEffect(Unit) {
        viewModel.cameraPosition.collect { newPosition ->
            //Kamera zur aktuellen Position teleportieren
            if (!GeneralConstants.manualSearch) {
                cameraPosition.animate(CameraUpdateFactory.newCameraPosition(newPosition))
            }

            //Alle Kacheln im Bereich holen
            try {
                polygonData.value = TileApi.tileService.getTiles(
                    cameraPosition.position.target.latitude,
                    cameraPosition.position.target.longitude,
                    GeneralConstants.volatileZoom.toInt().toByte()
                ).filter { (it.opacity == 0) }
                    .map {
                        listOf(
                            LatLng(it.posUpperRight[0], it.posUpperRight[1]),
                            LatLng(it.posLowerRight[0], it.posLowerRight[1]),
                            LatLng(it.posLowerLeft[0], it.posLowerLeft[1]),
                            LatLng(it.posUpperLeft[0], it.posUpperLeft[1]),
                        )
                    }
                println("--polygonData geholt für lat: " + cameraPosition.position.target.latitude + " mit Elementzahl: " + polygonData.value.size)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = false),
        cameraPositionState = cameraPosition,
        onMyLocationButtonClick = {
            GeneralConstants.manualSearch = false
            GeneralConstants.volatileZoom = GeneralConstants.defaultZoom
            GlobalScope.launch(Dispatchers.Main) {
                cameraPosition.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(UserConstants.userLat, UserConstants.userLng),
                            GeneralConstants.volatileZoom
                        )
                    )
                )
                polygonData.value = TileApi.tileService.getTiles(
                    cameraPosition.position.target.latitude,
                    cameraPosition.position.target.longitude, /* cameraPosition.position.zoom.toInt() */
                    GeneralConstants.volatileZoom.toInt().toByte()
                ).filter { (it.opacity == 0) }
                    .map {
                        listOf(
                            LatLng(it.posUpperRight[0], it.posUpperRight[1]),
                            LatLng(it.posLowerRight[0], it.posLowerRight[1]),
                            LatLng(it.posLowerLeft[0], it.posLowerLeft[1]),
                            LatLng(it.posUpperLeft[0], it.posUpperLeft[1]),
                        )
                    }
            }
            true
        }
    ) {
        Polygon(
            points = germanyMapBounds,
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
    }

    val onMapCameraIdle: (cameraPosition: CameraPosition) -> Unit = {

        val cameraMovementReason = cameraPosition.cameraMoveStartedReason

        if (cameraMovementReason == CameraMoveStartedReason.GESTURE) {
            GeneralConstants.manualSearch = true
            var newZoom = cameraPosition.position.zoom
            if (GeneralConstants.volatileZoom != newZoom) {
                newZoom = newZoom + 2
                if(newZoom >= 14f) {
                    newZoom = 14f
                } else if (newZoom <= 8f) {
                    newZoom = 8f
                }
                GeneralConstants.volatileZoom = newZoom
            }
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    polygonData.value = TileApi.tileService.getTiles(
                        cameraPosition.position.target.latitude,
                        cameraPosition.position.target.longitude,
                        GeneralConstants.volatileZoom.toInt().toByte()
                    ).filter { (it.opacity == 0) }
                        .map {
                            listOf(
                                LatLng(it.posUpperRight[0], it.posUpperRight[1]),
                                LatLng(it.posLowerRight[0], it.posLowerRight[1]),
                                LatLng(it.posLowerLeft[0], it.posLowerLeft[1]),
                                LatLng(it.posUpperLeft[0], it.posUpperLeft[1]),
                            )
                        }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    LaunchedEffect(key1 = cameraPosition.isMoving) {
        onMapCameraIdle(cameraPosition.position)

    }
}
