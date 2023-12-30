package com.yusy.keykeeper.ui.pages.authenticator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yusy.keykeeper.ui.pages.empty.EmptyScreen

@Composable
fun AuthenticatorScreen(modifier: Modifier = Modifier) {
    AuthenticatorBody(modifier = modifier)
}

@Composable
fun AuthenticatorBody(modifier: Modifier) {
    EmptyScreen(modifier = modifier)
}