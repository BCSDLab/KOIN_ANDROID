package `in`.koreatech.koin.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalKoinColorPalette = staticCompositionLocalOf {
    KoinColorPalette()
}

val KoinLightColorScheme = lightColors()

// 다크 테마 대응시 수정
val KoinDarkColorScheme = lightColors()

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

    CompositionLocalProvider(LocalKoinColorPalette provides extendedColors) {
        MaterialTheme(
            colors = colorScheme,
            content = content
        )
    }
}

object KoinTheme {
    val colors: KoinColorPalette
        @Composable
        get() = LocalKoinColorPalette.current
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isSupportDynamicTheme(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S