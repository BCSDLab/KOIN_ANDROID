package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.mock.expressTimetableMock
import `in`.koreatech.bus.state.ExpressTimetableState
import `in`.koreatech.bus.type.CommonDirectionType
import `in`.koreatech.bus.util.formatUpdatedTime
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
internal fun ExpressTimetableScreenContent(
    expressTimetable: ExpressTimetableState,
    modifier: Modifier = Modifier,
    onDirectionChanged: (CommonDirectionType) -> Unit = {}
) {

    var selectedDirectionType by rememberSaveable { mutableStateOf(CommonDirectionType.TO_BYEONGCHEON) }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.starting_point),
                style = KoinTheme.typography.regular16,
                color = KoinTheme.colors.neutral600
            )

            TextChipGroup(
                modifier = Modifier.padding(start = 16.dp),
                titles = CommonDirectionType.entries.map { stringResource(it.titleRes) },
                onChipSelected = { title ->
                    selectedDirectionType =
                        CommonDirectionType.entries.find { context.getString(it.titleRes) == title } ?: CommonDirectionType.TO_BYEONGCHEON
                },
                selectedChipIndexes = intArrayOf(selectedDirectionType.ordinal)
            )
        }

        CommonTimetableView(
            timetable = expressTimetable.timetable,
            updatedAt = expressTimetable.updatedAt.formatUpdatedTime(),
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.background(Color.White).fillMaxWidth().height(115.dp))
    }

    LaunchedEffect(selectedDirectionType) {
        onDirectionChanged(selectedDirectionType)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpressTimetableScreenPreview() {
    ExpressTimetableScreenContent(
        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
        expressTimetable = expressTimetableMock
    )
}