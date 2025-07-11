package `in`.koreatech.koin.feature.user.ui.inforequire

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun InfoRequiredModalScreen(
    title: String,
    description: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    descriptionStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0f, 0f, 0f, 0.7f)) // 배경 70% 검정 투명
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onNegative()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clickable(enabled = false) {} // 배경 클릭 막기
        ) {
            ChoiceDialog(
                title = title,
                description = description,
                descriptionStyle = descriptionStyle,
                onPositive = onPositive,
                onNegative = onNegative,
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChoiceDialogPreview() {
    KoinTheme {
        InfoRequiredModalScreen(
            title = "아직 입력되지 않은 정보가 있어요",
            description = "필수 정보를 입력하시면 더 많은 기능을 이용하실 수 있어요.\n지금 입력하시겠어요?",
            descriptionStyle =
            KoinTheme.typography.regular12.copy(
                textAlign = TextAlign.Center,
                color = KoinTheme.colors.neutral500
            ),
            onPositive = {},
            onNegative = {}
        )
    }
}
