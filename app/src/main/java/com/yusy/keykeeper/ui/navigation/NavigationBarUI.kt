package com.yusy.keykeeper.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NavigationBarUI(
    myNavActions: MyNavActions,
    selectedDestination: String
) {
    NavigationBar {
        TOP_LEVEL_DESTINATIONS.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.selectedIconId),
                        contentDescription = stringResource(id = item.iconTextId)
                    )
                },
                label = { Text(stringResource(id = item.iconTextId)) },
                selected = (selectedDestination == item.route),
                onClick = {
                    myNavActions.navigateTo(item)
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewNavigationUI() {
    NavigationBar {
        TOP_LEVEL_DESTINATIONS.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(item.selectedIconId),
                        contentDescription = stringResource(id = item.iconTextId)
                    )
                },
                label = { Text(stringResource(id = item.iconTextId)) },
                selected = (false),
                onClick = {}
            )
        }
    }
}