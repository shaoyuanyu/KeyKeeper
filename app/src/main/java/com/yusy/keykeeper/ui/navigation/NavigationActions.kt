package com.yusy.keykeeper.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yusy.keykeeper.R

object MyRouter {
    const val HOME = "Home"
    const val SEARCH = "Search"
    const val MINE = "Mine"
}


data class MyTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    MyTopLevelDestination(
        route = "Search",
        selectedIcon = Icons.Default.Search,
        unselectedIcon = Icons.Default.Search,
        iconTextId = R.string.nav_tab_search
    ),
    MyTopLevelDestination(
        route = "Home",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = R.string.nav_tab_home
    ),
    MyTopLevelDestination(
        route = "Mine",
        selectedIcon = Icons.Default.Person,
        unselectedIcon = Icons.Default.Person,
        iconTextId = R.string.nav_tab_mine
    ),
)

class MyNavActions(private val navController: NavHostController) {
    fun navigateTo(destination: MyTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
