package com.yusy.keykeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yusy.keykeeper.ui.MyNavHost
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.NavigationBarUI
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

// class MainActivity: ComponentActivity() {
// Biometric Auth要求FragmentActivity
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                MyNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    myNavController = myNavController,
                    myNavActions = myNavActions
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