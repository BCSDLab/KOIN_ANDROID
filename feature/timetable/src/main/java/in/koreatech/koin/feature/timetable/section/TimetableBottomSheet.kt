package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.feature.timetable.component.TimetableSearchBox

@Composable
fun TimetableBottomSheet(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (text: String) -> Unit = {},
    onBottomSheetHeightChange: (height: Float) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
            .background(Color.White)
            .onGloballyPositioned {
                onBottomSheetHeightChange(it.size.height.toFloat())
            },
    ) {
        TimetableSearchBox(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange
        )
    }
}

@Preview
@Composable
private fun TimetableBottomSheetPreview() {
    TimetableBottomSheet(
        searchText = ""
    )
}