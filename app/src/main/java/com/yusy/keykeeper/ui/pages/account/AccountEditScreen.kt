package com.yusy.keykeeper.ui.pages.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.accountExampleAndroid
import com.yusy.keykeeper.data.account.accountExampleWebsite
import com.yusy.keykeeper.ui.AppViewModelProvider
import com.yusy.keykeeper.ui.components.iconpainter.iconPainter
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme
import kotlinx.coroutines.launch

@Composable
fun AccountEditScreen(
    myNavActions: MyNavActions,
    id: Int,
    modifier: Modifier = Modifier,
    viewModel: AccountEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setId(id)
    }

    AccountEditBody(
        accountEditUiState = viewModel.accountEditUiState,
        onAccountValueChange = viewModel::updateUiState,
        onSave = {
            coroutineScope.launch {
                viewModel.saveAccount()
                myNavActions.navigateBack()
                // TODO:弹窗文本本地化
                Toast.makeText(context, "修改成功，密码已为您复制到剪切板", Toast.LENGTH_LONG).show()
            }
        },
        modifier = modifier
    )
}

@Composable
fun AccountEditBody(
    accountEditUiState: AccountEditUiState,
    onAccountValueChange: (AccountDetails) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val inputModifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .align(Alignment.CenterHorizontally)

        // app icon
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            val iconImgModifier = modifier
                .size(150.dp)
                .clip(CircleShape)
                .align(Alignment.Center)

            Image(
                modifier = iconImgModifier,
                painter = iconPainter(appIcon = accountEditUiState.accountDetails.appIcon),
                contentDescription = "app icon",
            )
        }

        // input form
        EditInputForm(accountDetails = accountEditUiState.accountDetails, onValueChange = onAccountValueChange, modifier = modifier)

        // created at
        Text(
            text = stringResource(R.string.account_page_createdat) + "  " + accountEditUiState.accountDetails.createdAt,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = inputModifier
        )

        // save button
        Button(
            shape = MaterialTheme.shapes.medium,
            enabled = accountEditUiState.isValid,
            onClick = {
                if (accountEditUiState.isValid) {
                    onSave()
                }
            },
            modifier = inputModifier
        ) {
            Text(text = stringResource(R.string.account_page_save))
        }

        // hint text
        // TODO:增加提示词，如：密码不得为空
    }
}

@Preview
@Composable
fun AccountEditScreenPreview_Android() {
    KeyKeeperTheme {
        AccountEditBody(
            accountEditUiState = accountExampleAndroid.toAccountDetails().toAccountEditUiState(true),
            onSave = {},
            onAccountValueChange = {}
        )
    }
}

@Preview
@Composable
fun AccountEditScreenPreview_Website() {
    KeyKeeperTheme {
        AccountEditBody(
            accountEditUiState = accountExampleWebsite.toAccountDetails().toAccountEditUiState(true),
            onSave = {},
            onAccountValueChange = {}
        )
    }
}
