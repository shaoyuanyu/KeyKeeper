package com.yusy.keykeeper.ui.components.biometricauth

import android.annotation.SuppressLint
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@SuppressLint("RememberReturnType")
@Composable
fun BiometricAuth(
    onAuthenticationSucceeded: () -> Unit,
    onAuthenticationFailed: () -> Unit
) {
    val context = LocalContext.current

    // TODO:弹窗文本本地化
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("cancel")
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .build()

    val biometricPrompt = remember {
        BiometricPrompt(
            context as FragmentActivity,
            ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onAuthenticationSucceeded()
                }

                override fun onAuthenticationFailed() {
                    onAuthenticationFailed()
                }
            })
    }

    DisposableEffect(Unit) {
        biometricPrompt.authenticate(promptInfo)
        onDispose {
            // Cancel authentication
            biometricPrompt.cancelAuthentication()
        }
    }
}

@Preview
@Composable
fun BiometricAuthPreview() {
    KeyKeeperTheme {
        BiometricAuth(
            onAuthenticationSucceeded = { },
            onAuthenticationFailed = { }
        )
    }
}