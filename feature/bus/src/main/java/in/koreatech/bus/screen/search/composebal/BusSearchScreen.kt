package `in`.koreatech.bus.screen.search.composebal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusSearchScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusSearchViewModel = hiltViewModel()
) {

    val departure by viewModel.departure.collectAsStateWithLifecycle()
    val arrival by viewModel.arrival.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_search),
            onNavigationIconClick = onNavigationIconClick
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.introduce_bus_search),
            style = KoinTheme.typography.medium16,
            color = KoinTheme.colors.neutral800
        )

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = stringResource(R.string.caution_possibly_inaccurate),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral600
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 46.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            BusSearchInput(
                title = stringResource(R.string.departure),
                place = departure,
                placeholder = stringResource(R.string.select_departure),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = viewModel::swapDepartureAndArrival,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swap),
                    contentDescription = stringResource(R.string.swap_content_description)
                )
            }

            BusSearchInput(
                title = stringResource(R.string.arrival),
                place = arrival,
                placeholder = stringResource(R.string.select_arrival),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BusSearchInput(
    title: String,
    place: String,
    placeholder: String,
    modifier: Modifier = Modifier,
) {

    val isPlaceDetermined by remember { derivedStateOf { place.isNotEmpty() } }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = KoinTheme.typography.medium16,
            color = KoinTheme.colors.primary500
        )

        Box(
            modifier = Modifier
                .background(if(isPlaceDetermined.not()) KoinTheme.colors.neutral100 else Color.Transparent)
                .padding(vertical = 12.dp, horizontal = 39.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isPlaceDetermined.not())
                Text(
                    text = placeholder,
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral400     // TODO neutral450 ?
                )
            else {
                Text(
                    text = place,
                    style = KoinTheme.typography.bold18,
                    color = KoinTheme.colors.neutral800
                )
            }
        }
    }
}

@Preview
@Composable
private fun BusSearchScreenPreview() {
    BusSearchScreen(
        modifier = Modifier.fillMaxWidth()
    )
}