package `in`.koreatech.koin.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

val LocalKoinColorPalette = staticCompositionLocalOf {
    KoinColorPalette()
}

val KoinLightColorScheme = lightColors()

// 다크 테마 대응시 수정
val KoinDarkColorScheme = lightColors()

val LocalKoinTypography = staticCompositionLocalOf {
    KoinTypography(
        regular10 = TextStyle.Default,
        regular12 = TextStyle.Default,
        regular13 = TextStyle.Default,
        regular14 = TextStyle.Default,
        regular15 = TextStyle.Default,
        regular16 = TextStyle.Default,
        regular18 = TextStyle.Default,
        medium12 = TextStyle.Default,
        medium13 = TextStyle.Default,
        medium14 = TextStyle.Default,
        medium15 = TextStyle.Default,
        medium16 = TextStyle.Default,
        medium18 = TextStyle.Default,
        bold12 = TextStyle.Default,
        bold13 = TextStyle.Default,
        bold14 = TextStyle.Default,
        bold15 = TextStyle.Default,
        bold16 = TextStyle.Default,
        bold18 = TextStyle.Default,
        bold20 = TextStyle.Default
    )
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
        defaultFontFamily = Pretendard,
        h1 = MediumStyle6,
        h2 = MediumStyle5,
        h3 = MediumStyle5,
        h4 = MediumStyle4,
        h5 = MediumStyle3,
        h6 = MediumStyle2,
        body1 = RegularStyle5,
        body2 = RegularStyle4,
        button = MediumStyle5,
        caption = RegularStyle1,
        overline = RegularStyle3
    )

    CompositionLocalProvider(
        LocalKoinColorPalette provides extendedColors,
        LocalKoinTypography provides koinTypography,
    ) {
        MaterialTheme(
            colors = colorScheme,
            typography = typography,
            content = content
        )
    }
}

object KoinTheme {
    val colors: KoinColorPalette
        @Composable
        get() = LocalKoinColorPalette.current
    val typography: KoinTypography
        @Composable
        get() = LocalKoinTypography.current
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isSupportDynamicTheme(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S