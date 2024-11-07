package `in`.koreatech.bus.screen.search.composebal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.search.type.PlaceSelectMode
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchViewModel
import `in`.koreatech.bus.screen.search.viewmodel.SearchBusUiState
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusSearchScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onSearchSuccess: (Unit) -> Unit = {},
    viewModel: BusSearchViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val departure by viewModel.departure.collectAsStateWithLifecycle()
    val arrival by viewModel.arrival.collectAsStateWithLifecycle()

    val searchButtonEnabled by remember { derivedStateOf { departure.isNotEmpty() && arrival.isNotEmpty() } }

    var placeSelectMode by remember { mutableStateOf(PlaceSelectMode.NONE) }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_search),
            onNavigationIconClick = onNavigationIconClick
        )

        BusSearchContentView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            departure = departure,
            arrival = arrival,
            searchButtonEnabled = searchButtonEnabled,
            onSwapIconClicked = viewModel::swapDepartureAndArrival,
            onSearchClicked = viewModel::search,
            onDepartureFieldClicked = { placeSelectMode = PlaceSelectMode.DEPARTURE },
            onArrivalFieldClicked = { placeSelectMode = PlaceSelectMode.ARRIVAL }
        )
    }

    if (placeSelectMode != PlaceSelectMode.NONE) {
        SelectPlaceBottomSheet(
            onDismissRequest = { placeSelectMode = PlaceSelectMode.NONE },
            selectMode = placeSelectMode,
            onConfirmSelection = {
                when (placeSelectMode) {
                    PlaceSelectMode.DEPARTURE -> {
                        placeSelectMode = PlaceSelectMode.ARRIVAL
                        viewModel.setDeparture(context.getString(it.titleRes))
                    }

                    PlaceSelectMode.ARRIVAL -> {
                        placeSelectMode = PlaceSelectMode.NONE
                        viewModel.setArrival(context.getString(it.titleRes))
                    }

                    PlaceSelectMode.NONE -> Unit
                }
            },
            modifier = Modifier,
        )
    }


    LaunchedEffect(Unit) {
        viewModel.searchBusUiState.collect {
            when(it) {
                is SearchBusUiState.Loading -> Unit
                is SearchBusUiState.EmptyDeparture -> Unit
                is SearchBusUiState.EmptyArrival -> Unit
                is SearchBusUiState.Success -> onSearchSuccess(it.data) // TODO : data 타입
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BusSearchScreenPreview() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_search),
            onNavigationIconClick = { }
        )

        BusSearchContentView(
            departure = "",
            arrival = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            onSwapIconClicked = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BusSearchScreen2Preview() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_search),
            onNavigationIconClick = { }
        )

        BusSearchContentView(
            departure = "코리아텍",
            arrival = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            onSwapIconClicked = { }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BusSearchScreen3Preview() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_search),
            onNavigationIconClick = { }
        )

        BusSearchContentView(
            departure = "코리아텍",
            arrival = "천안역",
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            onSwapIconClicked = { },
            searchButtonEnabled = true
        )
    }
}