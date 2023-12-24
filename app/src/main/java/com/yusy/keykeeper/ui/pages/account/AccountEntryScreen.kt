package com.yusy.keykeeper.ui.pages.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.data.account.accountExampleAndroid
import com.yusy.keykeeper.data.account.accountExampleWebsite
import com.yusy.keykeeper.ui.AppViewModelProvider
import com.yusy.keykeeper.ui.components.iconpainter.iconPainter
import com.yusy.keykeeper.ui.navigation.MyNavActions
import com.yusy.keykeeper.ui.navigation.MyRoutes
import com.yusy.keykeeper.ui.navigation.MySecondLevelDestination
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme
import kotlinx.coroutines.launch

@Composable
fun AccountEntryScreen(
    myNavActions: MyNavActions,
    modifier: Modifier = Modifier,
    viewModel: AccountEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AccountEntryBody(
        accountEntryUiState = viewModel.accountEntryUiState,
        onAccountValueChange = viewModel::updateAccountEntryUiState,
        onSave = {
            coroutineScope.launch {
                viewModel.saveAccount(context)
                myNavActions.navigateBack()
                // TODO:弹窗文本本地化
                Toast.makeText(context, "创建成功，密码已为您复制到剪切板", Toast.LENGTH_LONG).show()
            }
        },
        onAppChoose = {
            myNavActions.navigateToChild(
                MySecondLevelDestination(
                    route = MyRoutes.APP_CHOOSE_PAGE
                )
            )
        },
        modifier = modifier
    )
}

@Composable
fun AccountEntryBody(
    accountEntryUiState: AccountEntryUiState,
    onAccountValueChange: (AccountDetails) -> Unit,
    onAppChoose: () -> Unit,
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
                    appType = accountEntryUiState.accountDetails.appType,
                    appIcon = accountEntryUiState.accountDetails.appIcon
                ),
                contentDescription = "app icon",
            )
        }

        // input form
        EntryInputForm(
            accountDetails = accountEntryUiState.accountDetails,
            onValueChange = onAccountValueChange,
            onAppChoose = onAppChoose,
            modifier = modifier
        )

        // save button
        Button(
            shape = MaterialTheme.shapes.medium,
            enabled = accountEntryUiState.isValid,
            onClick = {
                if (accountEntryUiState.isValid) {
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

/**
 * Entry的输入表
 */
@Composable
fun EntryInputForm(
    accountDetails: AccountDetails,
    onValueChange: (AccountDetails) -> Unit,
    onAppChoose: () -> Unit,
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
        OutlinedTextField(
            modifier = inputModifier,
            value = accountDetails.uid,
            onValueChange = { onValueChange(accountDetails.copy(uid = it)) },
            label = { Row {
                Text(stringResource(R.string.account_page_account))
            } },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") },
            singleLine = true,
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

        // choose app type
        Row(modifier.selectableGroup()) {
            val choiceModifier = modifier.align(Alignment.CenterVertically)

            // 类型选择提示词
            Text(
                modifier = choiceModifier.padding(horizontal = 20.dp),
                text = stringResource(R.string.account_page_choosetype)
            )

            // radio group
            with(accountDetails) {
                // website
                Row(modifier.padding(10.dp)) {
                    RadioButton(
                        modifier = choiceModifier,
                        selected = (appType == AppType.Website),
                        onClick = { onValueChange(accountDetails.copy(appType = AppType.Website)) },
                    )
                    Text(
                        modifier = choiceModifier.clickable {
                            onValueChange(accountDetails.copy(appType = AppType.Website))
                        },
                        text = stringResource(R.string.account_page_website)
                    )
                }

                // app
                Row(modifier.padding(10.dp)) {
                    RadioButton(
                        modifier = choiceModifier,
                        selected = (appType == AppType.AndroidAPP),
                        onClick = { onValueChange(accountDetails.copy(appType = AppType.AndroidAPP)) },
                    )
                    Text(
                        modifier = choiceModifier.clickable {
                            onValueChange(accountDetails.copy(appType = AppType.AndroidAPP))
                        },
                        text = stringResource(R.string.account_page_app)
                    )
                }
            }
        }

        // appUrl & appName
        with (accountDetails) {
            if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
                // appUrl - app
                // TODO:增加APP选择页面，点击输出框后切出，同时输出框上滑至页面顶端，可用于输入关键词搜索APP
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appUrl,
                    onValueChange = { onValueChange(accountDetails.copy(appUrl = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_appurl))
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_appurl)) },
                    trailingIcon = {
                        IconButton(
                            onClick = { onAppChoose() }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_screen_search_desktop), contentDescription = stringResource(R.string.account_page_chooseapp))
                        }
                    },
                    singleLine = true,
                )

                // 提示 - 点击图标在本地app中选择
                // TODO:文本本地化
                Row(modifier = hintTextModifier) {
                    Text(
                        text = "点击",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_screen_search_desktop),
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = stringResource(R.string.account_page_chooseapp)
                    )
                    Text(
                        text = "在本地app中选择",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

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
                // TODO:增加网站icon预览
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appUrl,
                    onValueChange = { onValueChange(accountDetails.copy(appUrl = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_websiteurl))
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_websiteurl)) },
                    singleLine = true,
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
fun AccountEntryScreenPreview_Android() {
    KeyKeeperTheme {
        AccountEntryBody(
            accountEntryUiState = accountExampleAndroid.toAccountDetails().toAccountEntryUiState(true),
            onAccountValueChange = {},
            onAppChoose = {},
            onSave = {}
        )
    }
}

@Preview
@Composable
fun AccountEntryScreenPreview_Website() {
    KeyKeeperTheme {
        AccountEntryBody(
            accountEntryUiState = accountExampleWebsite.toAccountDetails().toAccountEntryUiState(true),
            onAccountValueChange = {},
            onAppChoose = {},
            onSave = {}
        )
    }
}
