package `in`.koreatech.koin.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalKoinColorPalette = staticCompositionLocalOf {
    KoinColorPalette(
        primary900 = Color.Unspecified,
        primary800 = Color.Unspecified,
        primary700 = Color.Unspecified,
        primary600 = Color.Unspecified,
        primary500 = Color.Unspecified,
        primary400 = Color.Unspecified,
        primary300 = Color.Unspecified,
        primary200 = Color.Unspecified,
        primary100 = Color.Unspecified,
        sub900 = Color.Unspecified,
        sub800 = Color.Unspecified,
        sub700 = Color.Unspecified,
        sub600 = Color.Unspecified,
        sub500 = Color.Unspecified,
        sub400 = Color.Unspecified,
        sub300 = Color.Unspecified,
        sub200 = Color.Unspecified,
        sub100 = Color.Unspecified,
        neutral800 = Color.Unspecified,
        neutral700 = Color.Unspecified,
        neutral600 = Color.Unspecified,
        neutral500 = Color.Unspecified,
        neutral400 = Color.Unspecified,
        neutral300 = Color.Unspecified,
        neutral200 = Color.Unspecified,
        neutral100 = Color.Unspecified,
        neutral50 = Color.Unspecified,
        neutral0 = Color.Unspecified,
        danger700 = Color.Unspecified,
        danger600 = Color.Unspecified,
        danger500 = Color.Unspecified,
        danger400 = Color.Unspecified,
        danger300 = Color.Unspecified,
        danger200 = Color.Unspecified,
        danger100 = Color.Unspecified,
        danger50 = Color.Unspecified,
        warning700 = Color.Unspecified,
        warning600 = Color.Unspecified,
        warning500 = Color.Unspecified,
        warning400 = Color.Unspecified,
        warning300 = Color.Unspecified,
        warning200 = Color.Unspecified,
        warning100 = Color.Unspecified,
        warning50 = Color.Unspecified,
        success700 = Color.Unspecified,
        success600 = Color.Unspecified,
        success500 = Color.Unspecified,
        success400 = Color.Unspecified,
        success300 = Color.Unspecified,
        success200 = Color.Unspecified,
        success100 = Color.Unspecified,
        success50 = Color.Unspecified,
        info700 = Color.Unspecified,
        info600 = Color.Unspecified,
        info500 = Color.Unspecified,
        info400 = Color.Unspecified,
        info300 = Color.Unspecified,
        info200 = Color.Unspecified,
        info100 = Color.Unspecified,
        info50 = Color.Unspecified
    )
}


internal val LocalKoinTypography = staticCompositionLocalOf {
    KoinTypography(
        regular10 = RegularStyle1,
        regular12 = RegularStyle2,
        regular13 = RegularStyle3,
        regular14 = RegularStyle4,
        regular15 = RegularStyle5,
        regular16 = RegularStyle6,
        regular18 = RegularStyle7,
        medium12 = MediumStyle1,
        medium13 = MediumStyle2,
        medium14 = MediumStyle3,
        medium15 = MediumStyle4,
        medium16 = MediumStyle5,
        medium18 = MediumStyle6,
        bold12 = BoldStyle1,
        bold13 = BoldStyle2,
        bold14 = BoldStyle3,
        bold15 = BoldStyle4,
        bold16 = BoldStyle5,
        bold18 = BoldStyle6,
        bold20 = BoldStyle7,
    )
}

internal val KoinLightColorScheme = lightColorScheme(
    primary = blue50
)

// 다크 테마 대응시 수정
internal val KoinDarkColorScheme = lightColorScheme(
    primary = blue50
)

internal val LocalShapes = staticCompositionLocalOf {
    Shapes
}

@Composable
fun KoinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val extendedColors =
        if (darkTheme)
            KoinDarkColorPalette
        else
            KoinLightColorPalette

    val colorScheme = when {
        dynamicTheme && isSupportDynamicTheme() -> {
            if (darkTheme) KoinDarkColorScheme else KoinLightColorScheme
        }

        darkTheme -> KoinDarkColorScheme
        else -> KoinLightColorScheme
    }

    val koinTypography = KoinTypography(
        regular10 = RegularStyle1,
        regular12 = RegularStyle2,
        regular13 = RegularStyle3,
        regular14 = RegularStyle4,
        regular15 = RegularStyle5,
        regular16 = RegularStyle6,
        regular18 = RegularStyle7,
        medium12 = MediumStyle1,
        medium13 = MediumStyle2,
        medium14 = MediumStyle3,
        medium15 = MediumStyle4,
        medium16 = MediumStyle5,
        medium18 = MediumStyle6,
        bold12 = BoldStyle1,
        bold13 = BoldStyle2,
        bold14 = BoldStyle3,
        bold15 = BoldStyle4,
        bold16 = BoldStyle5,
        bold18 = BoldStyle6,
        bold20 = BoldStyle7,
    )
    
    val typography = Typography(
        displayLarge = MediumStyle6,
        displayMedium = MediumStyle5,
        displaySmall = MediumStyle4,
        headlineLarge = MediumStyle6,
        headlineMedium = MediumStyle5,
        headlineSmall = MediumStyle4,
        titleLarge = MediumStyle5,
        titleMedium = MediumStyle4,
        titleSmall = MediumStyle3,
        bodyLarge = RegularStyle5,
        bodyMedium = RegularStyle4,
        bodySmall = RegularStyle3,
        labelLarge = RegularStyle3,
        labelMedium = RegularStyle2,
        labelSmall = RegularStyle1
    )

    val shapes = Shapes

    CompositionLocalProvider(
        LocalKoinColorPalette provides extendedColors,
        LocalKoinTypography provides koinTypography,
        LocalShapes provides shapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = Shapes,
            content = content
        )
    }
}

object KoinTheme {
    val colors: KoinColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalKoinColorPalette.current
    val typography: KoinTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKoinTypography.current
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun isSupportDynamicTheme(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S