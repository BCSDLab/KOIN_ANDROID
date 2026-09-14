package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = RebrandKoinTheme.colors.neutral0,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentDialog(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.medium15,
                color = RebrandKoinTheme.colors.neutral700
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral500
            )
            Spacer(modifier = Modifier.height(20.dp))
            val colors = RebrandKoinTheme.colors
            val cancelButtonColors = remember(colors) {
                ButtonColors(
                    containerColor = colors.neutral0,
                    contentColor = colors.neutral600,
                    disabledContainerColor = colors.neutral300,
                    disabledContentColor = colors.neutral600
                )
            }
            val confirmButtonColors = remember(colors) {
                ButtonColors(
                    containerColor = colors.primary500,
                    contentColor = colors.neutral0,
                    disabledContainerColor = colors.neutral300,
                    disabledContentColor = colors.neutral600
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedBoxButton(
                    text = cancelText,
                    onClick = onDismiss,
                    textStyle = RebrandKoinTheme.typography.medium16,
                    shape = RoundedCornerShape(16.dp),
                    colors = cancelButtonColors,
                    border = BorderStroke(1.dp, colors.neutral500),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledButton(
                    text = confirmText,
                    onClick = onConfirm,
                    textStyle = RebrandKoinTheme.typography.medium16,
                    shape = RoundedCornerShape(16.dp),
                    colors = confirmButtonColors,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RecruitmentConfirmDialogPreview() {
    RebrandKoinTheme {
        RecruitmentConfirmDialog(
            title = "해당 모집글을 마감하시겠어요?",
            message = "마감 후에는 더 이상 지원자를 받을 수 없습니다.",
            confirmText = "마감하기",
            cancelText = "취소",
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RecruitmentDialogPreview() {
    RebrandKoinTheme {
        RecruitmentDialog(onDismiss = {}) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Dialog Title",
                    style = RebrandKoinTheme.typography.medium15,
                    color = RebrandKoinTheme.colors.neutral700
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dialog message goes here.",
                    style = RebrandKoinTheme.typography.regular13,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }
    }
}
