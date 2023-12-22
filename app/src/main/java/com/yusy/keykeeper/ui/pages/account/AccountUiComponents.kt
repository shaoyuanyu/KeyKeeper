package com.yusy.keykeeper.ui.pages.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.AppType

/**
 * MyInputer
 */
@Composable
fun MyInputer(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    isNecessary: Boolean = true,
    isReadonly: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        readOnly = isReadonly,
        label = {
            Row {
                Text(labelText)
                if (isNecessary) {
                    Text(
                        color = MaterialTheme.colorScheme.error,
                        text = "*"
                    )
                }
            }
        },
        leadingIcon = { if (!isReadonly) Icon(Icons.Default.Edit, contentDescription = "") },
        singleLine = true,
    )
}

@Composable
fun entryInputForm(
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

        //
        with (accountDetails) {
            if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
//                MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(
//                    R.string.account_page_appurl), isReadonly = true)
                // appUrl - app
                OutlinedTextField(
                    modifier = inputModifier,
                    value = accountDetails.appUrl,
                    onValueChange = { onValueChange(accountDetails.copy(appUrl = it)) },
                    label = { Row {
                        Text(stringResource(R.string.account_page_appurl))
                        Text( color = MaterialTheme.colorScheme.error, text = "*")
                    } },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") },
                    singleLine = true,
                )

                // appName - app
                MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(
                    R.string.account_page_appname)
                )
            } else if (appType == AppType.Website) {
                MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(
                    R.string.account_page_websiteurl), isReadonly = true)
                MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(
                    R.string.account_page_websitename)
                )
            }
        }
    }
}

/**
 * AppTypeChoose
 */
//@Composable
//fun AppTypeChoose(
//    modifier: Modifier,
//    appTypeState: MutableState<AppType>,
//) {
//    var appType by remember { appTypeState }
//
//    Row(modifier.selectableGroup()) {
//        val choiceModifier = modifier.align(Alignment.CenterVertically)
//
//        // 类型选择提示词
//        Text(
//            modifier = choiceModifier.padding(horizontal = 20.dp),
//            text = stringResource(R.string.account_page_choosetype)
//        )
//
//        // website
//        Row(modifier.padding(10.dp)) {
//            RadioButton(
//                modifier = choiceModifier,
//                selected = (appType == AppType.Website),
//                onClick = {
//                    appType = AppType.Website
//                },
//            )
//            Text(
//                modifier = choiceModifier,
//                text = stringResource(R.string.account_page_website)
//            )
//        }
//
//        // app
//        Row(modifier.padding(10.dp)) {
//            RadioButton(
//                modifier = choiceModifier,
//                selected = (appType == AppType.AndroidAPP),
//                onClick = {
//                    appType = AppType.AndroidAPP
//                },
//            )
//            Text(
//                modifier = choiceModifier,
//                text = stringResource(R.string.account_page_app)
//            )
//        }
//    }
//}
@Composable
fun AppTypeChoose(
    modifier: Modifier,
    accountUiState: AccountUiState,
) {
    Row(modifier.selectableGroup()) {
        val choiceModifier = modifier.align(Alignment.CenterVertically)

        // 类型选择提示词
        Text(
            modifier = choiceModifier.padding(horizontal = 20.dp),
            text = stringResource(R.string.account_page_choosetype)
        )

        with(accountUiState.accountDetails) {
            // website
            Row(modifier.padding(10.dp)) {
                RadioButton(
                    modifier = choiceModifier,
                    selected = (appType == AppType.Website),
                    onClick = {
                        appType = AppType.Website
                    },
                )
                Text(
                    modifier = choiceModifier,
                    text = stringResource(R.string.account_page_website)
                )
            }

            // app
            Row(modifier.padding(10.dp)) {
                RadioButton(
                    modifier = choiceModifier,
                    selected = (appType == AppType.AndroidAPP),
                    onClick = {
                        appType = AppType.AndroidAPP
                    },
                )
                Text(
                    modifier = choiceModifier,
                    text = stringResource(R.string.account_page_app)
                )
            }
        }
    }
}

/**
 * AppChooser
 */
@Composable
fun AppChooser(
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.aligned(Alignment.CenterHorizontally)
    ) {
        Button(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = { /*TODO*/ }
        ) {
            Text(text = stringResource(R.string.account_page_chooseapp))
        }
    }
}

/**
 * SaveButton
 */
@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.aligned(Alignment.CenterHorizontally)
    ) {
        Button(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = {
                onClick()
            }
        ) {
            Text(text = stringResource(R.string.account_page_save))
        }
    }
}

/**
 * InputCheckAlert
 */
@Composable
fun InputCheckAlert(
    openDialogState: MutableState<Boolean>,
    alertText: Int
) {
    var openDialog by rememberSaveable { openDialogState }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text(text = "账号信息不完整") },
            icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "") },
            text = { Text(stringResource(alertText)) },
            confirmButton = {
                TextButton(
                    onClick = { openDialog = false }
                ) {
                    Text("确认")
                }
            }
        )
    }
}
