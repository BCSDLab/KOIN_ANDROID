package `in`.koreatech.koin.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme


enum class OutlinedBoxButtonColors {
    Primary,
    Neutral
}

/**
 * 테두리 선이 있는 텍스트 버튼
 * @param text 텍스트
 * @param onClick 버튼 클릭시 실행할 함수
 * @param textStyle 텍스트 스타일
 * @param shape 버튼 모양
 * @param enabled 버튼 활성화 여부
 * @param colors 버튼 색상 타입
 * @param contentPadding 버튼 내부 padding
 * @see OutlinedBoxButtonColors
 */
@Composable
fun OutlinedBoxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium15,
    shape: Shape = KoinTheme.shapes.extraSmall,
    enabled: Boolean = true,
    colors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Primary,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    val buttonColors = outlinedBoxButtonColorByType(colors)
    val buttonBorder = outlinedBoxButtonBorderByType(colors)

    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = buttonColors,
        border = buttonBorder,
        contentPadding = contentPadding,
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}


/**
 * 테두리 선이 있는 텍스트 버튼
 * @param text 텍스트
 * @param onClick 버튼 클릭시 실행할 함수
 * @param textStyle 텍스트 스타일
 * @param shape 버튼 모양
 * @param enabled 버튼 활성화 여부
 * @param colors 버튼 색상
 * @param border 테두리 선
 * @param contentPadding 버튼 내부 padding
 * @see OutlinedBoxButtonColors
 */
@Composable
fun OutlinedBoxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium15,
    shape: Shape = KoinTheme.shapes.extraSmall,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonColors(
        containerColor = KoinTheme.colors.neutral0,
        contentColor = KoinTheme.colors.neutral0,
        disabledContainerColor = KoinTheme.colors.neutral400,
        disabledContentColor = KoinTheme.colors.neutral500
    ),
    border: BorderStroke,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

@Composable
@Stable
internal fun outlinedBoxButtonColorByType(type: OutlinedBoxButtonColors): ButtonColors = when (type) {
    OutlinedBoxButtonColors.Primary -> ButtonColors(
        containerColor = KoinTheme.colors.neutral0,
        contentColor = KoinTheme.colors.primary500,
        disabledContainerColor = KoinTheme.colors.neutral400,
        disabledContentColor = KoinTheme.colors.neutral500
    )

    OutlinedBoxButtonColors.Neutral -> ButtonColors(
        containerColor = KoinTheme.colors.neutral0,
        contentColor = KoinTheme.colors.neutral500,
        disabledContainerColor = KoinTheme.colors.neutral400,
        disabledContentColor = KoinTheme.colors.neutral500
    )
}


@Composable
@Stable
internal fun outlinedBoxButtonBorderByType(type: OutlinedBoxButtonColors): BorderStroke = when (type) {
    OutlinedBoxButtonColors.Primary -> BorderStroke(
        1.0.dp,
        color = KoinTheme.colors.primary500
    )

    OutlinedBoxButtonColors.Neutral -> BorderStroke(
        1.0.dp,
        color = KoinTheme.colors.neutral500
    )
}


@Preview
@Composable
private fun OutlinedButtonPrimaryPreview() {
    KoinTheme {
        OutlinedBoxButton(
            text = "대체하기",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun OutlinedButtonNeutralPreview() {
    KoinTheme {
        OutlinedBoxButton(
            text = "대체하기",
            colors = OutlinedBoxButtonColors.Neutral,
            onClick = {}
        )
    }
}