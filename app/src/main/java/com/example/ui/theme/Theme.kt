package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = MutedRose,
  onPrimary = BurgundyDeep,
  primaryContainer = BurgundyPrimary,
  onPrimaryContainer = WarmIvory,
  secondary = WarmStone,
  onSecondary = NearBlack,
  secondaryContainer = CharcoalDark,
  onSecondaryContainer = WarmIvory,
  tertiary = WarmStoneLight,
  onTertiary = NearBlack,
  background = NearBlack,
  onBackground = WarmIvory,
  surface = CharcoalDark,
  onSurface = WarmIvory,
  surfaceVariant = Color(0xFF33302E),
  onSurfaceVariant = WarmStone,
  outline = Color(0xFF554F49),
  outlineVariant = Color(0xFF3D3833),
)

private val LightColorScheme = lightColorScheme(
  primary = BurgundyPrimary,
  onPrimary = WarmIvory,
  primaryContainer = BurgundyDark,
  onPrimaryContainer = WarmIvory,
  secondary = CharcoalDark,
  onSecondary = WarmIvory,
  secondaryContainer = WarmStoneLight,
  onSecondaryContainer = NearBlack,
  tertiary = MutedRose,
  onTertiary = NearBlack,
  background = WarmIvory,
  onBackground = NearBlack,
  surface = Color(0xFFFFFFFF),
  onSurface = NearBlack,
  surfaceVariant = WarmIvoryDarker,
  onSurfaceVariant = TextSecondary,
  outline = WarmStoneBorder,
  outlineVariant = Color(0xFFE8E2D8),
)

@Composable
fun CareereTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MyApplicationTheme(
    darkTheme = darkTheme,
    dynamicColor = dynamicColor,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For Careeré's refined editorial identity, default to consistent brand colors
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
