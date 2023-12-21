package com.yusy.keykeeper.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.aligned
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

/**
 * AccountCreatePageUI
 * 账号创建页面
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountCreatePageUI() {
    val modifier = Modifier

    //
    var uid by rememberSaveable { mutableStateOf("") }
    var plainPasswd by rememberSaveable { mutableStateOf("") }
    val appTypeState = mutableStateOf(AppType.Unknown)
    val appType by rememberSaveable { appTypeState }
    var appName by rememberSaveable { mutableStateOf("") }
    var appUrl by rememberSaveable { mutableStateOf("") }
    val appIcon by rememberSaveable { mutableIntStateOf(R.drawable.ic_launcher_foreground) }
    //
    val inputCheckAlertState = mutableStateOf(false)
    var inputCheckAlert by rememberSaveable { inputCheckAlertState }
    var alertText by rememberSaveable { mutableIntStateOf(0) }

    fun checkInput() {
        if (plainPasswd.isEmpty()) {
            inputCheckAlert = true
            alertText = R.string.account_page_warning_passwdempty
            return
        }
        if (appType == AppType.Unknown) {
            inputCheckAlert = true
            alertText = R.string.account_page_warning_typeempty
            return
        }
        if (appUrl.isEmpty()) {
            inputCheckAlert = true
            if (appType == AppType.Website) {
                alertText = R.string.account_page_warning_urlempty_web
            } else if (appType == AppType.AndroidAPP) {
                alertText =R.string.account_page_warning_urlempty_app
            }
            return
        }
        if (appName.isEmpty()) {
            inputCheckAlert = true
            if (appType == AppType.Website) {
                alertText = R.string.account_page_warning_nameempty_web
            } else if (appType == AppType.AndroidAPP) {
                alertText = R.string.account_page_warning_nameempty_app
            }
            return
        }
    }

    // 弹窗警告
    InputCheckAlert(
        openDialogState = inputCheckAlertState,
        alertText = alertText
    )

    Column(
        modifier = modifier,
    ) {
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
            Image(
                modifier = modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                painter = painterResource(id = appIcon),
                contentDescription = "app icon",
            )
        }
        
        // account id
        MyInputer(modifier = inputModifier, value = uid, onValueChange = { uid = it }, labelText = stringResource(R.string.account_page_account), isNecessary = false)
        // passwd
        MyInputer(modifier = inputModifier, value = plainPasswd, onValueChange = { plainPasswd = it }, labelText = stringResource(R.string.account_page_passwd))
        
        AppTypeChoose(Modifier, appTypeState)
        
        if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
            AppChooser(modifier = inputModifier)
            MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(R.string.account_page_appurl), isReadonly = true)
            MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(R.string.account_page_appname))
        } else if (appType == AppType.Website) {
            MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(R.string.account_page_websiteurl))
            MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(R.string.account_page_websitename))
        }

        SaveButton(modifier = inputModifier, onClick = { checkInput() })
    }
}

/**
 * AccountEditPageUI
 * 账号编辑页面
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountEditPageUI(
    id: String
) {
    val modifier = Modifier

    var uid by rememberSaveable { mutableStateOf("account") }
    var plainPasswd by remember { mutableStateOf("123456") }
    val appType by rememberSaveable { mutableStateOf(AppType.AndroidAPP) }
    var appName by rememberSaveable { mutableStateOf("test app") }
    var appUrl by rememberSaveable { mutableStateOf("com.yusy.test") }
    val appIcon by rememberSaveable { mutableIntStateOf(R.drawable.ic_launcher_foreground) }
    //
    val inputCheckAlertState = mutableStateOf(false)
    var inputCheckAlert by rememberSaveable { inputCheckAlertState }
    var alertText by rememberSaveable { mutableIntStateOf(0) }

    fun checkInput() {
        if (plainPasswd.isEmpty()) {
            inputCheckAlert = true
            alertText = R.string.account_page_warning_passwdempty
        }
        if (appName.isEmpty()) {
            inputCheckAlert = true
            if (appType == AppType.Website) {
                alertText = R.string.account_page_warning_nameempty_web
            } else if (appType == AppType.AndroidAPP) {
                alertText = R.string.account_page_warning_nameempty_app
            }
            return
        }
    }

    // 弹窗警告
    InputCheckAlert(
        openDialogState = inputCheckAlertState,
        alertText = alertText
    )


    Column(
        modifier = modifier,
    ) {
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
            Image(
                modifier = modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                painter = painterResource(id = appIcon),
                contentDescription = "app icon",
            )
        }

        // account id
        MyInputer(modifier = inputModifier, value = uid, onValueChange = { uid = it }, labelText = stringResource(R.string.account_page_account), isNecessary = false, isReadonly = true)
        // passwd
        MyInputer(modifier = inputModifier, value = plainPasswd, onValueChange = { plainPasswd = it }, labelText = stringResource(R.string.account_page_passwd))

        if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
            MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(R.string.account_page_appurl), isReadonly = true)
            MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(R.string.account_page_appname))
        } else if (appType == AppType.Website) {
            MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(R.string.account_page_websiteurl), isReadonly = true)
            MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(R.string.account_page_websitename))
        }

        SaveButton(modifier = inputModifier, onClick = { checkInput() })
    }
}

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

/**
 * AppTypeChoose
 */
@Composable
fun AppTypeChoose(
    modifier: Modifier,
    appTypeState: MutableState<AppType>,
) {
    var appType by remember { appTypeState }

    Row(modifier.selectableGroup()) {
        val choiceModifier = modifier.align(Alignment.CenterVertically)

        // 类型选择提示词
        Text(
            modifier = choiceModifier.padding(horizontal = 20.dp),
            text = stringResource(R.string.account_page_choosetype)
        )

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

/**
 * AppChooser
 */
@Composable
fun AppChooser(
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = aligned(Alignment.CenterHorizontally)
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
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = aligned(Alignment.CenterHorizontally)
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


@Preview
@Composable
fun PreviewAccountCreatePageUI() {
    KeyKeeperTheme {
        AccountCreatePageUI()
    }
}

@Preview
@Composable
fun PreviewAccountEditPageUI() {
    KeyKeeperTheme {
        AccountEditPageUI("test000")
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewInputCheckAlert() {
    val inputCheckAlertState = mutableStateOf(false)
    KeyKeeperTheme {
        InputCheckAlert(
            openDialogState = inputCheckAlertState,
            alertText = R.string.account_page_warning_passwdempty
        )
    }
}
