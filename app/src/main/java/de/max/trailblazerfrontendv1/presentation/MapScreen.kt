package de.max.trailblazerfrontendv1.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GroundOverlay
import com.google.maps.android.compose.GroundOverlayPosition
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import de.max.trailblazerfrontendv1.R

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
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
        MapUiSettings(zoomControlsEnabled = true)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = viewModel.state.properties,
        uiSettings = uiSettings
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

        /*
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,6.009073390399999), LatLng(55.092927,6.151816780799999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,6.294560171199999), LatLng(55.092927,6.4373035615999985))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,6.580046951999998), LatLng(55.092927,6.722790342399998))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,6.865533732799998), LatLng(55.092927,7.008277123199997))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,7.151020513599997), LatLng(55.092927,7.293763903999997))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,7.436507294399997), LatLng(55.092927,7.579250684799996))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,7.721994075199996), LatLng(55.092927,7.864737465599996))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,8.007480855999995), LatLng(55.092927,8.150224246399995))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,8.292967636799995), LatLng(55.092927,8.435711027199995))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,8.578454417599994), LatLng(55.092927,8.721197807999994))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,8.863941198399994), LatLng(55.092927,9.006684588799994))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,9.149427979199993), LatLng(55.092927,9.292171369599993))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,9.434914759999993), LatLng(55.092927,9.577658150399992))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,9.720401540799992), LatLng(55.092927,9.863144931199992))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,10.005888321599992), LatLng(55.092927,10.148631711999991))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,10.291375102399991), LatLng(55.092927,10.43411849279999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,10.57686188319999), LatLng(55.092927,10.71960527359999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,10.86234866399999), LatLng(55.092927,11.00509205439999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,11.14783544479999), LatLng(55.092927,11.29057883519999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,11.433322225599989), LatLng(55.092927,11.576065615999989))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,11.718809006399988), LatLng(55.092927,11.861552396799988))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,12.004295787199988), LatLng(55.092927,12.147039177599988))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,12.289782567999987), LatLng(55.092927,12.432525958399987))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,12.575269348799987), LatLng(55.092927,12.718012739199986))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,12.860756129599986), LatLng(55.092927,13.003499519999986))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,13.146242910399986), LatLng(55.092927,13.288986300799985))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,13.431729691199985), LatLng(55.092927,13.574473081599985))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,13.717216471999985), LatLng(55.092927,13.859959862399984))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,14.002703252799984), LatLng(55.092927,14.145446643199984))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,14.288190033599983), LatLng(55.092927,14.430933423999983))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,14.573676814399983), LatLng(55.092927,14.716420204799983))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(55.00248983,14.859163595199982), LatLng(55.092927,15.001906985599982))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,5.86633), LatLng(55.00248983,6.009073390399999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,6.151816780799999), LatLng(55.00248983,6.294560171199999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,6.4373035615999985), LatLng(55.00248983,6.580046951999998))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,6.722790342399998), LatLng(55.00248983,6.865533732799998))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,7.008277123199997), LatLng(55.00248983,7.151020513599997))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,7.293763903999997), LatLng(55.00248983,7.436507294399997))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,7.579250684799996), LatLng(55.00248983,7.721994075199996))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,7.864737465599996), LatLng(55.00248983,8.007480855999995))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,8.150224246399995), LatLng(55.00248983,8.292967636799995))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,8.435711027199995), LatLng(55.00248983,8.578454417599994))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,8.721197807999994), LatLng(55.00248983,8.863941198399994))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,9.006684588799994), LatLng(55.00248983,9.149427979199993))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,9.292171369599993), LatLng(55.00248983,9.434914759999993))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,9.577658150399992), LatLng(55.00248983,9.720401540799992))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,9.863144931199992), LatLng(55.00248983,10.005888321599992))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,10.148631711999991), LatLng(55.00248983,10.291375102399991))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,10.43411849279999), LatLng(55.00248983,10.57686188319999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,10.71960527359999), LatLng(55.00248983,10.86234866399999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,11.00509205439999), LatLng(55.00248983,11.14783544479999))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,11.29057883519999), LatLng(55.00248983,11.433322225599989))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,11.576065615999989), LatLng(55.00248983,11.718809006399988))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,11.861552396799988), LatLng(55.00248983,12.004295787199988))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,12.147039177599988), LatLng(55.00248983,12.289782567999987))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,12.432525958399987), LatLng(55.00248983,12.575269348799987))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,12.718012739199986), LatLng(55.00248983,12.860756129599986))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,13.003499519999986), LatLng(55.00248983,13.146242910399986))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,13.288986300799985), LatLng(55.00248983,13.431729691199985))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,13.574473081599985), LatLng(55.00248983,13.717216471999985))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,13.859959862399984), LatLng(55.00248983,14.002703252799984))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,14.145446643199984), LatLng(55.00248983,14.288190033599983))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
        GroundOverlay(position = GroundOverlayPosition.create(LatLngBounds(LatLng(54.91205266,14.430933423999983), LatLng(55.00248983,14.573676814399983))), image = BitmapDescriptorFactory.fromResource(R.drawable.grauer_pixel))
    */
    }
}
