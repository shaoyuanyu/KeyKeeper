package com.yusy.keykeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yusy.keykeeper.ui.MyNavHost
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

class MainActivity: ComponentActivity() {
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
            MyNavHost(
                modifier = Modifier.fillMaxSize(),
                myNavController = myNavController,
                myNavActions = myNavActions,
                selectedDestination = selectedDestination
            )
        }
    }
}