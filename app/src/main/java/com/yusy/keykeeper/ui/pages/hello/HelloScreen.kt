package com.yusy.keykeeper.ui.pages.hello

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.yusy.keykeeper.MainActivity
import com.yusy.keykeeper.R
import com.yusy.keykeeper.ui.components.biometricauth.BiometricAuth
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@Composable
fun HelloScreen() {
    val context = LocalContext.current

    HelloBody()

    BiometricAuth(
        onAuthenticationSucceeded = {
            context.startActivity(Intent(context, MainActivity::class.java))
        },
        onAuthenticationFailed = { }
    )
}

@Composable
fun HelloBody() {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            val modifier = Modifier.align(Alignment.CenterHorizontally)

            Image(
                painter = painterResource(R.drawable.appic_key_keeper_round),
                modifier = modifier.clip(CircleShape),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun HelloBodyPreview() {
    KeyKeeperTheme {
        HelloBody()
    }
}