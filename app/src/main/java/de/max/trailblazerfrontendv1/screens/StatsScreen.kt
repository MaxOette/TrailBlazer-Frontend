package de.max.trailblazerfrontendv1.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.CountyStats
import de.max.trailblazerfrontendv1.Api.StatsApi
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import java.util.Locale

@Composable
fun StatsScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp)
        ) {
            Text(
                text = "Stats",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            var countyStats by remember { mutableStateOf(emptyList<CountyStats>()) }

            LaunchedEffect(key1 = Unit) {
                try {
                    countyStats = StatsApi.statsService.getStats()
                    countyStats.forEach { county ->
                        county.imageResource = GeneralConstants.applicationContext.resources.getIdentifier(county.kuerzel.lowercase(), "drawable", GeneralConstants.applicationContext.packageName)
                        county.kuerzel = countyNameFromKuerzel(county.kuerzel)
                    }
                    countyStats = countyStats.sortedWith(compareByDescending<CountyStats> { it.kuerzel == "Deutschland" }.thenBy { it.kuerzel })
                    println(countyStats)
                } catch (e: Error) {
                    println(e.message)
                }
            }

            LazyColumn {
                itemsIndexed(countyStats) { index, county ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(county.imageResource),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = county.kuerzel, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = county.percentage.div(100f),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${String.format("%.3f", county.percentage)}%",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun countyNameFromKuerzel(kuerzel: String): String {
    when (kuerzel) {
        "DE" -> return "Deutschland"
        "BW" -> return "Baden-Württemberg"
        "BY" -> return "Bayern"
        "BE" -> return "Berlin"
        "BB" -> return "Brandenburg"
        "HB" -> return "Bremen"
        "HH" -> return "Hamburg"
        "HE" -> return "Hessen"
        "MV" -> return "Mecklenburg-Vorpommern"
        "NI" -> return "Niedersachsen"
        "NW" -> return "Nordrhein-Westfalen"
        "RP" -> return "Rheinland-Pfalz"
        "SL" -> return "Saarland"
        "SN" -> return "Sachsen"
        "ST" -> return "Sachsen-Anhalt"
        "SH" -> return "Schleswig-Holstein"
        "TH" -> return "Thüringen"
        else -> return "unbekannt"
    }
}