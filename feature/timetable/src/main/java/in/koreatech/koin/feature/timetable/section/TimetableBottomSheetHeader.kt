package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun TimetableBottomSheetHeader(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {},
    onAddLecture: () -> Unit = {},
    onAddCustomLecture: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "직접추가",
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.primary500,
            modifier = Modifier.clickable {
                onAddCustomLecture()
            }
        )
        Text(
            text = "수업 추가",
            style = KoinTheme.typography.bold18,
            color = KoinTheme.colors.primary600,
            modifier = Modifier.clickable {
                onAddLecture()
            }
        )
        Text(
            text = "완료",
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