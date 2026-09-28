package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.view.TimetableBottomSheetContentMode

@Composable
fun TimetableBottomSheetFooter(
    modifier: Modifier = Modifier,
    mode: TimetableBottomSheetContentMode = TimetableBottomSheetContentMode.BASIC,
    onClickAddLectureMode: (mode: TimetableBottomSheetContentMode) -> Unit = {},
    onClickAddCustomLectureMode: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            modifier = Modifier.weight(0.3f),
            shape = RebrandKoinTheme.shapes.medium,
            border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400),
            onClick = {
                when (mode) {
                    TimetableBottomSheetContentMode.CUSTOM -> {
                        onClickAddLectureMode(TimetableBottomSheetContentMode.BASIC)
                    }
                    TimetableBottomSheetContentMode.BASIC -> {
                        onClickAddCustomLectureMode()
                    }
                }
            },
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.neutral0,
                contentColor = RebrandKoinTheme.colors.neutral600
            )
        ) {
            Text(
                text = when (mode) {
                    TimetableBottomSheetContentMode.CUSTOM -> {
                        stringResource(R.string.timetable_bottom_sheet_extra_return)
                    }
                    TimetableBottomSheetContentMode.BASIC -> {
                        stringResource(R.string.timetable_bottom_sheet_extra_custom_lecture)
                    }
                },
                style = RebrandKoinTheme.typography.bold16.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        Button(
            modifier = Modifier.weight(0.7f),
            shape = RebrandKoinTheme.shapes.medium,
            onClick = onComplete,
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0
            )
        ) {
            Text(
                text = stringResource(R.string.timetable_bottom_sheet_complete),
                style = RebrandKoinTheme.typography.bold16.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableBottomSheetFooterPreview() {
    TimetableBottomSheetFooter()
}
