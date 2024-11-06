package `in`.koreatech.bus.screen.search.composebal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
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

        BusSearchContentView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            departure = departure,
            arrival = arrival,
            onSwapIconClicked = viewModel::swapDepartureAndArrival,
            onSearchClicked = viewModel::search
        )
    }
}

@Composable
private fun BusSearchContentView(
    departure: String,
    arrival: String,
    modifier: Modifier = Modifier,
    onSwapIconClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {}
) {

    KoinSurface {
        Column(modifier = modifier) {
            Text(
                modifier = Modifier,
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

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 46.dp)
                    .height(IntrinsicSize.Min)
            ) {
                val (departureText, arrivalText, departureField, arrivalField, iconSwap) = createRefs()

                Text(
                    modifier = Modifier.constrainAs(departureText) {
                        start.linkTo(departureField.start)
                        top.linkTo(parent.top)
                        end.linkTo(departureField.end)
                    },
                    text = stringResource(R.string.departure),
                    style = KoinTheme.typography.medium16,
                    color = KoinTheme.colors.primary500
                )
                Text(
                    modifier = Modifier.constrainAs(arrivalText) {
                        top.linkTo(parent.top)
                        start.linkTo(arrivalField.start)
                        end.linkTo(arrivalField.end)
                    },
                    text = stringResource(R.string.arrival),
                    style = KoinTheme.typography.medium16,
                    color = KoinTheme.colors.primary500
                )

                BusSearchInput(
                    place = departure,
                    placeholder = stringResource(R.string.select_departure),
                    modifier = Modifier.padding(top = 10.dp).constrainAs(departureField) {
                        top.linkTo(iconSwap.top)
                        bottom.linkTo(iconSwap.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconSwap.start)

                        width = Dimension.fillToConstraints
                        height = Dimension.preferredWrapContent
                    }
                )

                IconButton(
                    onClick = onSwapIconClicked,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .size(32.dp)
                        .constrainAs(iconSwap) {
                            top.linkTo(departureText.bottom)
                            start.linkTo(departureField.end)
                            end.linkTo(arrivalField.start)
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_swap),
                        contentDescription = stringResource(R.string.swap_content_description),
                    )
                }

                BusSearchInput(
                    place = arrival,
                    placeholder = stringResource(R.string.select_arrival),
                    modifier = Modifier.padding(top = 10.dp).constrainAs(arrivalField) {
                        top.linkTo(iconSwap.top)
                        bottom.linkTo(iconSwap.bottom)
                        start.linkTo(iconSwap.end)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                        height = Dimension.preferredWrapContent
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            FilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                text = stringResource(R.string.search),
                contentPadding = PaddingValues(vertical = 12.dp),
                onClick = onSearchClicked
            )
        }
    }
}

@Composable
private fun BusSearchInput(
    place: String,
    placeholder: String,
    modifier: Modifier = Modifier,
) {

    val isPlaceDetermined by remember { derivedStateOf { place.isNotEmpty() } }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isPlaceDetermined.not()) KoinTheme.colors.neutral100 else Color.Transparent)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isPlaceDetermined.not())
                Text(
                    text = placeholder,
                    maxLines = 1,
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral400     // TODO neutral450 ?
                )
            else {
                Text(
                    text = place,
                    maxLines = 1,
                    style = KoinTheme.typography.bold18,
                    color = KoinTheme.colors.neutral800
                )
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
            onSwapIconClicked = { }
        )
    }
}