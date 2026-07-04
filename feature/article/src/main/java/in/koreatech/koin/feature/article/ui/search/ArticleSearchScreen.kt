package `in`.koreatech.koin.feature.article.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.ui.component.ArticleItem

@Composable
fun ArticleSearchScreen(
    onNavigateBack: () -> Unit,
    onArticleClick: (articleId: Int, boardId: Int) -> Unit,
    viewModel: ArticleSearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()
    val mostSearchedKeywords by viewModel.mostSearchedKeywords.collectAsStateWithLifecycle()
    val searchResultUiState by viewModel.searchResultUiState.collectAsStateWithLifecycle(
        initialValue = SearchUiState.Idle
    )

    Scaffold(
        topBar = { ArticleSearchTopBar(onNavigateBack = onNavigateBack) },
        containerColor = RebrandKoinTheme.colors.neutral0
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ArticleSearchField(
                query = query,
                onQueryChanged = viewModel::onSearchInputChanged,
                onSearch = viewModel::search
            )

            when (val state = searchResultUiState) {
                is SearchUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.articlePagination.articles) { article ->
                            ArticleItem(
                                article = article,
                                onClick = { onArticleClick(article.id, article.board.id) }
                            )
                            HorizontalDivider(color = RebrandKoinTheme.colors.neutral100)
                        }
                    }
                }

                is SearchUiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.empty_search_result),
                            style = RebrandKoinTheme.typography.regular14,
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    }
                }

                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = RebrandKoinTheme.colors.primary500)
                    }
                }

                else -> {
                    SearchIdleContent(
                        mostSearchedKeywords = mostSearchedKeywords,
                        searchHistory = searchHistory,
                        onKeywordClick = { keyword ->
                            viewModel.onSearchInputChanged(keyword)
                            viewModel.search()
                        },
                        onHistoryClick = { historyQuery ->
                            viewModel.onSearchInputChanged(historyQuery)
                            viewModel.search()
                        },
                        onHistoryDelete = viewModel::deleteSearchHistory,
                        onClearAll = viewModel::clearSearchHistory
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleSearchTopBar(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onNavigateBack
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.navigation_title_article_search),
            style = RebrandKoinTheme.typography.bold18
        )
    }
}

@Composable
private fun ArticleSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .border(1.dp, RebrandKoinTheme.colors.neutral300, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(RebrandKoinTheme.colors.neutral0)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        textStyle = RebrandKoinTheme.typography.regular14.copy(
            color = RebrandKoinTheme.colors.neutral800
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = RebrandKoinTheme.typography.regular14,
                            color = RebrandKoinTheme.colors.neutral400
                        )
                    }
                    innerTextField()
                }
                Icon(
                    painter = painterResource(R.drawable.ic_article_chip_search),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onSearch
                        ),
                    tint = RebrandKoinTheme.colors.primary500
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchIdleContent(
    mostSearchedKeywords: List<String>,
    searchHistory: List<String>,
    onKeywordClick: (String) -> Unit,
    onHistoryClick: (String) -> Unit,
    onHistoryDelete: (String) -> Unit,
    onClearAll: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (mostSearchedKeywords.isNotEmpty()) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                    text = stringResource(R.string.most_searched_keyword),
                    style = RebrandKoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.neutral700
                )
            }
            item {
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mostSearchedKeywords.forEach { keyword ->
                        TextChip(
                            title = "#$keyword",
                            isSelected = false,
                            onSelect = { onKeywordClick(keyword) },
                            chipColors = TextChipDefaults.chipColors(
                                unselectedContainerColor = RebrandKoinTheme.colors.neutral100,
                                unselectedContentColor = RebrandKoinTheme.colors.neutral500
                            )
                        )
                    }
                }
            }
        }

        if (searchHistory.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.recent_searched_keyword),
                        style = RebrandKoinTheme.typography.bold14,
                        color = RebrandKoinTheme.colors.neutral700
                    )
                    Text(
                        modifier = Modifier.clickable { onClearAll() },
                        text = stringResource(R.string.clear_all),
                        style = RebrandKoinTheme.typography.regular13,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
            items(searchHistory) { historyItem ->
                SearchHistoryItem(
                    query = historyItem,
                    onItemClick = { onHistoryClick(historyItem) },
                    onDeleteClick = { onHistoryDelete(historyItem) }
                )
            }
        }
    }
}

@Composable
private fun SearchHistoryItem(
    query: String,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = query,
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral700
        )
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = RebrandKoinTheme.colors.neutral400
            )
        }
    }
}
