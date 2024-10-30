package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
        Text(text = "직접추가")
        Text(text = "수업 추가")
        Text(text = "완료")
    }
}

@Preview
@Composable
private fun TimetableBottomSheetHeaderPreview() {
    TimetableBottomSheetHeader()
}