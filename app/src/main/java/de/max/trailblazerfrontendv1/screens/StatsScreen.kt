package de.max.trailblazerfrontendv1.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.max.trailblazerfrontendv1.Api.TileApi
import de.max.trailblazerfrontendv1.Api.TileData
import de.max.trailblazerfrontendv1.Util.UserConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun StatsScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Stats Screen"
        )
        Button(
            onClick = {
                println("getTiles clicked --------------------")
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = TileApi.tileService.getTiles(UserConstants.userLat, UserConstants.userLng, 14)
                        val tileData : List<TileData> = response
                        UserConstants.testTileData = tileData
                        println("" + tileData.size + " Kacheln geladen.")
                        println(tileData)
                    }   catch(e: Error) {
                        println(e.message)
                    }
                }
            }
        ){
            Text( text = "get Tiles")
        }
    }
}