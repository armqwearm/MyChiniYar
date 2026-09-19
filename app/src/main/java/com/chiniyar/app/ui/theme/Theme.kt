package com.chiniyar.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Porcelain = Color(0xFFFFFBF2)
private val PorcelainSurface = Color(0xFFFFF7E8)
private val ChineseRed = Color(0xFFC62828)
private val ChineseRedDark = Color(0xFF8E1B1B)
private val ImperialGold = Color(0xFFD6A72C)
private val Jade = Color(0xFF247C78)
private val Ink = Color(0xFF173B4B)
private val MutedInk = Color(0xFF5D6E73)
private val DarkBackground = Color(0xFF171313)
private val DarkSurface = Color(0xFF25201F)
private val DarkCard = Color(0xFF302A28)
private val DarkGold = Color(0xFFE2BC54)
private val DarkJade = Color(0xFF6DB4AD)

private val LightColors = lightColorScheme(
    primary = ChineseRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD5),
    onPrimaryContainer = Color(0xFF410005),
    secondary = Jade,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB7ECE5),
    onSecondaryContainer = Color(0xFF00201D),
    tertiary = ImperialGold,
    onTertiary = Color(0xFF392B00),
    tertiaryContainer = Color(0xFFFFE7A6),
    onTertiaryContainer = Color(0xFF251A00),
    background = Porcelain,
    onBackground = Ink,
    surface = Porcelain,
    onSurface = Ink,
    surfaceVariant = PorcelainSurface,
    onSurfaceVariant = MutedInk,
    outline = Color(0xFF9A8F80)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF8A80),
    onPrimary = Color(0xFF5F0006),
    primaryContainer = ChineseRedDark,
    onPrimaryContainer = Color(0xFFFFDAD5),
    secondary = DarkJade,
    onSecondary = Color(0xFF003733),
    secondaryContainer = Color(0xFF14534F),
    onSecondaryContainer = Color(0xFFB7ECE5),
    tertiary = DarkGold,
    onTertiary = Color(0xFF392B00),
    tertiaryContainer = Color(0xFF594700),
    onTertiaryContainer = Color(0xFFFFE7A6),
    background = DarkBackground,
    onBackground = Color(0xFFF4EDEA),
    surface = DarkSurface,
    onSurface = Color(0xFFF4EDEA),
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFFD5C8C3),
    outline = Color(0xFFA99B96)
)

private val MyChiniYarTypography = Typography()

@Composable
fun MyChiniYarTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme && isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = MyChiniYarTypography,
        content = content
    )
}
