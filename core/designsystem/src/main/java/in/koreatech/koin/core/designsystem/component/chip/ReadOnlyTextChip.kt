package `in`.koreatech.koin.core.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * 선택 불가한 텍스트 칩
 * @param title 텍스트
 * @param modifier Modifier
 * @param containerColor 칩 배경색
 * @param textStyle 텍스트 스타일
 * @param shape 칩 모양
 * @param contentPadding 칩 내부 padding
 */
@Composable
fun ReadOnlyTextChip(
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = KoinTheme.colors.primary500,
    textStyle: TextStyle = KoinTheme.typography.regular12,
    shape: Shape = RoundedCornerShape(4.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 4.dp),
) {
    Box(
        modifier =
            modifier
                .clip(shape)
                .background(containerColor)
                .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = textStyle,
        )
    }
}

@Preview
@Composable
private fun ReadOnlyTextChipPreview() {
    ReadOnlyTextChip(
        title = "순환",
        containerColor = Color(0xFF4ED92C),
        textStyle = KoinTheme.typography.regular12.copy(color = Color.White),
    )
}
