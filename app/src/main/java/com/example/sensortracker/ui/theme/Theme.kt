package com.example.sensortracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DeepNavyBlue,
    onPrimary = ElevatedCardSurface,
    primaryContainer = DeepNavyBlue,
    onPrimaryContainer = ElevatedCardSurface,
    secondary = ElectricAzureBlue,
    onSecondary = ElevatedCardSurface,
    tertiary = VibrantCyanTeal,
    onTertiary = ElevatedCardSurface,
    background = SoftIceBlue,
    onBackground = PrimaryText,
    surface = ElevatedCardSurface,
    onSurface = PrimaryText,
    surfaceVariant = DropdownMenuSurface,
    onSurfaceVariant = SecondaryText,
    outline = CardBorderStroke,
    outlineVariant = CardBorderStroke
)

private val DarkColorScheme = darkColorScheme(
    primary = DeepNavyBlue,
    onPrimary = ElevatedCardSurface,
    primaryContainer = DeepNavyBlue,
    onPrimaryContainer = ElevatedCardSurface,
    secondary = ElectricAzureBlue,
    onSecondary = ElevatedCardSurface,
    tertiary = VibrantCyanTeal,
    onTertiary = ElevatedCardSurface,
    background = SoftIceBlue,
    onBackground = PrimaryText,
    surface = ElevatedCardSurface,
    onSurface = PrimaryText,
    surfaceVariant = DropdownMenuSurface,
    onSurfaceVariant = SecondaryText,
    outline = CardBorderStroke,
    outlineVariant = CardBorderStroke
)

@Composable
fun SensorTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
