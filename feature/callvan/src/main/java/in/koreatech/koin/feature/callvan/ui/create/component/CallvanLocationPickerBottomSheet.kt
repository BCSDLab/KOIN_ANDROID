package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanBottomSheet

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CallvanLocationPickerBottomSheet(
    isDeparture: Boolean,
    modifier: Modifier = Modifier,
    initialSelection: CallvanLocationOption? = null,
    initialCustomText: String? = null,
    onLocationSelected: (CallvanLocationOption, String?) -> Unit = { _, _ -> },
    onDismiss: () -> Unit = {}
) {
    var selectedLocation by remember(initialSelection) { mutableStateOf(initialSelection) }
    var customText by remember(initialSelection, initialCustomText) {
        mutableStateOf(
            if (initialSelection == CallvanLocationOption.CUSTOM) initialCustomText.orEmpty() else ""
        )
    }
    val isOtherSelected by remember { derivedStateOf { selectedLocation == CallvanLocationOption.CUSTOM } }
    val customInputDescription = stringResource(
        if (isDeparture) {
            R.string.callvan_create_departure_placeholder
        } else {
            R.string.callvan_create_arrival_placeholder
        }
    )

    CallvanBottomSheet(
        title = stringResource(
            if (isDeparture) {
                R.string.callvan_create_departure_picker_question
            } else {
                R.string.callvan_create_arrival_picker_question
            }
        ),
        onDismiss = onDismiss,
        showCloseButton = true
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CallvanLocationOption.entries.forEach { location ->
                        val isSelected = selectedLocation == location
                        Box(
                            modifier = Modifier
                                .height(34.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) {
                                        RebrandKoinTheme.colors.primary500
                                    } else {
                                        RebrandKoinTheme.colors.neutral300
                                    },
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .clip(RoundedCornerShape(24.dp))
                                .clickable {
                                    selectedLocation = location
                                    if (location != CallvanLocationOption.CUSTOM) customText = ""
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(location.displayNameRes),
                                style = RebrandKoinTheme.typography.medium14,
                                color = if (isSelected) {
                                    RebrandKoinTheme.colors.primary500
                                } else {
                                    RebrandKoinTheme.colors.neutral500
                                }
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = isOtherSelected,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    BasicTextField(
                        value = customText,
                        onValueChange = { customText = it },
                        singleLine = true,
                        textStyle = RebrandKoinTheme.typography.regular14.copy(
                            color = RebrandKoinTheme.colors.neutral600
                        ),
                        modifier = Modifier
                            .semantics {
                                contentDescription = customInputDescription
                            }
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .border(1.dp, RebrandKoinTheme.colors.neutral300, RebrandKoinTheme.shapes.medium)
                            .background(RebrandKoinTheme.colors.neutral0, RebrandKoinTheme.shapes.medium)
                            .padding(horizontal = 20.dp, vertical = 13.dp)
                    )
                }
                HorizontalDivider(
                    color = RebrandKoinTheme.colors.neutral300,
                    thickness = 0.5.dp
                )
            }
            FilledButton(
                text = stringResource(
                    if (isOtherSelected) {
                        R.string.callvan_create_location_picker_confirm_other
                    } else {
                        R.string.callvan_create_location_picker_select
                    }
                ),
                onClick = {
                    selectedLocation?.let { loc ->
                        onLocationSelected(loc, if (isOtherSelected) customText.trim() else null)
                    }
                },
                enabled = selectedLocation != null && (!isOtherSelected || customText.isNotBlank()),
                modifier = Modifier.fillMaxWidth(),
                shape = RebrandKoinTheme.shapes.medium,
                textStyle = RebrandKoinTheme.typography.bold16,
                contentPadding = PaddingValues(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500,
                    disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                    contentColor = RebrandKoinTheme.colors.neutral0,
                    disabledContentColor = RebrandKoinTheme.colors.neutral0
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationPickerBottomSheetPreview() {
    RebrandKoinTheme {
        CallvanLocationPickerBottomSheet(
            isDeparture = true,
            initialSelection = CallvanLocationOption.FRONT_GATE,
            initialCustomText = null,
            onLocationSelected = { _, _ -> },
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationPickerBottomSheetCustomPreview() {
    RebrandKoinTheme {
        CallvanLocationPickerBottomSheet(
            isDeparture = true,
            initialSelection = CallvanLocationOption.CUSTOM,
            initialCustomText = "test test test",
            onLocationSelected = { _, _ -> },
            onDismiss = {}
        )
    }
}
