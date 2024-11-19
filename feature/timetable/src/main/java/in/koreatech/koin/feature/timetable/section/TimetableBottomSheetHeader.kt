package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.view.TimetableBottomSheetContentMode

@Composable
fun TimetableBottomSheetHeader(
    modifier: Modifier = Modifier,
    mode: TimetableBottomSheetContentMode = TimetableBottomSheetContentMode.BASIC,
    onComplete: () -> Unit = {},
    onClickAddLectureMode: (mode: TimetableBottomSheetContentMode) -> Unit = {},
    onClickAddCustomLectureMode: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_extra_custom_lecture),
            style = when (mode) {
                TimetableBottomSheetContentMode.BASIC -> KoinTheme.typography.medium18.copy(color = KoinTheme.colors.primary500)
                TimetableBottomSheetContentMode.CUSTOM -> KoinTheme.typography.bold18.copy(color = KoinTheme.colors.primary600)
            },
            modifier = Modifier.clickable {
                onClickAddCustomLectureMode()
            }
        )
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_extra_lecture),
            style = when (mode) {
                TimetableBottomSheetContentMode.BASIC -> KoinTheme.typography.bold18.copy(color = KoinTheme.colors.primary600)
                TimetableBottomSheetContentMode.CUSTOM -> KoinTheme.typography.medium18.copy(color = KoinTheme.colors.primary500)
            },
            modifier = Modifier.clickable {
                onClickAddLectureMode(TimetableBottomSheetContentMode.BASIC)
            }
        )
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_complete),
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.clickable {
                onComplete()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetHeaderPreview() {
    TimetableBottomSheetHeader()
}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetHeaderPreview_Custom() {
    TimetableBottomSheetHeader(
        mode = TimetableBottomSheetContentMode.CUSTOM
    )
}