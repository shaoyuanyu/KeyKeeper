package com.yusy.keykeeper.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.yusy.keykeeper.model.AccountData
import com.yusy.keykeeper.model.AppType

/**
 * AccountCreatePageUI
 * 账号创建页面
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountCreatePageUI() {
    val modifier = Modifier

    var uid by rememberSaveable { mutableStateOf("") }
    var plainPasswd by rememberSaveable { mutableStateOf("") }
    val appTypeState = mutableStateOf(AppType.Unknown)
    val appType by rememberSaveable { appTypeState }
    var appName by rememberSaveable { mutableStateOf("") }
    var appUrl by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {
        val inputModifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .align(Alignment.CenterHorizontally)

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
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "app icon",
            )
        }

        MyInputer(modifier = inputModifier, value = uid, onValueChange = { uid = it }, labelText = stringResource(R.string.account_page_account), isNecessary = false)

        MyInputer(modifier = inputModifier, value = plainPasswd, onValueChange = { plainPasswd = it }, labelText = stringResource(R.string.account_page_passwd), isNecessary = true)

        AppTypeChoose(Modifier, appTypeState)

        if (appType == AppType.AndroidAPP || appType == AppType.HmAPP) {
            MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(R.string.account_page_appurl), isNecessary = true)
            MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(R.string.account_page_appname), isNecessary = true)
        } else if (appType == AppType.Website) {
            MyInputer(modifier = inputModifier, value = appUrl, onValueChange = {  appUrl = it }, labelText = stringResource(R.string.account_page_websiteurl), isNecessary = true)
            MyInputer(modifier = inputModifier, value = appName, onValueChange = { appName = it }, labelText = stringResource(R.string.account_page_websitename), isNecessary = true)
        }
    }
}

/**
 * AccountEditPageUI
 * 账号编辑页面
 */
@Composable
fun AccountEditPageUI(
    id: String
) {
    Text(text = id)
    EmptyUI()
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
    isNecessary: Boolean = false
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
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
        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") },
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


@Preview
@Composable
fun PreviewAccountCreatePageUI() {
    AccountCreatePageUI()
}

@Preview
@Composable
fun PreviewAccountEditPageUI() {
    AccountEditPageUI("test000")
}
