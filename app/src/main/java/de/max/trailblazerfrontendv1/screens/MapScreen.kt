package de.max.trailblazerfrontendv1.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.map.MapsViewModel

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

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

    /* Extrempunkte Deutschlands:

           Latitude, Longitude
    Norden: 55.0846, 8.3174
    Osten: 51.27291, 15.04193
    Süden: 47.270111, 10.178342
    Westen: 51.05109, 5.86633

    Lat-Distanz Nord-Süd: 7,814489 => 864,079306686km -> Ziel: 865km => 7,822816
    Lng-Distanz West-Ost: 9,1756 => 642,8053375km -> Ziel: 643km => 9,1784

    1km Nord-Süd entspricht ca.: Lat 0,009043717
    1km West-Ost entspricht ca.: Lng 0,01427433904

    Daraus errechnete Eckpunkte:

    Südwest-Ecke: 47.270111, 5.86633
    Südost-Ecke: 47.270111, 15.04193
    Nordost-Ecke: 55.0846, 15.04193
    Nordwest-Ecke: 55.0846, 5.86633

    Mit überschuss im Osten und Norden:
    Südwest-Ecke: 47.270111, 5.86633
    Südost-Ecke: 47.270111, 15.04473
    Nordost-Ecke: 55.092927, 15.04473
    Nordwest-Ecke: 55.092927, 5.86633

     */

    val holeList = listOf(
        smallPolygonLocations
    )


    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true)
    }

    //val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(UserConstants.userLat, UserConstants.userLng), 14f)
    val cameraPositionState = rememberCameraPositionState { position = UserConstants.cameraPosition }
    //val cameraPositionState = viewModel.cameraPosition

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState
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
