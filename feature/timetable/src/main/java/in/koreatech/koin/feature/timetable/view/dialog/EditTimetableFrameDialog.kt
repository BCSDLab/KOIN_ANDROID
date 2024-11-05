package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledButtonType
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.component.HighlightedText
import `in`.koreatech.koin.feature.timetable.component.TextCheckbox

data class TimeTableFrameState(
    val id: Int,
    val timetableName: String,
    val isMain: Boolean
)

@Composable
fun EditTimetableFrameDialog(
    timetableFrameState: TimeTableFrameState,
    onDismiss: () -> Unit,
    onConfirmEdit: (TimeTableFrameState) -> Unit,
    onDeleteFrame: (TimeTableFrameState) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMain by remember { mutableStateOf(timetableFrameState.isMain) }
    var timetableName by remember { mutableStateOf(timetableFrameState.timetableName) }
    var showingDeleteDialog by remember { mutableStateOf(false) }

    EditTimetableFrameDialog(
        modifier = modifier,
        timetableName = timetableName,
        isMain = isMain,
        onDismiss = onDismiss,
        onConfirm = {
            onConfirmEdit(
                timetableFrameState.copy(
                    timetableName = timetableName,
                    isMain = isMain
                )
            )
        },
        onClickDelete = { showingDeleteDialog = true },
        onValueChanged = {
            timetableName = it
        },
        onCheckChanged = {
            isMain = it
        },
    )

    if (showingDeleteDialog) {
        DeleteTimetableFrameDialog(
            timetableName = timetableName,
            onDismiss = { showingDeleteDialog = false },
            onConfirm = {
                onDeleteFrame(timetableFrameState)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTimetableFrameDialog(
    timetableName: String,
    isMain: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onClickDelete: () -> Unit,
    onValueChanged: (String) -> Unit,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(color = KoinTheme.colors.neutral0, shape = KoinTheme.shapes.extraSmall)
                .padding(horizontal = 24.dp)
        ) {
            FilledTextButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(24.dp),
                text = stringResource(id = R.string.edit_titletable_frame_delete),
                textStyle = KoinTheme.typography.medium14,
                buttonStyle = FilledButtonType.Danger,
                onClick = onClickDelete
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 26.dp, horizontal = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.edit_titletable_frame_title),
                    style = KoinTheme.typography.bold16
                )
                // TODO:: 높이 수정 필요
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = KoinTheme.colors.neutral300
                            ),
                            shape = KoinTheme.shapes.extraSmall
                        ),
                    value = timetableName,
                    textStyle = KoinTheme.typography.regular14.copy(
                        color = KoinTheme.colors.neutral500
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = KoinTheme.colors.neutral100, // 배경색 (클릭 X)
                        focusedContainerColor = KoinTheme.colors.neutral100, // 배경색 (클릭 O)
                        unfocusedIndicatorColor = Color.Transparent, // 밑줄색 (클릭 X)
                        focusedIndicatorColor = Color.Transparent, // 밑줄색 (클릭 O)
                        cursorColor = Color.Black, // 클릭 시, 커서색
                        focusedTextColor = Color.Black // 클릭 시, 입력 텍스트 색
                    ),
                    onValueChange = onValueChanged
                )
                TextCheckbox(
                    text = stringResource(id = R.string.edit_titletable_frame_main),
                    textStyle = KoinTheme.typography.medium14,
                    isChecked = isMain,
                    onCheckChanged = onCheckChanged
                )
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedBoxButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.common_cancellation),
                        onClick = onDismiss,
                        colors = OutlinedBoxButtonColors.Neutral
                    )
                    FilledTextButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.common_save),
                        onClick = onConfirm
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteTimetableFrameDialog(
    timetableName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringArrayResource(id = R.array.delete_titletable_frame_title).apply {
        this[0] = String.format(this[0], timetableName)
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = KoinTheme.shapes.extraSmall
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HighlightedText(
                    texts = title,
                    highlightIndices = listOf(1),
                    defaultStyle = KoinTheme.typography.medium16.copy(
                        color = KoinTheme.colors.neutral600,
                    ),
                    highlightStyle = KoinTheme.typography.bold16.copy(
                        color = KoinTheme.colors.danger700,
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedBoxButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.common_cancellation),
                        onClick = onDismiss,
                        colors = OutlinedBoxButtonColors.Neutral
                    )
                    FilledButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.delete_titletable_frame_confirmation),
                        onClick = onConfirm,
                        colors = FilledButtonColors.Danger
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun EditTimetableFrameDialogPreview() {
    KoinTheme {
        EditTimetableFrameDialog(
            timetableFrameState = TimeTableFrameState(
                id = 1,
                timetableName = "시간표1",
                isMain = true
            ),
            onDismiss = {},
            onConfirmEdit = {},
            onDeleteFrame = {}
        )
    }
}

@Preview
@Composable
private fun DeleteTimetableFrameDialogPreview() {
    KoinTheme {
        DeleteTimetableFrameDialog(
            timetableName = "시간표1",
            onDismiss = {},
            onConfirm = {},
        )
    }
}