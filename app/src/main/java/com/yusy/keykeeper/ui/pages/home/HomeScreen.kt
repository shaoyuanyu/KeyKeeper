package com.yusy.keykeeper.ui.pages.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.data.account.accountExampleAndroid
import com.yusy.keykeeper.ui.AppViewModelProvider
import com.yusy.keykeeper.ui.components.accountcard.AccountCard
import com.yusy.keykeeper.ui.components.accountcard.AccountPreview
import com.yusy.keykeeper.ui.components.accountcard.toAccountPreview
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.MySecondLevelDestination
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@Composable
fun HomeScreen(
    myNavActions: MyNavActions,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.updateAccountPreviewList()
    }

    HomeBody(
        accountPreviewList = viewModel.homeUiState.resultAccountPreviewList,
        searchWord = viewModel.homeUiState.searchWord,
        onSearchWordChange = {
             viewModel.updateSearchWord(it)
        },
        onCardClick = { id ->
            myNavActions.navigateTo(
                MySecondLevelDestination(
                    route = MyRoutes.ACCOUNT_EDIT_PAGE,
                    param = id.toString()
                )
            )
        },
        onCreateClick = {
            myNavActions.navigateTo(
                MySecondLevelDestination(
                    route = MyRoutes.ACCOUNT_CREATE_PAGE
                )
            )
        },
        modifier = modifier
    )
}

@Composable
fun HomeBody(
    accountPreviewList: List<AccountPreview>,
    searchWord: String,
    onSearchWordChange: (String) -> Unit,
    onCardClick: (Int) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            OutlinedTextField(
                value = searchWord,
                onValueChange = {
                    onSearchWordChange(it)
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp),
                singleLine = true,
            )

            LazyColumn(
                modifier = modifier.fillMaxSize(),
            ) {
                items(accountPreviewList) { accountPreview ->
                    AccountCard(
                        accountPreview = accountPreview,
                        onClick = { onCardClick(accountPreview.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { onCreateClick() },
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

@Preview
@Composable
fun HomeScreenPreview() {
    val accountPreviewList: List<AccountPreview> = List(10) {
        accountExampleAndroid.toAccountPreview()
    }

    KeyKeeperTheme {
        HomeBody(
            accountPreviewList = accountPreviewList,
            searchWord = "",
            onSearchWordChange = {},
            onCardClick = {},
            onCreateClick = {}
        )
    }
}