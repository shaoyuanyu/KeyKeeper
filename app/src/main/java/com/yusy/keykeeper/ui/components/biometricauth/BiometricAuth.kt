package com.yusy.keykeeper.ui.components.biometricauth

import android.annotation.SuppressLint
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
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
    promptInfo: PromptInfo,
    onAuthenticationSucceeded: () -> Unit,
    onAuthenticationFailed: () -> Unit,
    onAuthenticationError: () -> Unit
) {
    val context = LocalContext.current

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

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onAuthenticationError()
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
            promptInfo = PromptInfo.Builder()
                .setTitle("安全验证")
                .setSubtitle("请验证指纹以进入app")
                .setNegativeButtonText("取消")
                .setAllowedAuthenticators(BIOMETRIC_STRONG)
                .build(),
            onAuthenticationSucceeded = { },
            onAuthenticationFailed = { },
            onAuthenticationError = { }
        )
    }
}