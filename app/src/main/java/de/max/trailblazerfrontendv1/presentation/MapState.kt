package de.max.trailblazerfrontendv1.presentation

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

/**
 * NORMAL = Flache Karte mit Daten/Infos
 * TERRAIN = Höhenprofilkarte mit Daten/Infos
 * SATELLITE = Satellitenbilder ohne Daten/Infos
 * HYBRID = Satellitenbilder mit Daten/Infos
 * NONE = Keine Karte sichtbar
 *
 * => Erste Empfehlung: TERRAIN nutzen
 *
 * #####################################################
 * Zoomlevelauswirkung auf Straßen (gilt für SATELLITE)
 * #####################################################
 * 0 = ganz nah
 * 1..2..3 = man sieht Straßenoberfläche
 * 4+ = man sieht nur schwarze Streifen
 * 8+ = man sieht wieder Straßenoberfläche
 * 10+ = Straßen irrelevant weil zu weit weg
 *
 * #####################################################
 * Mitte von Deutschland: 50.856361,10.0667911
 * #####################################################
 *
 */
data class MapState(
    val properties: MapProperties = MapProperties(mapType = MapType.TERRAIN)
)
