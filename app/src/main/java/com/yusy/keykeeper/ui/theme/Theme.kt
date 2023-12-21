package com.yusy.keykeeper.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val myDarkColorScheme = darkColorScheme(
    primary = myDarkPrimary,
    onPrimary = myDarkOnPrimary,
    primaryContainer = myDarkPrimaryContainer,
    onPrimaryContainer = myDarkOnPrimaryContainer,
    inversePrimary = myDarkInversePrimary,
    secondary = myDarkSecondary,
    onSecondary = myDarkOnSecondary,
    secondaryContainer = myDarkSecondaryContainer,
    onSecondaryContainer = myDarkOnSecondaryContainer,
    tertiary = myDarkTertiary,
    onTertiary = myDarkOnTertiary,
    tertiaryContainer = myDarkTertiaryContainer,
    onTertiaryContainer = myDarkOnTertiaryContainer,
    error = myDarkError,
    onError = myDarkOnError,
    errorContainer = myDarkErrorContainer,
    onErrorContainer = myDarkOnErrorContainer,
    background = myDarkBackground,
    onBackground = myDarkOnBackground,
    surface = myDarkSurface,
    onSurface = myDarkOnSurface,
    inverseSurface = myDarkInverseSurface,
    inverseOnSurface = myDarkInverseOnSurface,
    surfaceVariant = myDarkSurfaceVariant,
    onSurfaceVariant = myDarkOnSurfaceVariant,
    outline = myDarkOutline,
    surfaceTint = myDarkSurfaceTint,
    outlineVariant = myDarkOutlineVariant,
    scrim = myDarkScrim,
)

private val myLightColorScheme = lightColorScheme(
    primary = myLightPrimary,
    onPrimary = myLightOnPrimary,
    primaryContainer = myLightPrimaryContainer,
    onPrimaryContainer = myLightOnPrimaryContainer,
    inversePrimary = myLightInversePrimary,
    secondary = myLightSecondary,
    onSecondary = myLightOnSecondary,
    secondaryContainer = myLightSecondaryContainer,
    onSecondaryContainer = myLightOnSecondaryContainer,
    tertiary = myLightTertiary,
    onTertiary = myLightOnTertiary,
    tertiaryContainer = myLightTertiaryContainer,
    onTertiaryContainer = myLightOnTertiaryContainer,
    error = myLightError,
    onError = myLightOnError,
    errorContainer = myLightErrorContainer,
    onErrorContainer = myLightOnErrorContainer,
    background = myLightBackground,
    onBackground = myLightOnBackground,
    surface = myLightSurface,
    onSurface = myLightOnSurface,
    inverseSurface = myLightInverseSurface,
    inverseOnSurface = myLightInverseOnSurface,
    surfaceVariant = myLightSurfaceVariant,
    onSurfaceVariant = myLightOnSurfaceVariant,
    outline = myLightOutline,
    surfaceTint = myLightSurfaceTint,
    outlineVariant = myLightOutlineVariant,
    scrim = myLightScrim,
)


@Composable
fun KeyKeeperTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = false,
        content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> myDarkColorScheme
        else -> myLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RhombusShapes,
        content = content
    )
}