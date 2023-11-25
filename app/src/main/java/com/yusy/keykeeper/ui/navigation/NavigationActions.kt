package com.yusy.keykeeper.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yusy.keykeeper.R

object MyRoutes {
    // top level
    const val HOME = "Home"
    const val SEARCH = "Search"
    const val MINE = "Mine"

    // second level
    const val SETTING = "Setting"
    const val ACCOUNT_CREATE_PAGE = "AccountCreatePage"
    const val ACCOUNT_EDIT_PAGE = "AccountEditPage"
}

data class MyTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

data class MySecondLevelDestination(
    val route: String,
    val param: String = ""
)


val TOP_LEVEL_DESTINATIONS = listOf(
    MyTopLevelDestination(
        route = MyRoutes.SEARCH,
        selectedIcon = Icons.Default.Search,
        unselectedIcon = Icons.Default.Search,
        iconTextId = R.string.nav_tab_search
    ),
    MyTopLevelDestination(
        route = MyRoutes.HOME,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = R.string.nav_tab_home
    ),
    MyTopLevelDestination(
        route = MyRoutes.MINE,
        selectedIcon = Icons.Default.Person,
        unselectedIcon = Icons.Default.Person,
        iconTextId = R.string.nav_tab_mine
    ),
)

class MyNavActions(private val navController: NavHostController) {
    fun navigateTo(destination: MyTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true

            restoreState = false
        }
    }

    fun navigateTo(destination: MySecondLevelDestination) {
        var routeString: String
        if (destination.param.isEmpty()) {
            routeString = destination.route
        } else {
            routeString = destination.route + "/" + destination.param
        }

        navController.navigate(routeString) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
