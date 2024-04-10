package de.max.trailblazerfrontendv1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val primaryIcon: ImageVector,
    val secondaryIcon: ImageVector,
    val route: String
)

val listOfNavItems = listOf(
    NavItem(
        label = "Karte",
        primaryIcon = Icons.Filled.Map,
        secondaryIcon = Icons.Outlined.Map,
        route = Screens.MapScreen.name
    ),
    NavItem(
        label = "Stats",
        primaryIcon = Icons.Filled.InsertChart,
        secondaryIcon = Icons.Outlined.InsertChart,
        route = Screens.StatsScreen.name
    ),
    NavItem(
        label = "Profil",
        primaryIcon = Icons.Filled.Person,
        secondaryIcon = Icons.Outlined.Person,
        route = Screens.ProfileScreen.name
    ),
    NavItem(
        label = "Optionen",
        primaryIcon = Icons.Filled.Settings,
        secondaryIcon = Icons.Outlined.Settings,
        route = Screens.SettingsScreen.name
    )
)