package com.yusy.keykeeper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.NavigationBarUI
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

class KeyKeeper: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyContent()
        }
    }
}

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
            /*
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                content = { innerPadding ->
                    MyNavHost(
                        modifier = Modifier.fillMaxSize(),
                        myNavController = myNavController,
                        innerPadding = innerPadding
                    )
                },
                bottomBar = {
                    NavigationBarUI(
                        selectedDestination = selectedDestination,
                        myNavActions = myNavActions
                    )
                },
            )
            */

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                MyNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    myNavController = myNavController,
                    innerPadding = PaddingValues(8.dp)
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
    innerPadding: PaddingValues
) {
    NavHost(
        modifier = modifier,
        navController = myNavController,
        startDestination = MyRoutes.HOME,
    ) {
        // top level destination
        composable(MyRoutes.HOME) {
            HomeUI(innerPadding = innerPadding)
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
            AccountCreatePageUI()
        }
        composable(MyRoutes.ACCOUNT_EDIT_PAGE) {
            AccountEditPageUI()
        }
    }
}