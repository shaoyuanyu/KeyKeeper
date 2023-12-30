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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    val clipboardManager = LocalClipboardManager.current
    var openDeleteDialog by remember { mutableStateOf(false) }
    var openSaveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setId(id)
    }

    AccountEditBody(
        accountEditUiState = viewModel.accountEditUiState,
        onAccountValueChange = viewModel::updateAccountEditUiState,
        onPasswdVisibleChange = {
            viewModel.changePasswdVisible()
            clipboardManager.setText(AnnotatedString(viewModel.accountEditUiState.accountDetails.plainPasswd))
            // TODO:弹窗文本本地化
            Toast.makeText(context, "密码已为您复制到剪切板", Toast.LENGTH_SHORT).show()
        },
        onGeneratePasswd = {
            viewModel.generateSecurePasswd()
        },
        onSave = {
            // 显示弹窗
            openSaveDialog = true
        },
        onDelete = {
            // 显示弹窗
            openDeleteDialog = true
        },
        modifier = modifier
    )

    if (openDeleteDialog) {
        AlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            title = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            text = {
                Text(text = "删除后账号和密码数据无法恢复，是否确认删除？")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDeleteDialog = false

                        coroutineScope.launch {
                            viewModel.deleteAccount()
                            myNavActions.navigateBack()
                            // TODO:弹窗文本本地化
                            Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text(text = "确认")
                }
            },
            dismissButton = {
                Button(
                    onClick = { openDeleteDialog = false }
                ) {
                    Text(text = "取消")
                }
            },
        )
    }

    if (openSaveDialog) {
        AlertDialog(
            onDismissRequest = { openSaveDialog = false },
            title = {
                Icon(Icons.Default.Warning, contentDescription = null)
            },
            text = {
                Text(text = "保存后将覆盖原有的账号数据，是否确认保存？")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openSaveDialog = false

                        coroutineScope.launch {
                            viewModel.saveAccount()
                            clipboardManager.setText(AnnotatedString(viewModel.accountEditUiState.accountDetails.plainPasswd))
                            myNavActions.navigateBack()
                            // TODO:弹窗文本本地化
                            Toast.makeText(context, "修改成功，密码已为您复制到剪切板", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text(text = "确认")
                }
            },
            dismissButton = {
                Button(
                    onClick = { openSaveDialog = false }
                ) {
                    Text(text = "取消")
                }
            },
        )
    }
}

@Composable
fun AccountEditBody(
    accountEditUiState: AccountEditUiState,
    onAccountValueChange: (AccountDetails) -> Unit,
    onPasswdVisibleChange: () -> Unit,
    onGeneratePasswd: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
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
        EditInputForm(
            accountDetails = accountEditUiState.accountDetails,
            isPasswdVisible = accountEditUiState.isPasswdVisible,
            onPasswdVisibleChange = onPasswdVisibleChange,
            onGeneratePasswd = onGeneratePasswd,
            onValueChange = onAccountValueChange,
            modifier = modifier
        )

        // created at
        Text(
            text = stringResource(R.string.account_page_createdat) + "  " + accountEditUiState.accountDetails.createdAt,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
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
                contentDescription = null
            )
            Text(
                text = if (accountEditUiState.accountDetails.plainPasswd.isEmpty()) {
                    "密码不得为空"
                } else if (accountEditUiState.accountDetails.appName.isEmpty()) {
                    "应用名不得为空"
                } else if (!accountEditUiState.isValid) {
                    "数据未更改"
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

        // delete button
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = { onDelete() },
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
    }
}

/**
 * Edit的输入表
 */
@Composable
fun EditInputForm(
    accountDetails: AccountDetails,
    isPasswdVisible: Boolean,
    onPasswdVisibleChange: () -> Unit,
    onValueChange: (AccountDetails) -> Unit,
    onGeneratePasswd: () -> Unit,
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
            leadingIcon = { Icon(painter = painterResource(R.drawable.ic_edit_off), contentDescription = null) },
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
            leadingIcon = { Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = null) },
            trailingIcon = {
                Row {
                    IconButton(onClick = onPasswdVisibleChange) {
                        if (isPasswdVisible) {
                            Icon(painter = painterResource(R.drawable.ic_visibility), contentDescription = null)
                        } else {
                            Icon(painter = painterResource(R.drawable.ic_visibility_off), contentDescription = null)
                        }
                    }
                    IconButton(onClick = onGeneratePasswd) {
                        Icon(painter = painterResource(R.drawable.ic_infinity), contentDescription = null)
                    }
                }
            },
            singleLine = true,
            visualTransformation = if (isPasswdVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                contentDescription = null
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
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_edit_off), contentDescription = null) },
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
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = null) },
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
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_edit_off), contentDescription = null) },
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
                    leadingIcon = { Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = null) },
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
            onAccountValueChange = {},
            onPasswdVisibleChange = {},
            onGeneratePasswd = {},
            onDelete = {},
            onSave = {}
        )
    }
}

@Preview
@Composable
fun AccountEditScreenPreview_Website() {
    KeyKeeperTheme {
        AccountEditBody(
            accountEditUiState = accountExampleWebsite.toAccountDetails().toAccountEditUiState(true),
            onAccountValueChange = {},
            onPasswdVisibleChange = {},
            onGeneratePasswd = {},
            onDelete = {},
            onSave = {}
        )
    }
}
