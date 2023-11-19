package com.yusy.keykeeper.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * AccountCreatePageUI
 * 账号创建页面
 */
@Composable
fun AccountCreatePageUI() {
    val modifier = Modifier

    Box(
        modifier = modifier
    ) {
        MyInputRow(
            modifier = modifier,
            rowLabel = "账号: ",
            inputLabel = {
                Row {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "input"
                    )
                    Text(text = "请输入账号")
                }
            }
        )
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
    EmptyUI()
}

/**
 *
 */
@Composable
fun MyInputRow(
    modifier: Modifier,
    rowLabel: String,
    inputLabel: @Composable (() -> Unit)
) {
    var text by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier
            .height(50.dp)
    ) {
        Text(
            modifier = modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp),
            text = rowLabel
        )

        TextField(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            value = text,
            onValueChange = {
                text = it
            },
            label = inputLabel,
            singleLine = true
        )
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
