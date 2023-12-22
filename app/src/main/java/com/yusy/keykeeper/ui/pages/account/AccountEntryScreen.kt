package com.yusy.keykeeper.ui.pages.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.ui.AppViewModelProvider
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@Composable
fun AccountEntryScreen(
    viewModel: AccountEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    AccountEntryBody(
        accountUiState = viewModel.accountUiState,
        onAccountValueChange = viewModel::updateUiState,
        modifier = modifier
    )
}

@Composable
fun AccountEntryBody(
    accountUiState: AccountUiState,
    onAccountValueChange: (AccountDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val inputModifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .align(Alignment.CenterHorizontally)

        with (accountUiState.accountDetails) {
            // app icon
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Image(
                    modifier = modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    painter = painterResource(id = accountUiState.accountDetails.appIcon),
                    contentDescription = "app icon",
                )
            }

            // input form
            entryInputForm(accountDetails = accountUiState.accountDetails, onValueChange = onAccountValueChange, modifier = modifier)

            // save button
            SaveButton(modifier = inputModifier, onClick = {})
        }
    }
}

@Preview
@Composable
fun AccountEntryScreenPreview() {
    KeyKeeperTheme {
        AccountEntryBody(
            accountUiState = AccountUiState(),
            onAccountValueChange = {}
        )
    }
}
