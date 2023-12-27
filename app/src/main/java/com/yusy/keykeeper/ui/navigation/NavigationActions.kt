package com.yusy.keykeeper.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yusy.keykeeper.R

object MyRoutes {
    // top level
    const val HOME = "Home"
    const val AUTHENTICATOR = "Authenticator"
    const val MINE = "Mine"

    // second level
    const val SETTING = "Setting"
    const val ACCOUNT_CREATE_PAGE = "AccountCreatePage"
    const val ACCOUNT_EDIT_PAGE = "AccountEditPage"
    const val APP_CHOOSE_PAGE = "AppChoosePage"
}

data class MyTopLevelDestination(
    val route: String,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int
)

data class MySecondLevelDestination(
    val route: String,
    val param: String = ""
)


val TOP_LEVEL_DESTINATIONS = listOf(
    MyTopLevelDestination(
        route = MyRoutes.AUTHENTICATOR,
        selectedIconId = R.drawable.ic_shield_person,
        unselectedIconId = R.drawable.ic_shield_person,
        iconTextId = R.string.nav_tab_authenticator
    ),
    MyTopLevelDestination(
        route = MyRoutes.HOME,
        selectedIconId = R.drawable.ic_encrypted,
        unselectedIconId = R.drawable.ic_encrypted,
        iconTextId = R.string.nav_tab_home
    ),
    MyTopLevelDestination(
        route = MyRoutes.MINE,
        selectedIconId = R.drawable.ic_manage_accounts,
        unselectedIconId = R.drawable.ic_manage_accounts,
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
            // re selecting the same item
            launchSingleTop = true

            restoreState = false
        }
    }

    fun navigateTo(destination: MySecondLevelDestination) {
        val routeString = if (destination.param.isEmpty()) {
            destination.route
        } else {
            destination.route + "/" + destination.param
        }

        navController.navigate(routeString) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            // Restore state when re selecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToChild(destination: MySecondLevelDestination) {
        val routeString = if (destination.param.isEmpty()) {
            destination.route
        } else {
            destination.route + "/" + destination.param
        }

        navController.navigate(routeString) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
