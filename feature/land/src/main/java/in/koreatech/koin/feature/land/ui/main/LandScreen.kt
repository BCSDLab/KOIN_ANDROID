package `in`.koreatech.koin.feature.land.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.components.map.BindLandMap
import `in`.koreatech.koin.feature.land.components.map.rememberLandMapView
import `in`.koreatech.koin.feature.land.model.LandUiModel
import `in`.koreatech.koin.feature.land.navigation.LandSideEffect
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LandScreen(
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit,
    onNavigateToDetail: (LandUiModel) -> Unit,
    viewModel: LandViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val visibleLands: ImmutableList<LandUiModel> = remember(state.lands, state.filter) {
        if (state.filter == LandFilter.ALL) {
            state.lands
        } else {
            state.lands.filterTo(persistentListOf<LandUiModel>().builder(), state.filter::matches).build()
        }
    }
    val selectedLand = remember(state.lands, state.selectedLandId) {
        state.selectedLandId?.let { id -> state.lands.firstOrNull { it.id == id } }
    }
    var selectedSheetHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    var mapBounds by remember { mutableStateOf(Rect.Zero) }
    var sheetBounds by remember { mutableStateOf(Rect.Zero) }
    val bottomPaddingPx = if (mapBounds.overlaps(sheetBounds)) {
        mapBounds.intersect(sheetBounds).height.roundToInt()
    } else {
        0
    }
    val sheetMaxHeight = with(density) {
        if (mapBounds.height > 0) mapBounds.height.toDp() else Dp.Unspecified
    }
    val sheetPeekHeight = if (selectedLand == null) {
        with(density) { LocalWindowInfo.current.containerSize.height.toDp() / 2 }
    } else {
        with(density) { selectedSheetHeightPx.toDp() }
    }
    BackHandler(enabled = selectedLand != null) {
        viewModel.onBackClick()
    }
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            LandSideEffect.NavigateBack -> onNavigateBack()
            is LandSideEffect.NavigateToDetail -> onNavigateToDetail(sideEffect.land)
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = RebrandKoinTheme.colors.neutral0,
        sheetContainerColor = RebrandKoinTheme.colors.neutral0,
        sheetPeekHeight = sheetPeekHeight,
        sheetMaxWidth = Dp.Unspecified,
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        sheetContent = {
            if (selectedLand == null) {
                LandBottomSheet(
                    lands = visibleLands,
                    filter = state.filter,
                    isLoading = state.isLoading,
                    hasLoadError = state.hasLoadError,
                    onFilterClick = viewModel::selectFilter,
                    onLandClick = viewModel::onDetailClick,
                    modifier = Modifier
                        .heightIn(max = sheetMaxHeight)
                        .fillMaxHeight()
                        .onGloballyPositioned { sheetBounds = it.boundsInWindow() }
                        .navigationBarsPadding()
                )
            } else {
                LandSelectedBottomSheet(
                    land = selectedLand,
                    onDismiss = { viewModel.selectLand(null) },
                    onDetailClick = { viewModel.onDetailClick(selectedLand.id) },
                    modifier = Modifier
                        .heightIn(max = sheetMaxHeight)
                        .wrapContentHeight(align = Alignment.Top)
                        .onSizeChanged { selectedSheetHeightPx = it.height }
                        .onGloballyPositioned { sheetBounds = it.boundsInWindow() }
                        .navigationBarsPadding()
                )
            }
        },
        sheetDragHandle = null,
        topBar = {
            KoinTopAppBar2(
                title = { Text(text = stringResource(R.string.land_title)) },
                onNavigationIconClick = viewModel::onBackClick,
                actions = {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_land_search),
                            contentDescription = stringResource(R.string.land_search),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) {
        LandScreenImpl(
            lands = visibleLands,
            selectedLandId = state.selectedLandId,
            onLandSelect = viewModel::selectLand,
            bottomPaddingPx = bottomPaddingPx,
            modifier = Modifier.onGloballyPositioned { mapBounds = it.boundsInWindow() }
        )
    }
}

@Composable
private fun LandScreenImpl(
    lands: ImmutableList<LandUiModel>,
    selectedLandId: Int?,
    onLandSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    bottomPaddingPx: Int = 0
) {
    if (!LocalInspectionMode.current) {
        val mapView = rememberLandMapView()
        BindLandMap(
            mapView = mapView,
            lands = lands,
            bottomPaddingPx = bottomPaddingPx,
            selectedLandId = selectedLandId,
            onLandSelect = onLandSelect
        )
        AndroidView(
            factory = { mapView },
            modifier = modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LandScreenPreview() {
    RebrandKoinTheme {
        LandScreenImpl(
            lands = persistentListOf(),
            selectedLandId = null,
            onLandSelect = {}
        )
    }
}
