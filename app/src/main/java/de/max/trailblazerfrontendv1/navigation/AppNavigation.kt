package de.max.trailblazerfrontendv1.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.max.trailblazerfrontendv1.screens.GoalsScreen
import de.max.trailblazerfrontendv1.screens.MapScreen
import de.max.trailblazerfrontendv1.screens.ProfileScreen
import de.max.trailblazerfrontendv1.screens.Screens
import de.max.trailblazerfrontendv1.screens.SettingsScreen
import de.max.trailblazerfrontendv1.screens.StatsScreen

@Composable
fun AppNavigation(applicationContext: Context) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
            
                listOfNavItems.forEach {navItem ->
                    val active = currentDestination?.hierarchy?.any {it.route == navItem.route} == true
                    NavigationBarItem(
                        selected = active,
                        onClick = { navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = {
                            if (active) {
                                Icon(imageVector = navItem.primaryIcon, contentDescription = null)
                            } else {
                                Icon(imageVector = navItem.secondaryIcon, contentDescription = null)
                            }
                        },
                        label = {Text(text = navItem.label)}
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.MapScreen.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.MapScreen.name) {
                MapScreen()
            }
            composable(route = Screens.StatsScreen.name) {
                StatsScreen()
            }
            composable(route = Screens.GoalsScreen.name) {
                GoalsScreen()
            }
            composable(route = Screens.ProfileScreen.name) {
                ProfileScreen()
            }
            composable(route = Screens.SettingsScreen.name) {
                SettingsScreen(applicationContext)
            }
        }
    }
}