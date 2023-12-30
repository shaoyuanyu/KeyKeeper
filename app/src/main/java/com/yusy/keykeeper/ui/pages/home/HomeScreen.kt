package com.yusy.keykeeper.ui.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.R
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
        sortTypeList = viewModel.homeUiState.sortTypeList,
        currentSortType = viewModel.homeUiState.currentSortType,
        onSearchWordChange = {
            viewModel.updateSearchWord(it)
        },
        onSortTypeChoose = {
            viewModel.updateSortType(it)
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
    sortTypeList: List<SortType>,
    currentSortType: SortType,
    onSearchWordChange: (String) -> Unit,
    onSortTypeChoose: (SortType) -> Unit,
    onCardClick: (Int) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            TopBar(
                searchWord = searchWord,
                sortTypeList = sortTypeList,
                currentSortType = currentSortType,
                onSearchWordChange = onSearchWordChange,
                onSortTypeChoose = onSortTypeChoose
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

@Composable
fun TopBar(
    searchWord: String,
    sortTypeList: List<SortType>,
    currentSortType: SortType,
    onSearchWordChange: (String) -> Unit,
    onSortTypeChoose: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    var sortChoiceShow by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = searchWord,
            onValueChange = { onSearchWordChange(it) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                IconButton(
                    onClick = { sortChoiceShow = true },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.ic_sort), contentDescription = null)
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp),
            singleLine = true,
        )

        if (sortChoiceShow) {
            Popup(
                alignment = Alignment.BottomCenter,
                offset = IntOffset(0,170*4),
                onDismissRequest = {
                    sortChoiceShow = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 0.dp)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(3.dp))
                        .background(
                            MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(3.dp)
                        )
                ) {
                    sortTypeList.forEach {
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Text(
                                        text = it.text,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )

                                    if (currentSortType == it) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                sortChoiceShow = false
                                onSortTypeChoose(it)
                            }
                        )
                    }
                }
            }
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
            sortTypeList = listOf(SortType.ByNameASC, SortType.ByNameDESC, SortType.ByCreatedTimeASC, SortType.ByCreatedTimeDESC),
            currentSortType = SortType.ByCreatedTimeDESC,
            onSearchWordChange = {},
            onSortTypeChoose = {},
            onCardClick = {},
            onCreateClick = {}
        )
    }
}