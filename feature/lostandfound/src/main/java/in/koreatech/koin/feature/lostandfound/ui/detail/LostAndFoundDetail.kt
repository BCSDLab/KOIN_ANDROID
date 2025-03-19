package `in`.koreatech.koin.feature.lostandfound.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.HotArticle
import `in`.koreatech.koin.feature.lostandfound.component.HotArticleData
import `in`.koreatech.koin.feature.lostandfound.component.LoadingDialog
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailButtonGroup
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailContent
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailHeader
import `in`.koreatech.koin.feature.lostandfound.util.findActivity
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LostAndFoundDetail(
    viewModel: LostAndFoundDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToArticleList: () -> Unit = {},
    navigateToHotArticle: (HotArticleData) -> Unit,
    navigateToChatRoom: (articleId: Int) -> Unit = {},
    navigateToReport: (articleId: Int) -> Unit = {}
) {
    KoinTheme {
        val uiState by viewModel.collectAsState()
        val hotArticle = uiState.hotArticles
        val context = LocalContext.current
        val isLoading = uiState.isLoading

        viewModel.collectSideEffect {
            handleSideEffect(it, context, navigateToArticleList)
        }
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            DetailHeader(
                lostOrFound = uiState.lostOrFound,
                category = uiState.category,
                foundPlace = uiState.foundPlace,
                foundDate = uiState.foundDate,
                author = uiState.author,
                registeredAt = uiState.registeredAt
            )

            HorizontalDivider(thickness = 6.dp, color = KoinTheme.colors.neutral100)

            DetailContent(
                imageUris = uiState.images,
                content = uiState.content,
                isWriterAdmin = uiState.isWriterCouncil
            )

            val loggingLostMessageSend = stringResource(id = R.string.logging_lost_message_send)
            val loggingFoundMessageSend = stringResource(id = R.string.logging_found_message_send)
            val loggingReport = stringResource(id = R.string.logging_report)

            DetailButtonGroup(
                showDeleteButton = uiState.isMine,
                showDeleteDialog = uiState.showDeleteDialog,
                isLoggedIn = uiState.isLoggedIn,
                onArticleListClick = {
                    navigateToArticleList()
                },
                onDeleteArticleClick = {
                    viewModel.deleteArticle()
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

            HotArticle(
                hotArticleList = hotArticle,
                navigateToHotArticle = navigateToHotArticle
            )

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
        // is LostAndFoundDetailSideEffect.FetchDetail -> {}
        // LostAndFoundDetailSideEffect.FetchHotArticles -> {}
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
