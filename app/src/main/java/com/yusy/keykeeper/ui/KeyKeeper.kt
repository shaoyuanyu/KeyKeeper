package com.yusy.keykeeper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRouter
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
    val selectedDestination = navBackStackEntry?.destination?.route ?: MyRouter.HOME

    KeyKeeperTheme {
        Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
            Scaffold(
                modifier = Modifier,
                content = { innerPadding ->
                    MyNavHost(
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
        }
    }
}

@Composable
fun MyNavHost(
    myNavController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = myNavController,
        startDestination = MyRouter.HOME,
    ) {
        composable(MyRouter.HOME) {
            HomeUI(innerPadding = innerPadding)
        }
        composable(MyRouter.SEARCH) {
            SearchUI()
        }
        composable(MyRouter.MINE) {
            MineUI()
        }
    }
}