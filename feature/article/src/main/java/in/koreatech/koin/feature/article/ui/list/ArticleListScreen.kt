package `in`.koreatech.koin.feature.article.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.ui.component.ArticleItem

@Composable
fun ArticleListScreen(
    onArticleClick: (articleId: Int, boardId: Int) -> Unit,
    onSearchClick: () -> Unit,
    onKeywordManageClick: () -> Unit,
    onOldVersionClick: () -> Unit = {},
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val currentBoard by viewModel.currentBoard.collectAsStateWithLifecycle()
    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()
    val pageNumbers by viewModel.pageNumbers.collectAsStateWithLifecycle()
    val myKeywords by viewModel.myKeywords.collectAsStateWithLifecycle()
    val selectedKeyword by viewModel.selectedKeyword.collectAsStateWithLifecycle()
    val articlePagination by viewModel.articlePagination.collectAsStateWithLifecycle()

    val exposedBoards = remember { ArticleBoardType.entries.filter { it.exposedInAll } }

    Scaffold(
        topBar = { ArticleListTopBar() },
        containerColor = RebrandKoinTheme.colors.neutral0,
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ArticleBoardTabs(
                boards = exposedBoards,
                selectedBoard = currentBoard,
                onBoardSelected = viewModel::setCurrentBoard
            )

            ArticleKeywordsRow(
                keywords = myKeywords,
                selectedKeyword = selectedKeyword,
                onKeywordSelected = viewModel::selectKeyword,
                onKeywordManageClick = onKeywordManageClick,
                onSearchClick = onSearchClick
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(articlePagination.articles) { article ->
                    ArticleItem(
                        article = article,
                        onClick = { onArticleClick(article.id, article.board.id) }
                    )
                    HorizontalDivider(color = RebrandKoinTheme.colors.neutral100)
                }
                if (articlePagination.articles.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.empty_articles),
                                style = RebrandKoinTheme.typography.regular14,
                                color = RebrandKoinTheme.colors.neutral500
                            )
                        }
                    }
                }
                item {
                    ArticlePagination(
                        pageNumbers = pageNumbers,
                        currentPage = currentPage,
                        totalPage = articlePagination.totalPage,
                        onPageSelected = viewModel::setCurrentPage
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleListTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bcsd_logo_symbol),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            painter = painterResource(R.drawable.ic_koin_text),
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.ic_koin_notification),
            contentDescription = null
        )
    }
}

@Composable
private fun ArticleBoardTabs(
    boards: List<ArticleBoardType>,
    selectedBoard: ArticleBoardType,
    onBoardSelected: (ArticleBoardType) -> Unit
) {
    val selectedIndex = boards.indexOf(selectedBoard).coerceAtLeast(0)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = RebrandKoinTheme.colors.neutral0,
        contentColor = RebrandKoinTheme.colors.primary600,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 2.dp,
                    color = RebrandKoinTheme.colors.primary800
                )
            }
        },
        divider = { HorizontalDivider(color = RebrandKoinTheme.colors.neutral300) }
    ) {
        boards.forEachIndexed { index, board ->
            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints.copy(minWidth = 0))
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBoardSelected(board) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = stringResource(board.simpleKoreanName),
                    style = if (selectedIndex == index) {
                        RebrandKoinTheme.typography.bold14
                    } else {
                        RebrandKoinTheme.typography.regular14
                    },
                    color = if (selectedIndex == index) {
                        RebrandKoinTheme.colors.primary600
                    } else {
                        RebrandKoinTheme.colors.neutral500
                    }
                )
            }
        }
    }
}

@Composable
private fun ArticleKeywordsRow(
    keywords: List<String>,
    selectedKeyword: String,
    onKeywordSelected: (String) -> Unit,
    onKeywordManageClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val filterChipColors = TextChipDefaults.chipColors(
        selectedContainerColor = RebrandKoinTheme.colors.primary600,
        selectedContentColor = RebrandKoinTheme.colors.neutral0,
        unselectedContainerColor = Color.Transparent,
        unselectedContentColor = RebrandKoinTheme.colors.neutral600
    )
    val keywordChipColors = TextChipDefaults.chipColors(
        selectedContainerColor = RebrandKoinTheme.colors.primary600,
        selectedContentColor = RebrandKoinTheme.colors.neutral0,
        unselectedContainerColor = RebrandKoinTheme.colors.neutral100,
        unselectedContentColor = RebrandKoinTheme.colors.neutral600
    )
    val chipPadding = PaddingValues(vertical = 6.dp, horizontal = 16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(RebrandKoinTheme.colors.neutral100)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onSearchClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_article_chip_search),
                contentDescription = stringResource(R.string.search),
                modifier = Modifier.size(16.dp),
                tint = RebrandKoinTheme.colors.neutral500
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(RebrandKoinTheme.colors.neutral100)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onKeywordManageClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_go_to_keyword),
                contentDescription = stringResource(R.string.add_new_keyword),
                modifier = Modifier.size(16.dp, 14.dp),
                tint = RebrandKoinTheme.colors.neutral500
            )
        }
        ProvideTextStyle(RebrandKoinTheme.typography.regular14) {
            TextChip(
                title = stringResource(R.string.keyword_see_all),
                isSelected = selectedKeyword.isEmpty(),
                onSelect = { onKeywordSelected("") },
                chipColors = filterChipColors,
                contentPadding = chipPadding
            )
            if (keywords.isEmpty()) {
                TextChip(
                    title = stringResource(R.string.add_new_keyword),
                    isSelected = false,
                    onSelect = { onKeywordManageClick() },
                    chipColors = keywordChipColors,
                    contentPadding = chipPadding
                )
            } else {
                keywords.forEach { keyword ->
                    TextChip(
                        title = "#$keyword",
                        isSelected = selectedKeyword == keyword,
                        onSelect = { onKeywordSelected(keyword) },
                        chipColors = keywordChipColors,
                        contentPadding = chipPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticlePagination(
    pageNumbers: IntArray,
    currentPage: Int,
    totalPage: Int,
    onPageSelected: (Int) -> Unit
) {
    val validPages = pageNumbers.filter { it > 0 }
    if (validPages.isEmpty()) return

    val currentGroupStart = ((currentPage - 1) / 5) * 5 + 1
    val hasPrev = currentGroupStart > 1
    val hasNext = currentGroupStart + 5 <= totalPage

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasPrev) {
            Text(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onPageSelected(currentGroupStart - 1) }
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                text = stringResource(R.string.pagination_prev),
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral700
            )
        }
        validPages.forEach { pageNum ->
            val isSelected = pageNum == currentPage
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) {
                            RebrandKoinTheme.colors.primary600
                        } else {
                            RebrandKoinTheme.colors.neutral200
                        }
                    )
                    .clickable { onPageSelected(pageNum) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$pageNum",
                    style = RebrandKoinTheme.typography.regular14,
                    color = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral600
                )
            }
        }
        if (hasNext) {
            Text(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onPageSelected(currentGroupStart + 5) }
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                text = stringResource(R.string.pagination_next),
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral700
            )
        }
    }
}
