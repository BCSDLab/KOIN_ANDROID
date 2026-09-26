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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledButtonType
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.model.dummyLecture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteLectureDialog(
    lecture: TimetableLecture?,
    onConfirm: (id: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RebrandKoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.delete_lecture_title, lecture?.classTitle ?: ""),
                    style = RebrandKoinTheme.typography.medium16.copy(
                        color = RebrandKoinTheme.colors.neutral800
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.delete_lecture_description),
                    style = RebrandKoinTheme.typography.medium16.copy(
                        color = RebrandKoinTheme.colors.neutral800
                    ),
                    textAlign = TextAlign.Center
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
                            containerColor = RebrandKoinTheme.colors.neutral0,
                            contentColor = RebrandKoinTheme.colors.neutral500,
                            disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                            disabledContentColor = RebrandKoinTheme.colors.neutral500
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(0.dp),
                        border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral500),
                        onClick = { onDismiss() }
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_cancellation),
                            style = RebrandKoinTheme.typography.medium15,
                            color = RebrandKoinTheme.colors.neutral600
                        )
                    }
                    FilledTextButton(
                        modifier =
                        Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.delete_lecture_confirmation),
                        buttonStyle = FilledButtonType.Danger,
                        onClick = {
                            lecture?.let {
                                onConfirm(it.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteLectureDialogPreview() {
    RebrandKoinTheme {
        DeleteLectureDialog(
            lecture = dummyLecture.toTimetableLecture(),
            onConfirm = {},
            onDismiss = {}
        )
    }
}
