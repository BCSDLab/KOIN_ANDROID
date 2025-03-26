package `in`.koreatech.koin.core.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

/**
 * TextStyle에 대한 한글 텍스트 높이를 Dp로 반환
 */
@Composable
fun TextStyle.getMeasuredKoreanHeightDp(): Dp {
    return with(rememberTextMeasurer()) {
        with(LocalDensity.current) {
            measure(
                "가",
                this@getMeasuredKoreanHeightDp
            ).size.height.toDp()
        }
    }
}
