package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.type.PlaceSelectMode
import `in`.koreatech.bus.type.PlaceType
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip2
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun SelectPlaceBottomSheet(
    onDismissRequest: () -> Unit,
    selectMode: PlaceSelectMode,
    onConfirmSelection: (selectedPlace: PlaceType) -> Unit,
    modifier: Modifier = Modifier,
    disabledPlace: PlaceType? = null
) {
    require(selectMode != PlaceSelectMode.NONE) {
        "SelectPlaceBottomSheet should not be used with PlaceSelectMode.NONE"
    }

    var selectedPlace by remember(disabledPlace) {
        mutableStateOf(
            PlaceType.entries.first {
                it != disabledPlace
            }
        )
    }

    val sheetTitle =
        when (selectMode) {
            PlaceSelectMode.DEPARTURE -> stringResource(R.string.question_departure)
            PlaceSelectMode.ARRIVAL -> stringResource(R.string.question_arrival)
            PlaceSelectMode.NONE -> ""
        }
    val buttonText =
        when (selectMode) {
            PlaceSelectMode.DEPARTURE -> stringResource(R.string.action_select_arrival)
            PlaceSelectMode.ARRIVAL -> stringResource(R.string.confirm_selection)
            PlaceSelectMode.NONE -> ""
        }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(
                text = sheetTitle,
                style = RebrandKoinTheme.typography.bold18,
                color = RebrandKoinTheme.colors.primary500
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onDismissRequest)
                    .padding(1.dp),
                tint = RebrandKoinTheme.colors.neutral800,
                imageVector = ImageVector.vectorResource(R.drawable.ic_x),
                contentDescription = null
            )
        }

        FlowRow(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CompositionLocalProvider(LocalTextStyle provides RebrandKoinTheme.typography.bold15) {
                PlaceType.entries.fastForEach {
                    TextChip2(
                        shape = RoundedCornerShape(24.dp),
                        showClickRipple = false,
                        title = stringResource(it.titleRes),
                        chipColors = if (disabledPlace == it) {
                            TextChipDefaults.chipColors(
                                selectedContainerColor = RebrandKoinTheme.colors.neutral0,
                                selectedContentColor = RebrandKoinTheme.colors.primary500,
                                unselectedContainerColor = RebrandKoinTheme.colors.neutral0,
                                unselectedContentColor = RebrandKoinTheme.colors.neutral300
                            )
                        } else {
                            TextChipDefaults.chipColors(
                                selectedContainerColor = RebrandKoinTheme.colors.neutral0,
                                selectedContentColor = RebrandKoinTheme.colors.primary500,
                                unselectedContainerColor = RebrandKoinTheme.colors.neutral0,
                                unselectedContentColor = RebrandKoinTheme.colors.neutral500
                            )
                        },
                        border = if (disabledPlace == it) {
                            TextChipDefaults.chipBorder(
                                selectedBorderStroke = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500),
                                unselectedBorderStroke = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral300)
                            )
                        } else {
                            TextChipDefaults.chipBorder(
                                selectedBorderStroke = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500),
                                unselectedBorderStroke = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral300)
                            )
                        },
                        isSelected = selectedPlace == it,
                        onSelect = {
                            if (it != disabledPlace) {
                                selectedPlace = PlaceType.entries.find { type ->
                                    type == it
                                } ?: PlaceType.KOREATECH
                            }
                        },
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 36.dp),
            onClick = { onConfirmSelection(selectedPlace) },
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0,
                disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                disabledContentColor = RebrandKoinTheme.colors.neutral0
            ),
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = buttonText,
                style = RebrandKoinTheme.typography.medium15
            )
        }
    }
}

@Preview
@Composable
private fun SelectPlaceBottomSheetPreview() {
    SelectPlaceBottomSheet(
        onDismissRequest = {},
        selectMode = PlaceSelectMode.DEPARTURE,
        onConfirmSelection = {},
        disabledPlace = PlaceType.TERMINAL
    )
}
