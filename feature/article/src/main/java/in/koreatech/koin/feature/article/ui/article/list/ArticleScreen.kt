package `in`.koreatech.koin.feature.article.ui.article.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import `in`.koreatech.koin.feature.article.ui.article.notice.NoticeListScreen
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.LostAndFoundList

@Composable
fun ArticleScreen(
    board: ArticleBoardType,
    modifier: Modifier = Modifier,
    viewModel: ArticleListViewModel = hiltViewModel(),
    navigateToArticleDetail: (articleId: Int, boardId: Int) -> Unit = { _, _ -> },
    navigateToWriteFoundItem: (lostOrFoundType: LostOrFoundType) -> Unit = {},
    navigateToLostAndFoundDetail: (articleId: Int) -> Unit = {},
    navigateToLoginActivity: () -> Unit = {},
    navigateToKeywordSetting: () -> Unit = {}
) {
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    LaunchedEffect(Unit) {
        if (selectedTabIndex == null) {
            viewModel.setSelectedTabIndex(ArticleBoardType.entries.indexOf(board))
        }
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomStart
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex ?: 0,
                containerColor = Color.Transparent,
                edgePadding = 24.dp,
                divider = {}
            ) {
                ArticleBoardType.entries.forEachIndexed { index, board ->
                    // material3 Tab's height is hard coded to 48.dp
                    // and text horizontal padding hard coded to 16.dp
                    // in our design, tab should has 16.dp horizontal padding
                    // and 50.dp height (text height 26.dp + vertical padding 12.dp)
                    // so, don't add padding here
                    Tab(
                        text = {
                            Text(
                                text = stringResource(board.simpleKoreanName),
                                style = KoinTheme.typography.regular16.merge(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    ),
                                    lineHeightStyle = LineHeightStyle(
                                        alignment = LineHeightStyle.Alignment.Center,
                                        trim = LineHeightStyle.Trim.Both
                                    )
                                )
                            )
                        },
                        unselectedContentColor = KoinTheme.colors.neutral500,
                        selected = selectedTabIndex == index,
                        onClick = {
                            viewModel.setSelectedTabIndex(index)
                        }
                    )
                }
            }
            HorizontalDivider(color = KoinTheme.colors.neutral300)
        }

        val currentBoard by remember(key1 = selectedTabIndex) { mutableStateOf(ArticleBoardType.entries[selectedTabIndex ?: 0]) }

        when (currentBoard) {
            ArticleBoardType.ALL,
            ArticleBoardType.NORMAL,
            ArticleBoardType.SCHOLARSHIP,
            ArticleBoardType.SCHOOL,
            ArticleBoardType.RECRUIT,
            ArticleBoardType.IPP,
            ArticleBoardType.STUDENT,
            ArticleBoardType.KOIN -> {
                NoticeListScreen(
                    board = currentBoard,
                    navigateToArticleDetail = navigateToArticleDetail,
                    navigateToKeywordSetting = navigateToKeywordSetting
                )
            }

            ArticleBoardType.LOSTANDFOUND -> {
                LostAndFoundList(
                    navigateToWriteFoundItem = {
                        when (it) {
                            LostOrFoundType.LOST -> navigateToWriteFoundItem(LostOrFoundType.LOST)
                            LostOrFoundType.FOUND -> navigateToWriteFoundItem(LostOrFoundType.FOUND)
                        }
                    },
                    navigateToLostAndFoundDetail = navigateToLostAndFoundDetail,
                    navigateToKeywordFragment = navigateToKeywordSetting,
                    navigateToLoginActivity = navigateToLoginActivity
                )
            }
        }
    }
}
