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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.StatsApi
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import java.util.Locale

fun countyCodeFromIdx(index: Int): String {
    when (index) {
        0 -> return "bw"
        1 -> return "by"
        2 -> return "be"
        3 -> return "bb"
        4 -> return "hb"
        5 -> return "hh"
        6 -> return "he"
        7 -> return "mv"
        8 -> return "ni"
        9 -> return "nw"
        10 -> return "rp"
        11 -> return "sl"
        12 -> return "sn"
        13 -> return "st"
        14 -> return "sh"
        15 -> return "th"
        else -> return "trail_blazer_app_icon"
    }
}

fun countyNameFromIdx(index: Int): String {
    when (index) {
        0 -> return "Baden-Württemberg"
        1 -> return "Bayern"
        2 -> return "Berlin"
        3 -> return "Brandenburg"
        4 -> return "Bremen"
        5 -> return "Hamburg"
        6 -> return "Hessen"
        7 -> return "Mecklenburg-Vorpommern"
        8 -> return "Niedersachsen"
        9 -> return "Nordrhein-Westfalen"
        10 -> return "Rheinland-Pfalz"
        11 -> return "Saarland"
        12 -> return "Sachsen"
        13 -> return "Sachsen-Anhalt"
        14 -> return "Schleswig-Holstein"
        15 -> return "Thüringen"
        else -> return "x"
    }
}
@Composable
fun StatsScreenOld() {
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

            val statsData: MutableMap<String, Float> = remember {
                mutableMapOf(
                    "DE" to 99.000f,
                    "BY" to 99.000f,
                    "BW" to 99.000f,
                    "BB" to 99.000f,
                    "BE" to 99.000f,
                    "HB" to 99.000f,
                    "HH" to 99.000f,
                    "HE" to 99.000f,
                    "MV" to 99.000f,
                    "NI" to 99.000f,
                    "NW" to 99.000f,
                    "RP" to 99.000f,
                    "SL" to 99.000f,
                    "SN" to 99.000f,
                    "ST" to 99.000f,
                    "SH" to 99.000f,
                    "TH" to 99.000f
                ) }

            LaunchedEffect(key1 = Unit) {
                try {
                    val response = StatsApi.statsService.getStats()
                    println(response)

                    for (county in response) {
                        statsData[county.kuerzel] = county.percentage
                    }
                    println("-----------------vvv---------------")
                    println(statsData)


                } catch (e: Error) {
                    println(e.message)
                }

            }

            LazyColumn {
                item {
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
                                painter = painterResource(id = R.drawable.de),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = "Deutschland", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                if (statsData["DE"] != null) {
                                    statsData["DE"]?.let {
                                        LinearProgressIndicator(
                                            progress = it,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "${statsData["DE"]}%", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                items(16) { index ->
                    val countyCode = countyCodeFromIdx(index)
                    val countyName = countyNameFromIdx(index)
                    val resourceId = GeneralConstants.applicationContext.resources.getIdentifier(
                        countyCode,
                        "drawable",
                        GeneralConstants.applicationContext.packageName
                    )
                    val visitValue = statsData[countyCode.uppercase(Locale.ROOT)]

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
                                painter = painterResource(resourceId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = countyName, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = visitValue?.div(100f) ?: 0.000f,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "${visitValue}%", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}