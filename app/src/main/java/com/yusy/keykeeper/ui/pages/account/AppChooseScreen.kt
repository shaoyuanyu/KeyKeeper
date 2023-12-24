package com.yusy.keykeeper.ui.pages.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.ui.AppViewModelProvider
import com.yusy.keykeeper.ui.components.accountcard.AccountCard
import com.yusy.keykeeper.ui.components.accountcard.AccountPreview
import com.yusy.keykeeper.ui.navigation.MyNavActions

@Composable
fun AppChooseScreen(
    myNavActions: MyNavActions,
    modifier: Modifier = Modifier,
    viewModel: AccountEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getDeskAppList(context)
    }

    AppChooseBody(
        appChooseUiState = viewModel.appChooseUiState,
        onAppChoose = {
            viewModel.chooseApp(it)
            myNavActions.navigateBack()
        },
        modifier = modifier
    )
}

@Composable
fun AppChooseBody(
    appChooseUiState: AppChooseUiState,
    onAppChoose: (LocalDeskApp) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier,
        ) {
            items(appChooseUiState.localDeskAppList) {
                // TODO:此处复用AccountCard先顶着
                AccountCard(
                    accountPreview = AccountPreview(
                        id = 0,
                        uid = "",
                        appName = it.appName,
                        appUrl = it.packageName,
                        appIcon = it.appIcon,
                        createdAt = ""
                    ),
                    onClick = { onAppChoose(it) }
                )
            }
        }
    }
}
