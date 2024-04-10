package de.max.trailblazerfrontendv1.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavBarItem(val route: String, val icon: ImageVector, val label: String) {
    object Map : BottomNavBarItem("search", Icons.Default.Map, "Karte")
    object Stats : BottomNavBarItem("home", Icons.Default.AutoGraph, "Stats")
    object Profile : BottomNavBarItem("profile", Icons.Default.Person, "Profil")
    object Settings : BottomNavBarItem("profile", Icons.Default.Settings, "Einstellungen")

}
