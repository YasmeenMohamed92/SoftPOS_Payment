package com.example.softpos_payment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SoftPOSColorScheme = lightColorScheme(

    primary = Blue,
    onPrimary = White,

    primaryContainer = BlueLight,
    onPrimaryContainer = White,

    secondary = BlueDark,
    onSecondary = White,

    background = White,
    onBackground = Black,

    surface = White,
    onSurface = Black,

    error = ErrorRed,
    onError = White
)

@Composable
fun SoftPOS_PaymentTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SoftPOSColorScheme,
        typography = Typography,
        content = content
    )
}