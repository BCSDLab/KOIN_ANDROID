package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledButtonType
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.component.HighlightedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteScheduleDialog(
    scheduleName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = KoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier =
                Modifier
                    .wrapContentSize()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val title =
                    stringArrayResource(id = R.array.delete_schedule_title).apply {
                        this[0] = String.format(this[0], scheduleName)
                    }
                HighlightedText(
                    texts = title,
                    highlightIndices = listOf(1),
                    defaultStyle =
                    KoinTheme.typography.medium16.copy(
                        color = KoinTheme.colors.neutral800
                    ),
                    highlightStyle =
                    KoinTheme.typography.bold16.copy(
                        color = KoinTheme.colors.danger700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.delete_schedule_description),
                    style =
                    KoinTheme.typography.medium16.copy(
                        color = KoinTheme.colors.neutral800
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        modifier =
                        Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        colors =
                        ButtonColors(
                            containerColor = KoinTheme.colors.neutral0,
                            contentColor = KoinTheme.colors.neutral500,
                            disabledContainerColor = KoinTheme.colors.neutral400,
                            disabledContentColor = KoinTheme.colors.neutral500
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(0.dp),
                        border = BorderStroke(1.dp, KoinTheme.colors.neutral500),
                        onClick = { onDismiss() }
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_cancellation),
                            style = KoinTheme.typography.medium15,
                            color = KoinTheme.colors.neutral600
                        )
                    }
                    FilledTextButton(
                        modifier =
                        Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.delete_schedule_confirmation),
                        buttonStyle = FilledButtonType.Danger,
                        onClick = { onConfirm() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteScheduleDialogPreview() {
    KoinTheme {
        DeleteScheduleDialog(
            scheduleName = "일정명",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
