package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldDarkPrimary,
    onPrimary = EmeraldDarkOnPrimary,
    primaryContainer = EmeraldDarkContainer,
    onPrimaryContainer = EmeraldDarkOnContainer,
    secondary = Color(0xFF81C7B0),
    onSecondary = Color(0xFF003828),
    secondaryContainer = DarkBotanicalSurfaceHigh,
    onSecondaryContainer = DarkBotanicalOnSurface,
    tertiary = Color(0xFFFFB4A9),
    background = DarkBotanicalBackground,
    onBackground = DarkBotanicalOnSurface,
    surface = DarkBotanicalSurface,
    onSurface = DarkBotanicalOnSurface,
    surfaceVariant = DarkBotanicalSurfaceHigh,
    onSurfaceVariant = DarkBotanicalMuted,
    outline = DarkBotanicalOutline,
    outlineVariant = DarkBotanicalOutline
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = SageSurface,
    onPrimaryContainer = EmeraldDeep,
    secondary = EmeraldDeep,
    onSecondary = Color.White,
    secondaryContainer = MintLightHighlight,
    onSecondaryContainer = BotanicalInk,
    tertiary = CoralHeart,
    onTertiary = Color.White,
    background = MintCanvas,
    onBackground = BotanicalInk,
    surface = Color.White,
    onSurface = BotanicalInk,
    surfaceVariant = SageSurface,
    onSurfaceVariant = MutedSage,
    outline = SoftMintBorder,
    outlineVariant = Color(0xFFE5EEE8)
)

val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun JahidulTranslateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
