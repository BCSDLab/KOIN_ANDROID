package `in`.koreatech.koin.feature.lostandfound.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LoadingDialog
import `in`.koreatech.koin.feature.lostandfound.component.RecentArticleList
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailButtonGroup
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailContent
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailDialog
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailHeader
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.LostAndFoundDetailCustomSwitch
import `in`.koreatech.koin.feature.lostandfound.util.findActivity
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LostAndFoundDetail(
    viewModel: LostAndFoundDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToArticleList: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {},
    navigateToRecentArticle: (articleId: Int) -> Unit = {},
    navigateToChatRoom: (articleId: Int) -> Unit = {},
    navigateToReport: (articleId: Int) -> Unit = {}
) {
    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.lost_and_found),
                onNavigationIconClick = onTopbarBackClick
            )
        }
    ) { contentPadding ->
        val uiState by viewModel.collectAsState()
        val recentArticles = uiState.recentArticles
        val context = LocalContext.current
        val isLoading = uiState.isLoading

        var isFound by remember { mutableStateOf(uiState.isFound) }

        if (uiState.showFoundDialog) {
            DetailDialog(
                title = stringResource(id = R.string.lost_and_found_dialog_message),
                onPositive = {
                    isFound = true
                    viewModel.setShowFoundDialog(false)
                },
                onNegative = {
                    viewModel.setShowFoundDialog(false)
                },
                titleStyle = KoinTheme.typography.medium16.copy(color = KoinTheme.colors.neutral600)
            )
        }

        viewModel.collectSideEffect {
            handleSideEffect(it, context, navigateToArticleList)
        }
        Column(
            modifier = modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            DetailHeader(
                lostOrFound = uiState.lostOrFound,
                category = uiState.category,
                foundPlace = uiState.foundPlace,
                foundDate = uiState.foundDate,
                author = uiState.author,
                isFound = isFound
            )

            HorizontalDivider(thickness = 6.dp, color = KoinTheme.colors.neutral100)

            DetailContent(
                imageUris = uiState.images,
                content = uiState.content,
                isWriterAdmin = uiState.isWriterCouncil
            )

            if (uiState.isMine) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = if (uiState.lostOrFound == LostOrFoundType.LOST) stringResource(id = R.string.lost_and_found_lost_message) else stringResource(id = R.string.lost_and_found_found_message),
                        style = KoinTheme.typography.regular12.copy(color = KoinTheme.colors.neutral500)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LostAndFoundDetailCustomSwitch(
                        checked = isFound,
                        onCheckedChange = { viewModel.setShowFoundDialog(true) },
                        enabled = !isFound
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            val loggingLostMessageSend = stringResource(id = R.string.logging_lost_message_send)
            val loggingFoundMessageSend = stringResource(id = R.string.logging_found_message_send)
            val loggingReport = stringResource(id = R.string.logging_report)

            DetailButtonGroup(
                showDeleteButton = uiState.isMine,
                showDeleteDialog = uiState.showDeleteDialog,
                isLoggedIn = uiState.isLoggedIn,
                isAuthorWithdraw = uiState.isAuthorWithdraw,
                onArticleListClick = {
                    navigateToArticleList()
                },
                onDeleteArticleClick = {
                    viewModel.deleteArticle()
                },
                onEditArticleClick = {
                },
                onShowDeleteDialogChange = {
                    viewModel.setShowDeleteDialog(it)
                },
                onChatRoomClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.LostAndFound.ITEM_MESSAGE_SEND,
                        if (uiState.lostOrFound == LostOrFoundType.LOST) {
                            loggingLostMessageSend
                        } else {
                            loggingFoundMessageSend
                        }
                    )
                    navigateToChatRoom(uiState.id)
                },
                onReportArticleClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.LostAndFound.ITEM_POST_REPORT,
                        loggingReport
                    )
                    navigateToReport(uiState.id)
                }
            )

            HorizontalDivider(thickness = 6.dp, color = KoinTheme.colors.neutral100)

            RecentArticleList(
                recentArticles = recentArticles,
                isLoadingMore = uiState.isLoadingMoreArticles,
                hasMoreArticles = uiState.hasMoreArticles,
                onLoadMore = { viewModel.loadMoreRecentArticles() },
                onArticleClick = { article ->
                    navigateToRecentArticle(article.id)
                }
            )

            Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))

            if (isLoading) {
                LoadingDialog()
            }
        }
    }
}

private fun handleSideEffect(
    sideEffect: LostAndFoundDetailSideEffect,
    context: Context,
    navigateToArticleList: () -> Unit = {}
) {
    when (sideEffect) {
        is LostAndFoundDetailSideEffect.DeleteArticle -> {
            Toast.makeText(
                context,
                context.getString(R.string.detail_delete_toast),
                Toast.LENGTH_SHORT
            ).show()
            navigateToArticleList()
        }

        LostAndFoundDetailSideEffect.DeleteArticleFailed -> {
            Toast.makeText(
                context,
                context.getString(R.string.detail_delete_failed_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        LostAndFoundDetailSideEffect.DeletedArticle -> {
            context.findActivity()?.finish()
            val intent =
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("koin://article/activity?fragment=article_lost_and_found")
                }
            context.startActivity(intent)
            Toast.makeText(
                context,
                context.getString(R.string.detail_deleted_article),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
