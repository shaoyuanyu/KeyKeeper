package com.yusy.keykeeper.ui.pages.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.ui.AppViewModelProvider
import com.yusy.keykeeper.ui.components.iconpainter.iconPainter
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
        onSearchValueChange = {
            viewModel.updateAppChooseUiState(it)
            viewModel.searchApp()
        },
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
    onSearchValueChange: (String) -> Unit,
    onAppChoose: (LocalDeskApp) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val inputModifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)

        OutlinedTextField(
            modifier = inputModifier,
            value = appChooseUiState.targetName,
            onValueChange = {
                onSearchValueChange(it)
            },
            label = { Row { Text("搜索本地App") } },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (appChooseUiState.isReadingLocalDeskApp) {
                    CircularProgressIndicator()
                }
            },
            singleLine = true,
        )

        LazyColumn(
            modifier = modifier,
        ) {
            items(appChooseUiState.targetAppList) {
                AppCard(
                    localDeskApp = it,
                    onClick = { onAppChoose(it) },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun AppCard(
    localDeskApp: LocalDeskApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                enabled = true,
                onClick = onClick
            )
    ) {
        Image(
            modifier = modifier
                .size(50.dp),
            painter = iconPainter(
                appType = AppType.AndroidAPP,
                appIcon = localDeskApp.appIcon
            ),
            contentDescription = "app icon",
        )

        Column(
            modifier = modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = localDeskApp.appName,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = localDeskApp.packageName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
