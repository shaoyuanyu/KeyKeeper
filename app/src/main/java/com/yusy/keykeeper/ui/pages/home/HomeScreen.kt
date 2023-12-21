package com.yusy.keykeeper.ui.pages.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.Account
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.ui.components.AccountCard
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.MySecondLevelDestination

@Composable
fun HomeUI(
    innerPadding: PaddingValues,
    myNavActions: MyNavActions
) {
    val testAccount = Account(
        id = 0,
        uid = "account",
        encryptedPasswd = "",
        encryptFunc = "",
        appType = AppType.AndroidAPP,
        appName = "test",
        appUrl = "com.yusy.test",
        appIcon = R.drawable.ic_launcher_foreground,
        createdAt = "2023/11/16"
    )
    val testAccountDataList = List(10){testAccount}

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            items(testAccountDataList) { item ->
                AccountCard(
                    account = item,
                    onClick = {
                        myNavActions.navigateTo(
                            MySecondLevelDestination(
                                route = MyRoutes.ACCOUNT_EDIT_PAGE,
                                param = item.id.toString()
                            )
                        )
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                myNavActions.navigateTo(
                    MySecondLevelDestination(
                        route = MyRoutes.ACCOUNT_CREATE_PAGE
                    )
                )
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(80.dp)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}