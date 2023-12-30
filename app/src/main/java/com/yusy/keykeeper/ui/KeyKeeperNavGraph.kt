package com.yusy.keykeeper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.NavigationBarUI
import com.yusy.keykeeper.ui.pages.account.AccountEditScreen
import com.yusy.keykeeper.ui.pages.account.AccountEntryScreen
import com.yusy.keykeeper.ui.pages.account.AccountEntryViewModel
import com.yusy.keykeeper.ui.pages.account.AppChooseScreen
import com.yusy.keykeeper.ui.pages.authenticator.AuthenticatorScreen
import com.yusy.keykeeper.ui.pages.home.HomeScreen
import com.yusy.keykeeper.ui.pages.mine.MineScreen

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    myNavController: NavHostController,
    myNavActions: MyNavActions,
    selectedDestination: String
) {
    // AccountEntryScreen 和 AppChooseScreen 共用一个ViewModel
    var accountEntryViewModel: AccountEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)

    // top level destination
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            modifier = if (judgeTopDestination(selectedDestination)) {
                modifier.weight(1f)
            } else {
                modifier
            },
            navController = myNavController,
            startDestination = MyRoutes.HOME,
        ) {
            // top
            composable (MyRoutes.HOME) {
                HomeScreen(myNavActions = myNavActions)
            }

            composable(MyRoutes.AUTHENTICATOR) {
                AuthenticatorScreen()
            }

            composable(MyRoutes.MINE) {
                MineScreen()
            }

            // second
            composable(MyRoutes.ACCOUNT_CREATE_PAGE) {
                accountEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)

                AccountEntryScreen(
                    myNavActions = myNavActions,
                    viewModel = accountEntryViewModel
                )
            }

            composable(
                MyRoutes.ACCOUNT_EDIT_PAGE + "/{given_id}",
                arguments = listOf(navArgument("given_id") { type = NavType.StringType })
            ) { backStackEntry ->
                AccountEditScreen(
                    myNavActions = myNavActions,
                    id = backStackEntry.arguments?.getString("given_id").toString().toInt()
                )
            }

            composable(MyRoutes.APP_CHOOSE_PAGE) {
                AppChooseScreen(
                    myNavActions = myNavActions,
                    viewModel = accountEntryViewModel
                )
            }
        }

        if (judgeTopDestination(selectedDestination)) {
            AnimatedVisibility(visible = true) {
                NavigationBarUI(
                    selectedDestination = selectedDestination,
                    myNavActions = myNavActions
                )
            }
        }
    }
}

fun judgeTopDestination(currentDestination: String): Boolean =
    (currentDestination == MyRoutes.HOME) || (currentDestination == MyRoutes.AUTHENTICATOR) || (currentDestination == MyRoutes.MINE)