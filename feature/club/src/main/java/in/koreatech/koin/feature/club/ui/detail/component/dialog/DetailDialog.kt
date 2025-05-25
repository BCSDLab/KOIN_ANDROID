package `in`.koreatech.koin.feature.club.ui.detail.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.button.DetailButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    onDismiss: () -> Unit = {},
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    content: @Composable () -> Unit = {},
    titleStyle: TextStyle = KoinTheme.typography.bold20,
    positiveButtonText: String = stringResource(id = R.string.common_confirmation),
    positiveButtonColors: ButtonColors = ButtonColors(
        containerColor = KoinTheme.colors.primary500,
        contentColor = KoinTheme.colors.neutral0,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    ),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    negativeButtonColors: ButtonColors = ButtonColors(
        containerColor = KoinTheme.colors.neutral0,
        contentColor = KoinTheme.colors.neutral600,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    )
) {
    BasicAlertDialog(
        modifier =
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.large
            )
            .padding(horizontal = 12.dp, vertical = 24.dp),
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = titleStyle
            )
            content()
            Row {
                DetailButton(
                    modifier =
                    Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = KoinTheme.colors.neutral600,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    text = negativeButtonText,
                    textStyle = KoinTheme.typography.medium15,
                    contentPadding = PaddingValues(vertical = 6.dp),
                    onClick = onNegative,
                    colors = negativeButtonColors
                )
                Spacer(Modifier.width(8.dp))
                DetailButton(
                    modifier =
                    Modifier
                        .weight(1f),
                    text = positiveButtonText,
                    textStyle = KoinTheme.typography.medium15,
                    contentPadding = PaddingValues(vertical = 6.dp),
                    onClick = onPositive,
                    colors = positiveButtonColors
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailDialogPreview() {
    DetailDialog(
        title = "동아리 생성 안내사항",
        onPositive = {},
        onNegative = {}
    )
}
