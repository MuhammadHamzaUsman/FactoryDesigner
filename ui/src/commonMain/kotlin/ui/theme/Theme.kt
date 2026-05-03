package com.example.compose
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ui.theme.AppTypography
import ui.theme.backgroundDark
import ui.theme.backgroundDarkHighContrast
import ui.theme.backgroundDarkMediumContrast
import ui.theme.backgroundLight
import ui.theme.backgroundLightHighContrast
import ui.theme.backgroundLightMediumContrast
import ui.theme.errorContainerDark
import ui.theme.errorContainerDarkHighContrast
import ui.theme.errorContainerDarkMediumContrast
import ui.theme.errorContainerLight
import ui.theme.errorContainerLightHighContrast
import ui.theme.errorContainerLightMediumContrast
import ui.theme.errorDark
import ui.theme.errorDarkHighContrast
import ui.theme.errorDarkMediumContrast
import ui.theme.errorLight
import ui.theme.errorLightHighContrast
import ui.theme.errorLightMediumContrast
import ui.theme.inverseOnSurfaceDark
import ui.theme.inverseOnSurfaceDarkHighContrast
import ui.theme.inverseOnSurfaceDarkMediumContrast
import ui.theme.inverseOnSurfaceLight
import ui.theme.inverseOnSurfaceLightHighContrast
import ui.theme.inverseOnSurfaceLightMediumContrast
import ui.theme.inversePrimaryDark
import ui.theme.inversePrimaryDarkHighContrast
import ui.theme.inversePrimaryDarkMediumContrast
import ui.theme.inversePrimaryLight
import ui.theme.inversePrimaryLightHighContrast
import ui.theme.inversePrimaryLightMediumContrast
import ui.theme.inverseSurfaceDark
import ui.theme.inverseSurfaceDarkHighContrast
import ui.theme.inverseSurfaceDarkMediumContrast
import ui.theme.inverseSurfaceLight
import ui.theme.inverseSurfaceLightHighContrast
import ui.theme.inverseSurfaceLightMediumContrast
import ui.theme.onBackgroundDark
import ui.theme.onBackgroundDarkHighContrast
import ui.theme.onBackgroundDarkMediumContrast
import ui.theme.onBackgroundLight
import ui.theme.onBackgroundLightHighContrast
import ui.theme.onBackgroundLightMediumContrast
import ui.theme.onErrorContainerDark
import ui.theme.onErrorContainerDarkHighContrast
import ui.theme.onErrorContainerDarkMediumContrast
import ui.theme.onErrorContainerLight
import ui.theme.onErrorContainerLightHighContrast
import ui.theme.onErrorContainerLightMediumContrast
import ui.theme.onErrorDark
import ui.theme.onErrorDarkHighContrast
import ui.theme.onErrorDarkMediumContrast
import ui.theme.onErrorLight
import ui.theme.onErrorLightHighContrast
import ui.theme.onErrorLightMediumContrast
import ui.theme.onPrimaryContainerDark
import ui.theme.onPrimaryContainerDarkHighContrast
import ui.theme.onPrimaryContainerDarkMediumContrast
import ui.theme.onPrimaryContainerLight
import ui.theme.onPrimaryContainerLightHighContrast
import ui.theme.onPrimaryContainerLightMediumContrast
import ui.theme.onPrimaryDark
import ui.theme.onPrimaryDarkHighContrast
import ui.theme.onPrimaryDarkMediumContrast
import ui.theme.onPrimaryLight
import ui.theme.onPrimaryLightHighContrast
import ui.theme.onPrimaryLightMediumContrast
import ui.theme.onSecondaryContainerDark
import ui.theme.onSecondaryContainerDarkHighContrast
import ui.theme.onSecondaryContainerDarkMediumContrast
import ui.theme.onSecondaryContainerLight
import ui.theme.onSecondaryContainerLightHighContrast
import ui.theme.onSecondaryContainerLightMediumContrast
import ui.theme.onSecondaryDark
import ui.theme.onSecondaryDarkHighContrast
import ui.theme.onSecondaryDarkMediumContrast
import ui.theme.onSecondaryLight
import ui.theme.onSecondaryLightHighContrast
import ui.theme.onSecondaryLightMediumContrast
import ui.theme.onSurfaceDark
import ui.theme.onSurfaceDarkHighContrast
import ui.theme.onSurfaceDarkMediumContrast
import ui.theme.onSurfaceLight
import ui.theme.onSurfaceLightHighContrast
import ui.theme.onSurfaceLightMediumContrast
import ui.theme.onSurfaceVariantDark
import ui.theme.onSurfaceVariantDarkHighContrast
import ui.theme.onSurfaceVariantDarkMediumContrast
import ui.theme.onSurfaceVariantLight
import ui.theme.onSurfaceVariantLightHighContrast
import ui.theme.onSurfaceVariantLightMediumContrast
import ui.theme.onTertiaryContainerDark
import ui.theme.onTertiaryContainerDarkHighContrast
import ui.theme.onTertiaryContainerDarkMediumContrast
import ui.theme.onTertiaryContainerLight
import ui.theme.onTertiaryContainerLightHighContrast
import ui.theme.onTertiaryContainerLightMediumContrast
import ui.theme.onTertiaryDark
import ui.theme.onTertiaryDarkHighContrast
import ui.theme.onTertiaryDarkMediumContrast
import ui.theme.onTertiaryLight
import ui.theme.onTertiaryLightHighContrast
import ui.theme.onTertiaryLightMediumContrast
import ui.theme.outlineDark
import ui.theme.outlineDarkHighContrast
import ui.theme.outlineDarkMediumContrast
import ui.theme.outlineLight
import ui.theme.outlineLightHighContrast
import ui.theme.outlineLightMediumContrast
import ui.theme.outlineVariantDark
import ui.theme.outlineVariantDarkHighContrast
import ui.theme.outlineVariantDarkMediumContrast
import ui.theme.outlineVariantLight
import ui.theme.outlineVariantLightHighContrast
import ui.theme.outlineVariantLightMediumContrast
import ui.theme.primaryContainerDark
import ui.theme.primaryContainerDarkHighContrast
import ui.theme.primaryContainerDarkMediumContrast
import ui.theme.primaryContainerLight
import ui.theme.primaryContainerLightHighContrast
import ui.theme.primaryContainerLightMediumContrast
import ui.theme.primaryDark
import ui.theme.primaryDarkHighContrast
import ui.theme.primaryDarkMediumContrast
import ui.theme.primaryLight
import ui.theme.primaryLightHighContrast
import ui.theme.primaryLightMediumContrast
import ui.theme.scrimDark
import ui.theme.scrimDarkHighContrast
import ui.theme.scrimDarkMediumContrast
import ui.theme.scrimLight
import ui.theme.scrimLightHighContrast
import ui.theme.scrimLightMediumContrast
import ui.theme.secondaryContainerDark
import ui.theme.secondaryContainerDarkHighContrast
import ui.theme.secondaryContainerDarkMediumContrast
import ui.theme.secondaryContainerLight
import ui.theme.secondaryContainerLightHighContrast
import ui.theme.secondaryContainerLightMediumContrast
import ui.theme.secondaryDark
import ui.theme.secondaryDarkHighContrast
import ui.theme.secondaryDarkMediumContrast
import ui.theme.secondaryLight
import ui.theme.secondaryLightHighContrast
import ui.theme.secondaryLightMediumContrast
import ui.theme.surfaceBrightDark
import ui.theme.surfaceBrightDarkHighContrast
import ui.theme.surfaceBrightDarkMediumContrast
import ui.theme.surfaceBrightLight
import ui.theme.surfaceBrightLightHighContrast
import ui.theme.surfaceBrightLightMediumContrast
import ui.theme.surfaceContainerDark
import ui.theme.surfaceContainerDarkHighContrast
import ui.theme.surfaceContainerDarkMediumContrast
import ui.theme.surfaceContainerHighDark
import ui.theme.surfaceContainerHighDarkHighContrast
import ui.theme.surfaceContainerHighDarkMediumContrast
import ui.theme.surfaceContainerHighLight
import ui.theme.surfaceContainerHighLightHighContrast
import ui.theme.surfaceContainerHighLightMediumContrast
import ui.theme.surfaceContainerHighestDark
import ui.theme.surfaceContainerHighestDarkHighContrast
import ui.theme.surfaceContainerHighestDarkMediumContrast
import ui.theme.surfaceContainerHighestLight
import ui.theme.surfaceContainerHighestLightHighContrast
import ui.theme.surfaceContainerHighestLightMediumContrast
import ui.theme.surfaceContainerLight
import ui.theme.surfaceContainerLightHighContrast
import ui.theme.surfaceContainerLightMediumContrast
import ui.theme.surfaceContainerLowDark
import ui.theme.surfaceContainerLowDarkHighContrast
import ui.theme.surfaceContainerLowDarkMediumContrast
import ui.theme.surfaceContainerLowLight
import ui.theme.surfaceContainerLowLightHighContrast
import ui.theme.surfaceContainerLowLightMediumContrast
import ui.theme.surfaceContainerLowestDark
import ui.theme.surfaceContainerLowestDarkHighContrast
import ui.theme.surfaceContainerLowestDarkMediumContrast
import ui.theme.surfaceContainerLowestLight
import ui.theme.surfaceContainerLowestLightHighContrast
import ui.theme.surfaceContainerLowestLightMediumContrast
import ui.theme.surfaceDark
import ui.theme.surfaceDarkHighContrast
import ui.theme.surfaceDarkMediumContrast
import ui.theme.surfaceDimDark
import ui.theme.surfaceDimDarkHighContrast
import ui.theme.surfaceDimDarkMediumContrast
import ui.theme.surfaceDimLight
import ui.theme.surfaceDimLightHighContrast
import ui.theme.surfaceDimLightMediumContrast
import ui.theme.surfaceLight
import ui.theme.surfaceLightHighContrast
import ui.theme.surfaceLightMediumContrast
import ui.theme.surfaceVariantDark
import ui.theme.surfaceVariantDarkHighContrast
import ui.theme.surfaceVariantDarkMediumContrast
import ui.theme.surfaceVariantLight
import ui.theme.surfaceVariantLightHighContrast
import ui.theme.surfaceVariantLightMediumContrast
import ui.theme.tertiaryContainerDark
import ui.theme.tertiaryContainerDarkHighContrast
import ui.theme.tertiaryContainerDarkMediumContrast
import ui.theme.tertiaryContainerLight
import ui.theme.tertiaryContainerLightHighContrast
import ui.theme.tertiaryContainerLightMediumContrast
import ui.theme.tertiaryDark
import ui.theme.tertiaryDarkHighContrast
import ui.theme.tertiaryDarkMediumContrast
import ui.theme.tertiaryLight
import ui.theme.tertiaryLightHighContrast
import ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

