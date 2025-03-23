package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestLoginDialog(
    onConfirm: () -> Unit,
    onDismiss: (visible: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss(false) },
        modifier = modifier
    ) {
        Surface(
            modifier =
            Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = KoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.request_login_title),
                    color = KoinTheme.colors.neutral800,
                    textAlign = TextAlign.Center,
                    style = KoinTheme.typography.bold16
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.request_login_description),
                    color = KoinTheme.colors.neutral800,
                    textAlign = TextAlign.Center,
                    style = KoinTheme.typography.regular14
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
                        onClick = { onDismiss(false) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.lecture_duplication_cancellation),
                            style = KoinTheme.typography.medium15,
                            color = KoinTheme.colors.neutral600
                        )
                    }
                    FilledTextButton(
                        modifier =
                        Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.request_login_confirmation),
                        onClick = {
                            EventLogger.logClickEvent(
                                EventAction.USER,
                                AnalyticsConstant.Label.LOGIN_PROMPT,
                                "시간표 관리 팝업"
                            )
                            onConfirm()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RequestLoginDialogPreview() {
    KoinTheme {
        RequestLoginDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
