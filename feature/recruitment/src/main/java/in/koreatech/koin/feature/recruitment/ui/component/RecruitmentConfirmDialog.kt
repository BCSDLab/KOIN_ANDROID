package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val DialogShape = RoundedCornerShape(8.dp)
private val ButtonShape = RoundedCornerShape(8.dp)
private val DialogWidth = 301.dp
private val ButtonHeight = 44.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentConfirmDialog(
    title: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    BasicAlertDialog(
        modifier = modifier.width(DialogWidth).wrapContentHeight(),
        onDismissRequest = onNegative
    ) {
        Column(
            modifier = Modifier
                .background(RebrandKoinTheme.colors.neutral0, DialogShape)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.medium16,
                color = RebrandKoinTheme.colors.neutral800,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedBoxButton(
                    text = negativeButtonText,
                    onClick = onNegative,
                    modifier = Modifier
                        .weight(1f)
                        .height(ButtonHeight)
                        .clip(ButtonShape)
                )
                FilledButton(
                    text = positiveButtonText,
                    onClick = onPositive,
                    modifier = Modifier
                        .weight(1f)
                        .height(ButtonHeight)
                        .clip(ButtonShape)
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
            title = "해당 팀원 모집에 지원하시겠어요?",
            positiveButtonText = "지원하기",
            negativeButtonText = "취소하기",
            onPositive = {},
            onNegative = {}
        )
    }
}
