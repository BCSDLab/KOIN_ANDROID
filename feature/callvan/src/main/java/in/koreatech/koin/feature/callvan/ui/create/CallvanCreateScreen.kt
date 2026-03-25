package `in`.koreatech.koin.feature.callvan.ui.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanDateField
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanLocationPickerBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanLocationSection
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanParticipantsSection
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanSubmitBottomBar
import `in`.koreatech.koin.feature.callvan.ui.create.component.CallvanTimeField
import `in`.koreatech.koin.feature.callvan.ui.displayNameRes
import java.time.LocalDate
import java.time.LocalTime
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CallvanCreateScreen(
    viewModel: CallvanCreateViewModel = hiltViewModel(),
    onCompleteAndNavigateToMain: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    viewModel.collectSideEffect { effect ->
        when (effect) {
            CallvanCreateSideEffect.NavigateToMain -> onCompleteAndNavigateToMain()
            is CallvanCreateSideEffect.ShowSubmitError ->
                snackbarHostState.showSnackbar(context.getString(effect.type.messageRes))
            CallvanCreateSideEffect.ShowPastTimeError -> snackbarHostState.showSnackbar(
                context.getString(R.string.callvan_create_past_time_error)
            )
        }
    }

    if (state.isLocationPickerVisible) {
        CallvanLocationPickerBottomSheet(
            isDeparture = state.isPickingDeparture,
            initialSelection = if (state.isPickingDeparture) state.departureLocation else state.arrivalLocation,
            initialCustomText = if (state.isPickingDeparture) state.departureCustomText else state.arrivalCustomText,
            onLocationSelected = { location, customText ->
                EventLogger.logCampusClickEvent(
                    if (state.isPickingDeparture) {
                        AnalyticsConstant.Label.Callvan.CALLVAN_WRITE_DEPARTURE
                    } else {
                        AnalyticsConstant.Label.Callvan.CALLVAN_WRITE_ARRIVAL
                    },
                    context.getString(location.displayNameRes()) + if (location == CallvanLocationOption.CUSTOM) ", $customText" else ""
                )
                viewModel.selectLocation(location, customText)
            },
            onDismiss = viewModel::closeLocationPicker
        )
    }

    CallvanCreateScreenImpl(
        state = state,
        snackbarHostState = snackbarHostState,
        onDepartureLocationClick = viewModel::openDepartureLocationPicker,
        onArrivalLocationClick = viewModel::openArrivalLocationPicker,
        onSwapLocations = viewModel::swapLocations,
        onDateFieldClick = viewModel::toggleDatePicker,
        onDateChange = viewModel::updateDate,
        onDateReset = viewModel::resetDate,
        onDateConfirm = viewModel::confirmDate,
        onTimeFieldClick = viewModel::toggleTimePicker,
        onTimeChange = viewModel::updateTime,
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
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onDepartureLocationClick: () -> Unit = {},
    onArrivalLocationClick: () -> Unit = {},
    onSwapLocations: () -> Unit = {},
    onDateFieldClick: () -> Unit = {},
    onDateChange: (LocalDate) -> Unit = {},
    onDateReset: () -> Unit = {},
    onDateConfirm: () -> Unit = {},
    onTimeFieldClick: () -> Unit = {},
    onTimeChange: (LocalTime) -> Unit = {},
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
                onNavigationIconClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Callvan.CALLVAN_WRITE_BACK,
                        ""
                    )
                    onTopbarBackClick()
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = RebrandKoinTheme.colors.neutral0
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
                    isPickerVisible = state.isTimePickerVisible,
                    selectedDate = state.selectedDate,
                    selectedTime = state.selectedTime,
                    onFieldClick = onTimeFieldClick,
                    onTimeChange = onTimeChange,
                    onReset = onTimeReset,
                    onConfirm = {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Callvan.CALLVAN_WRITE_TIME,
                            "${state.selectedTime.hour}:${"%02d".format(state.selectedTime.minute)}"
                        )
                        onTimeConfirm()
                    }
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
                onSubmit = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Callvan.CALLVAN_WRITE_DONE,
                        ""
                    )
                    onSubmit()
                }
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
            selectedTime = LocalTime.of(9, 0),
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
