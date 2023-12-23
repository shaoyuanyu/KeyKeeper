package com.yusy.keykeeper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.NavigationBarUI
import com.yusy.keykeeper.ui.pages.account.AccountEditScreen
import com.yusy.keykeeper.ui.pages.account.AccountEntryScreen
import com.yusy.keykeeper.ui.pages.home.HomeScreen
import com.yusy.keykeeper.ui.pages.mine.MineUI
import com.yusy.keykeeper.ui.pages.search.SearchUI
import com.yusy.keykeeper.ui.pages.setting.SettingUI
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@Composable
fun MyContent() {
    val myNavController = rememberNavController()
    val myNavActions = remember(myNavController) {
        MyNavActions(myNavController)
    }

    val navBackStackEntry by myNavController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: MyRoutes.HOME

    KeyKeeperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                MyNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    myNavController = myNavController,
                    myNavActions = myNavActions
//                    innerPadding = PaddingValues(8.dp)
                )

                AnimatedVisibility(visible = true) {
                    NavigationBarUI(
                        selectedDestination = selectedDestination,
                        myNavActions = myNavActions
                    )
                }
            }
        }
    }
}

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    myNavController: NavHostController,
    myNavActions: MyNavActions
//    innerPadding: PaddingValues
) {
    NavHost(
        modifier = modifier,
        navController = myNavController,
        startDestination = MyRoutes.HOME,
    ) {
        // top level destination
        composable(MyRoutes.HOME) {
            HomeScreen(myNavActions = myNavActions)
        }
        composable(MyRoutes.SEARCH) {
            SearchUI()
        }
        composable(MyRoutes.MINE) {
            MineUI()
        }

        // second level destination
        composable(MyRoutes.SETTING) {
            SettingUI()
        }
        composable(MyRoutes.ACCOUNT_CREATE_PAGE) {
            AccountEntryScreen(myNavActions = myNavActions)
        }
        composable(
            MyRoutes.ACCOUNT_EDIT_PAGE + "/{given_id}",
            arguments = listOf(navArgument("given_id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("given_id").toString().toInt()

            AccountEditScreen(
                myNavActions = myNavActions,
                id = id
            )
        }
    }
}