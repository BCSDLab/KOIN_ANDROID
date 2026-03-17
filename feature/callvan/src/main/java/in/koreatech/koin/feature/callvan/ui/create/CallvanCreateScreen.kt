package `in`.koreatech.koin.feature.callvan.ui.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanDateField
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanLocationPickerBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanLocationSection
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanParticipantsSection
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanSubmitBottomBar
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanTimeField
import `in`.koreatech.koin.feature.callvan.ui.create.model.CallvanLocationOption
import java.time.LocalDate
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CallvanCreateScreen(
    viewModel: CallvanCreateViewModel = hiltViewModel(),
    onCompleteAndNavigateToMain: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            CallvanCreateSideEffect.NavigateToMain -> onCompleteAndNavigateToMain()
            CallvanCreateSideEffect.ShowSubmitError -> {}
        }
    }

    if (state.isLocationPickerVisible) {
        CallvanLocationPickerBottomSheet(
            isDeparture = state.isPickingDeparture,
            initialSelection = if (state.isPickingDeparture) state.departureLocation else state.arrivalLocation,
            initialCustomText = if (state.isPickingDeparture) state.departureCustomText else state.arrivalCustomText,
            onLocationSelected = { location, customText -> viewModel.selectLocation(location, customText) },
            onDismiss = viewModel::closeLocationPicker
        )
    }

    CallvanCreateScreenImpl(
        state = state,
        onDepartureLocationClick = viewModel::openDepartureLocationPicker,
        onArrivalLocationClick = viewModel::openArrivalLocationPicker,
        onSwapLocations = viewModel::swapLocations,
        onDateFieldClick = viewModel::toggleDatePicker,
        onDateChange = viewModel::updateDate,
        onDateReset = viewModel::resetDate,
        onDateConfirm = viewModel::confirmDate,
        onTimeFieldClick = viewModel::toggleTimePicker,
        onAmPmIndexChange = viewModel::updateAmPm,
        onHourIndexChange = viewModel::updateHour,
        onMinuteIndexChange = viewModel::updateMinute,
        onTimeReset = viewModel::resetTime,
        onTimeConfirm = viewModel::confirmTime,
        onDecrement = viewModel::decrementParticipants,
        onIncrement = viewModel::incrementParticipants,
        onSubmit = viewModel::submit,
        onTopbarBackClick = onTopbarBackClick
    )
}

@Suppress("LongParameterList")
@Composable
fun CallvanCreateScreenImpl(
    state: CallvanCreateState,
    onDepartureLocationClick: () -> Unit = {},
    onArrivalLocationClick: () -> Unit = {},
    onSwapLocations: () -> Unit = {},
    onDateFieldClick: () -> Unit = {},
    onDateChange: (LocalDate) -> Unit = {},
    onDateReset: () -> Unit = {},
    onDateConfirm: () -> Unit = {},
    onTimeFieldClick: () -> Unit = {},
    onAmPmIndexChange: (Int) -> Unit = {},
    onHourIndexChange: (Int) -> Unit = {},
    onMinuteIndexChange: (Int) -> Unit = {},
    onTimeReset: () -> Unit = {},
    onTimeConfirm: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onIncrement: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_create_top_bar),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        containerColor = KoinTheme.colors.neutral0
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                CallvanLocationSection(
                    departureLocation = state.departureLocation,
                    arrivalLocation = state.arrivalLocation,
                    departureCustomText = state.departureCustomText,
                    arrivalCustomText = state.arrivalCustomText,
                    onDepartureClick = onDepartureLocationClick,
                    onArrivalClick = onArrivalLocationClick,
                    onSwap = onSwapLocations
                )
                CallvanDateField(
                    formattedDate = state.formattedDate,
                    isPickerVisible = state.isDatePickerVisible,
                    selectedDate = state.selectedDate,
                    onFieldClick = onDateFieldClick,
                    onDateChange = onDateChange,
                    onReset = onDateReset,
                    onConfirm = onDateConfirm
                )
                CallvanTimeField(
                    formattedTime = state.formattedTime,
                    isPickerVisible = state.isTimePickerVisible,
                    isAm = state.isAm,
                    selectedHour = state.selectedHour,
                    selectedMinute = state.selectedMinute,
                    onFieldClick = onTimeFieldClick,
                    onAmPmIndexChange = onAmPmIndexChange,
                    onHourIndexChange = onHourIndexChange,
                    onMinuteIndexChange = onMinuteIndexChange,
                    onReset = onTimeReset,
                    onConfirm = onTimeConfirm
                )
                CallvanParticipantsSection(
                    count = state.maxParticipants,
                    onDecrement = onDecrement,
                    onIncrement = onIncrement
                )
            }
            CallvanSubmitBottomBar(
                isFormComplete = state.isFormComplete,
                isSubmitting = state.isSubmitting,
                onSubmit = onSubmit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanCreateScreenPreview() {
    CallvanCreateScreenImpl(
        state = CallvanCreateState()
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanCreateScreenFilledPreview() {
    CallvanCreateScreenImpl(
        state = CallvanCreateState(
            departureLocation = CallvanLocationOption.FRONT_GATE,
            arrivalLocation = CallvanLocationOption.ASAN_STATION,
            selectedHour = 9,
            selectedMinute = 0,
            isAm = true,
            maxParticipants = 8
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanCreateScreenDatePickerPreview() {
    CallvanCreateScreenImpl(
        state = CallvanCreateState(
            departureLocation = CallvanLocationOption.FRONT_GATE,
            arrivalLocation = CallvanLocationOption.ASAN_STATION,
            isDatePickerVisible = true
        )
    )
}
