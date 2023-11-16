package com.yusy.keykeeper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme
import com.yusy.keykeeper.ui.HomeUI

class KeyKeeper: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeyKeeperTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    Scaffold(
                        modifier = Modifier,
                        content = { innerPadding ->
                            HomeUI(innerPadding)
                        },
                        bottomBar = { NavigationUI() },
                    )
                }

            }
        }
    }
}
