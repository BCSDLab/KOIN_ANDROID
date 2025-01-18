package `in`.koreatech.koin.feature.lostandfound.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.HotArticle
import `in`.koreatech.koin.feature.lostandfound.component.HotArticleData
import `in`.koreatech.koin.feature.lostandfound.component.LoadingDialog
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailButtonGroup
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailContent
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.DetailHeader
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LostAndFoundDetail(
    articleId: Int,
    modifier: Modifier = Modifier,
    navigateToArticleList: () -> Unit = {},
    navigateToHotArticle: (HotArticleData) -> Unit
) {
    val viewModelStoreOwner = requireNotNull(
        LocalViewModelStoreOwner.current as? HasDefaultViewModelProviderFactory
    )
    val viewModel: LostAndFoundDetailViewModel = viewModel(
        extras = viewModelStoreOwner.defaultViewModelCreationExtras.withCreationCallback<LostAndFoundDetailViewModel.Factory> {
            it.create(articleId = articleId)
        }
    )
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

            DetailButtonGroup(
                showDeleteButton = uiState.canDelete,
                showDeleteDialog = uiState.showDeleteDialog,
                onArticleListClick = {
                    navigateToArticleList()
                },
                onDeleteArticleClick = {
                    viewModel.deleteArticle()
                },
                onShowDeleteDialogChange = {
                    viewModel.setShowDeleteDialog(it)
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
    navigateToArticleList: () -> Unit = {},
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
    }
}


