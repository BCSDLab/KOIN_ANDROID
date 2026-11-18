package `in`.koreatech.koin.feature.land.ui.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.components.LandRow
import `in`.koreatech.koin.feature.land.components.LandStatus
import `in`.koreatech.koin.feature.land.model.LandUiModel
import `in`.koreatech.koin.feature.land.navigation.LandSideEffect
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LandSearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (LandUiModel) -> Unit,
    viewModel: LandSearchViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val query = remember(state.query) { state.query.trim() }
    val visibleLands: ImmutableList<LandUiModel> = remember(state.allLands, query) {
        if (query.isEmpty()) {
            state.allLands
        } else {
            state.allLands.filterTo(persistentListOf<LandUiModel>().builder()) {
                it.name.contains(query, ignoreCase = true)
            }.build()
        }
    }
    val listState = rememberLazyListState()
    LaunchedEffect(query) {
        listState.scrollToItem(0)
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val onSubmit = {
        keyboardController?.hide()
        focusManager.clearFocus()
    }
    BackHandler(onBack = viewModel::onBackClick)
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            LandSideEffect.NavigateBack -> onNavigateBack()
            is LandSideEffect.NavigateToDetail -> onNavigateToDetail(sideEffect.land)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = RebrandKoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar2(
                title = { Text(text = stringResource(R.string.land_title)) },
                onNavigationIconClick = viewModel::onBackClick
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LandSearchInput(
                query = state.query,
                onQueryChange = viewModel::onQueryChange,
                onSubmit = onSubmit,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)
            )
            Text(
                text = stringResource(R.string.land_count, visibleLands.size),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral600,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                when {
                    state.isLoading -> item {
                        LandStatus(
                            messageRes = R.string.land_loading,
                            isLoading = true,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                    state.hasLoadError -> item {
                        LandStatus(
                            messageRes = R.string.land_load_failed,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                    visibleLands.isEmpty() -> item {
                        LandStatus(
                            messageRes = if (query.isEmpty()) R.string.land_empty else R.string.land_search_empty,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                    else -> items(visibleLands, key = { it.id }) { land ->
                        LandRow(
                            land = land,
                            modifier = Modifier.clickable { viewModel.onLandClick(land.id) },
                            highlightQuery = query
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LandSearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hint = stringResource(R.string.land_search_hint)
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral800),
        cursorBrush = SolidColor(RebrandKoinTheme.colors.primary500),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(1.dp, RebrandKoinTheme.colors.neutral300, RoundedCornerShape(50))
            .semantics { contentDescription = hint },
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = hint,
                            style = RebrandKoinTheme.typography.regular14,
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    }
                    innerTextField()
                }
                IconButton(
                    onClick = onSubmit,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_land_search),
                        contentDescription = stringResource(R.string.land_search),
                        tint = RebrandKoinTheme.colors.primary500,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}
