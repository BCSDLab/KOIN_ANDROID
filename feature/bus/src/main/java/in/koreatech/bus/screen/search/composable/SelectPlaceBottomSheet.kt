package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.type.PlaceSelectMode
import `in`.koreatech.bus.type.PlaceType
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectPlaceBottomSheet(
    onDismissRequest: () -> Unit,
    selectMode: PlaceSelectMode,
    onConfirmSelection: (selectedPlace: PlaceType) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(selectMode != PlaceSelectMode.NONE) {
        "SelectPlaceBottomSheet should not be used with PlaceSelectMode.NONE"
    }

    val context = LocalContext.current

    var selectedPlace by remember { mutableStateOf(PlaceType.KOREATECH) }
    val sheetTitle = when (selectMode) {
        PlaceSelectMode.DEPARTURE -> stringResource(R.string.question_departure)
        PlaceSelectMode.ARRIVAL -> stringResource(R.string.question_arrival)
        PlaceSelectMode.NONE -> ""
    }
    val buttonText = when (selectMode) {
        PlaceSelectMode.DEPARTURE -> stringResource(R.string.action_select_arrival)
        PlaceSelectMode.ARRIVAL -> stringResource(R.string.confirm_selection)
        PlaceSelectMode.NONE -> ""
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {

        Text(
            text = sheetTitle,
            style = KoinTheme.typography.medium18,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 32.dp).padding(bottom = 12.dp)
        )

        HorizontalDivider(
            color = KoinTheme.colors.neutral200
        )

        TextChipGroup(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            titles = PlaceType.entries.map { context.getString(it.titleRes) },
            onChipSelected = {
                selectedPlace = PlaceType.entries.find { type ->
                    context.getString(type.titleRes) == it
                } ?: PlaceType.KOREATECH
            },
            selectedChipIndexes = intArrayOf(selectedPlace.ordinal),
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            showClickRipple = false
        )

        Spacer(modifier = Modifier.height(140.dp))
        FilledButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).padding(bottom = 36.dp),
            text = buttonText,
            onClick = { onConfirmSelection(selectedPlace) },
            contentPadding = PaddingValues(vertical = 12.dp)
        )
    }
}

@Preview
@Composable
private fun SelectPlaceBottomSheetPreview() {
    SelectPlaceBottomSheet(
        onDismissRequest = {},
        selectMode = PlaceSelectMode.DEPARTURE,
        onConfirmSelection = {}
    )
}