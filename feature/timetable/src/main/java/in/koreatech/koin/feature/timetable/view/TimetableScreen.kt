package `in`.koreatech.koin.feature.timetable.view

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import `in`.koreatech.koin.feature.timetable.section.TimetableBottomSheet

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableScreen(
    searchText: String,
    sheetState: BottomSheetState,
    scaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    onSearchTextChange: (text: String) -> Unit = {},
) {
    var bottomSheetHeight by remember { mutableStateOf(0f) }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            TimetableBottomSheet(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onBottomSheetHeightChange = { bottomSheetHeight = it }
            )
        },
        sheetPeekHeight = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .dynamicPadding(sheetState, bottomSheetHeight)
        ) {
            Timetable(
                range = 15,
                events = listOf(dummyEvent)
            )
        }
    }

}

val Float.pxToDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp

@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.dynamicPadding(
    sheetState: BottomSheetState,
    sheetHeight: Float,
) = padding(
    bottom = if (sheetState.isExpanded) {
        if (sheetState.progress == 1.0f) {
            if (sheetState.currentValue == BottomSheetValue.Expanded && sheetState.targetValue == BottomSheetValue.Collapsed) {
                0.dp
            } else {
                sheetHeight.pxToDp
            }
        } else {
            sheetHeight.pxToDp * (1.0f - sheetState.progress)
        }
    } else {
        0.dp
    }
)

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetableScreenPreview() {
    TimetableScreen(
        searchText = "",
        sheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        ),
        scaffoldState = rememberBottomSheetScaffoldState()
    )
}