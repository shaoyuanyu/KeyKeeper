package com.yusy.keykeeper.ui.pages.account

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.AppType

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
            singleLine = true,
        )

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
                    Text(modifier = choiceModifier, text = stringResource(R.string.account_page_website))
                }

                // app
                Row(modifier.padding(10.dp)) {
                    RadioButton(
                        modifier = choiceModifier,
                        selected = (appType == AppType.AndroidAPP),
                        onClick = { onValueChange(accountDetails.copy(appType = AppType.AndroidAPP)) },
                    )
                    Text(modifier = choiceModifier, text = stringResource(R.string.account_page_app))
                }
            }
        }

        // appUrl & appName
        with (accountDetails) {
            if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
                // appUrl - app
                // TODO:增加APP选择页面，点击输出框后切出，同时输出框上滑至页面顶端，可用于输入关键词搜索APP
                OutlinedTextField(
                    modifier = inputModifier
                        .onFocusChanged {
                            if (it.isFocused) {
                                onAppChoose()
                            }
                        },
                    value = accountDetails.appUrl,
                    onValueChange = { onValueChange(accountDetails.copy(appUrl = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_appurl))
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.account_page_appurl)) },
                    singleLine = true,
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
            singleLine = true,
        )

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
