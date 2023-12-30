package com.yusy.keykeeper.ui.pages.authenticator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yusy.keykeeper.ui.pages.empty.EmptyUI

@Composable
fun AuthenticatorScreen(modifier: Modifier = Modifier) {
    AuthenticatorBody(modifier = modifier)
}

@Composable
fun AuthenticatorBody(modifier: Modifier) {
    EmptyUI(modifier = modifier)
}