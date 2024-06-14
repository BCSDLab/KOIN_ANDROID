package `in`.koreatech.koin.ui.timetablev2.view

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.koreatech.koin.ui.timetablev2.TimetableSideEffect
import `in`.koreatech.koin.ui.timetablev2.viewmodel.TimetableViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    timetableViewModel: TimetableViewModel = viewModel(),
    content: @Composable ColumnScope.() -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
) {
    timetableViewModel.loadSemesters()

    val state by timetableViewModel.collectAsState()

    timetableViewModel.collectSideEffect {
        when (it) {
            is TimetableSideEffect.Toast -> Unit
        }
    }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = sheetContent,
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            content()
            TimetableContentHeader(
                semesters = state.semesters,
                modifier = Modifier.fillMaxWidth(),
                onSavedImage = {},
                onSemesterTextChanged = {}
            )
        }
    }
}

