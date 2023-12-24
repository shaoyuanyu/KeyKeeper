package com.yusy.keykeeper.ui.pages.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.AppType
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
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            val iconImgModifier = modifier
                .size(120.dp)
                .align(Alignment.Center)

            Image(
                modifier = iconImgModifier,
                painter = iconPainter(
                    appType = accountEditUiState.accountDetails.appType,
                    appIcon = accountEditUiState.accountDetails.appIcon
                ),
                contentDescription = "app icon",
            )
        }

        // input form
        EditInputForm(accountDetails = accountEditUiState.accountDetails, onValueChange = onAccountValueChange, modifier = modifier)

        // created at
        Text(
            text = stringResource(R.string.account_page_createdat) + "  " + accountEditUiState.accountDetails.createdAt,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp)
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
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.account_page_save),
                fontSize = 14.sp
            )
        }

        // delete button
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = inputModifier
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Text(
                text = stringResource(R.string.account_page_delete),
                fontSize = 14.sp
            )
        }

        // 提示词 - 比如密码不得为空
        // TODO:文本本地化
        val hintTextModifier = Modifier
            .padding(horizontal = 20.dp)
            .align(Alignment.End)
        Row(modifier = hintTextModifier) {
            Icon(
                imageVector = if (accountEditUiState.isValid) {
                    Icons.Default.Check
                } else {
                    Icons.Default.Warning
                },
                tint = if (accountEditUiState.isValid) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.error
                },
                contentDescription = ""
            )
            Text(
                text = if (accountEditUiState.accountDetails.plainPasswd.isEmpty()) {
                    "密码不得为空"
                } else if (accountEditUiState.accountDetails.appName.isEmpty()) {
                    "应用名不得为空"
                } else {
                    "数据校验成功"
                },
                color = if (accountEditUiState.isValid) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
    }
}

/**
 * Edit的输入表
 */
@Composable
fun EditInputForm(
    accountDetails: AccountDetails,
    onValueChange: (AccountDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val inputModifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .align(Alignment.CenterHorizontally)

        val hintTextModifier = Modifier
            .padding(horizontal = 20.dp)
            .align(Alignment.End)

        // account id
        // readonly
        OutlinedTextField(
            modifier = inputModifier,
            value = accountDetails.uid,
            onValueChange = { onValueChange(accountDetails.copy(uid = it)) },
            label = { Row {
                Text(stringResource(R.string.account_page_account))
            } },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") },
            singleLine = true,
            readOnly = true
        )

        // account passwd
        OutlinedTextField(
            modifier = inputModifier,
            value = accountDetails.plainPasswd,
            onValueChange = { onValueChange(accountDetails.copy(plainPasswd = it)) },
            label = { Row {
                Text(stringResource(R.string.account_page_passwd))
                Text( color = MaterialTheme.colorScheme.error, text = "*")
            } },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") },
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(R.drawable.ic_infinity), contentDescription = "")
                }
            },
            singleLine = true,
        )

        // 提示 - 点击图标生成可靠密码
        // TODO:文本本地化
        Row(modifier = hintTextModifier) {
            Text(
                text = "点击",
                color = MaterialTheme.colorScheme.tertiary
            )
            Icon(
                painter = painterResource(R.drawable.ic_infinity),
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = ""
            )
            Text(
                text = "生成可靠密码",
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        // appUrl & appName
        with (accountDetails) {
            if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
                // appUrl - app
                // readonly
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appUrl,
                    onValueChange = { onValueChange(accountDetails.copy(appUrl = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_appurl))
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_appurl)) },
                    singleLine = true,
                    readOnly = true
                )

                // appName - app
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appName,
                    onValueChange = { onValueChange(accountDetails.copy(appName = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_appname))
                        Text( color = MaterialTheme.colorScheme.error, text = "*")
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_appname)) },
                    singleLine = true,
                )
            } else if (appType == AppType.Website) {
                // appUrl - website
                // readonly
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appUrl,
                    onValueChange = { onValueChange(accountDetails.copy(appUrl = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_websiteurl))
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_websiteurl)) },
                    singleLine = true,
                    readOnly = true
                )

                // appName - website
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appName,
                    onValueChange = { onValueChange(accountDetails.copy(appName = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_websitename))
                        Text( color = MaterialTheme.colorScheme.error, text = "*")
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_websitename)) },
                    singleLine = true,
                )
            }
        }
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
