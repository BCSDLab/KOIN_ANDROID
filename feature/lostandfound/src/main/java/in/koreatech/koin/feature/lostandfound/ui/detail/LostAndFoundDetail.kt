package `in`.koreatech.koin.feature.lostandfound.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LoadingDialog
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailButtonGroup
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailContent
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailDialog
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailFoundSwitch
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailHeader
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.RecentArticleList
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
    navigateToLogin: () -> Unit = {},
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

        if (uiState.showFoundDialog) {
            DetailDialog(
                title = stringResource(id = R.string.lost_and_found_dialog_message),
                onPositive = {
                    viewModel.setFound()
                    viewModel.setShowFoundDialog(false)
                },
                onNegative = {
                    viewModel.setShowFoundDialog(false)
                },
                titleStyle = KoinTheme.typography.medium16.copy(color = KoinTheme.colors.neutral600)
            )
        }

        if (uiState.showLoginDialog) {
            ChoiceDialog(
                title = stringResource(id = R.string.detail_chat_login_dialog_title),
                description = stringResource(id = R.string.detail_chat_login_dialog_description),
                positiveButtonText = stringResource(id = R.string.detail_chat_login_dialog_positive),
                negativeButtonText = stringResource(id = R.string.detail_chat_login_dialog_negative),
                onPositive = {
                    navigateToLogin()
                    viewModel.setShowLoginDialog(false)
                },
                onNegative = {
                    viewModel.setShowLoginDialog(false)
                },
                titleStyle = KoinTheme.typography.medium18.copy(color = KoinTheme.colors.neutral600),
                descriptionStyle = KoinTheme.typography.regular14.copy(color = Color(0xFF8E8E8E))
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
            val configuration = LocalConfiguration.current
            val screenHeightDp = remember { configuration.screenHeightDp.dp }
            val layoutHeightDp = remember { mutableStateOf(0.dp) }
            val enableRecentArticleHeight = remember(layoutHeightDp.value) {
                mutableStateOf(screenHeightDp - (contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding()) - layoutHeightDp.value)
            }
            Layout(
                content = {
                    Column {
                        DetailHeader(
                            lostOrFound = uiState.lostOrFound,
                            category = uiState.category,
                            foundPlace = uiState.foundPlace,
                            foundDate = uiState.foundDate,
                            author = uiState.author,
                            isFound = uiState.isFound
                        )

                        HorizontalDivider(thickness = 6.dp, color = KoinTheme.colors.neutral100)

                        DetailContent(
                            imageUris = uiState.images,
                            content = uiState.content,
                            isWriterAdmin = uiState.isWriterCouncil
                        )

                        if (uiState.isMine && !uiState.isFound) {
                            DetailFoundSwitch(
                                lostOrFoundType = uiState.lostOrFound,
                                isFound = uiState.isFound,
                                onCheckedChange = { viewModel.setShowFoundDialog(true) }
                            )
                        }

                        val loggingLostMessageSend = stringResource(id = R.string.logging_lost_message_send)
                        val loggingFoundMessageSend = stringResource(id = R.string.logging_found_message_send)
                        val loggingReport = stringResource(id = R.string.logging_report)

                        DetailButtonGroup(
                            showDeleteButton = uiState.isMine,
                            showDeleteDialog = uiState.showDeleteDialog,
                            isLoggedIn = uiState.isLoggedIn,
                            isAuthorWithdraw = uiState.isAuthorWithdraw,
                            isWriterAdmin = uiState.isWriterCouncil,
                            onArticleListClick = {
                                navigateToArticleList()
                            },
                            onDeleteArticleClick = {
                                viewModel.deleteArticle()
                            },
                            onEditArticleClick = {
                                //TODO wait new api
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
                                if(uiState.isLoggedIn) {
                                    navigateToChatRoom(uiState.id)
                                }
                                else {
                                    viewModel.setShowLoginDialog(true)
                                }
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
                    }
                }
            ) { measurables, constraints ->
                val placeable = measurables.first().measure(constraints)
                layoutHeightDp.value = placeable.height.toDp()
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }

            RecentArticleList(
                modifier = Modifier
                    .heightIn(min = 300.dp, max = screenHeightDp)
                    .height(enableRecentArticleHeight.value),
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

        LostAndFoundDetailSideEffect.UpdateFoundFail -> {
            Toast.makeText(
                context,
                context.getString(R.string.lost_and_found_update_found_fail),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
