package com.chiniyar.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

// MyChiniYar visual identity: Chinese red, warm gold, jade/teal and porcelain cream.
private val Porcelain = Color(0xFFFFFBF2)
private val PorcelainSurface = Color(0xFFFFF7E8)
private val WhiteSurface = Color(0xFFFFFEFA)
private val SoftOutline = Color(0xFF7A6F62)
private val ChineseRed = Color(0xFFC62828)
private val ChineseRedDark = Color(0xFF8E1B1B)
private val ImperialGold = Color(0xFFD6A72C)
private val Jade = Color(0xFF247C78)
private val Ink = Color(0xFF1E2A2D)
private val MutedInk = Color(0xFF4F595B)
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
    surface = WhiteSurface,
    onSurface = Ink,
    surfaceVariant = PorcelainSurface,
    onSurfaceVariant = MutedInk,
    outline = SoftOutline
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

private val BaseTypography = Typography()
private val MyChiniYarTypography = BaseTypography.copy(
    headlineMedium = BaseTypography.headlineMedium.copy(fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = BaseTypography.headlineSmall.copy(fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = BaseTypography.titleLarge.copy(fontSize = 21.sp, lineHeight = 29.sp),
    titleMedium = BaseTypography.titleMedium.copy(fontSize = 18.sp, lineHeight = 26.sp),
    titleSmall = BaseTypography.titleSmall.copy(fontSize = 16.sp, lineHeight = 23.sp),
    bodyLarge = BaseTypography.bodyLarge.copy(fontSize = 17.sp, lineHeight = 28.sp),
    bodyMedium = BaseTypography.bodyMedium.copy(fontSize = 16.sp, lineHeight = 25.sp),
    bodySmall = BaseTypography.bodySmall.copy(fontSize = 14.sp, lineHeight = 22.sp),
    labelLarge = BaseTypography.labelLarge.copy(fontSize = 15.sp, lineHeight = 21.sp),
    labelMedium = BaseTypography.labelMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
    labelSmall = BaseTypography.labelSmall.copy(fontSize = 13.sp, lineHeight = 18.sp)
)

@Composable
fun MyChiniYarTheme(
    // The product identity is intentionally light/porcelain by default; dark mode remains supported.
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = MyChiniYarTypography,
        content = content
    )
}
