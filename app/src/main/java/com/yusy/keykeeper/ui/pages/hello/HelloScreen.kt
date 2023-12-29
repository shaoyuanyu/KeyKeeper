package com.yusy.keykeeper.ui.pages.hello

import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yusy.keykeeper.MainActivity
import com.yusy.keykeeper.R
import com.yusy.keykeeper.ui.components.biometricauth.BiometricAuth
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@Composable
fun HelloScreen() {
    val context = LocalContext.current
    var authStatus by remember { mutableStateOf(false) }
    // TODO:弹窗文本本地化
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("安全验证")
        .setSubtitle("验证指纹以进入KeyKeeper")
        .setNegativeButtonText("取消")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    HelloBody(
        onFingerprintAuthClick = {
            authStatus = true
        }
    )

    if (authStatus) {
        BiometricAuth(
            promptInfo = promptInfo,
            onAuthenticationSucceeded = {
                authStatus = false
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            onAuthenticationFailed = {
                authStatus = false
            },
            onAuthenticationError = {
                authStatus = false
            }
        )
    }
}

@Composable
fun HelloBody(
    onFingerprintAuthClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(R.drawable.appic_key_keeper_round),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .padding(bottom = 20.dp),
                contentDescription = null
            )

            // TODO:文本本地化
            Text(
                text = "验证指纹以进入KeyKeeper",
                color = MaterialTheme.colorScheme.secondary
            )
        }


        IconButton(
            onClick = onFingerprintAuthClick,
            modifier = Modifier
                .clip(CircleShape)
                .padding(bottom = 150.dp)
                .align(Alignment.BottomCenter)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fingerprint),
                modifier = Modifier.size(150.dp),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun HelloBodyPreview() {
    KeyKeeperTheme {
        HelloBody(
            onFingerprintAuthClick = {}
        )
    }
}