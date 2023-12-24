package com.yusy.keykeeper.ui.pages.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
                AppCard(
                    localDeskApp = it,
                    onClick = { onAppChoose(it) },
                    modifier = modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(
    localDeskApp: LocalDeskApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    painter = iconPainter(appIcon = localDeskApp.appIcon),
                    contentDescription = "app icon",
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = localDeskApp.appName,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = localDeskApp.packageName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
