package `in`.koreatech.koin.feature.store.reviewadd

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.reviewadd.component.ReviewHeaderSection
import `in`.koreatech.koin.feature.store.reviewadd.component.ReviewImageSection
import `in`.koreatech.koin.feature.store.reviewadd.component.ReviewTextFieldSection
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ReviewAddScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewAddViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    if (uiState.isLoading) {
        Popup(
            alignment = Alignment.Center
        ) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }
        return
    }

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, onNavigateBack)
    }

    ReviewAddScreen(
        shopName = uiState.storeName,
        rating = uiState.rating,
        reviewContent = uiState.reviewContent,
        menuTag = uiState.menuTag,
        menuTags = uiState.menuTags,
        imageUris = uiState.imageUris,
        modifier = modifier,
        onRatingChange = viewModel::updateRating,
        onReviewContentChange = viewModel::updateReviewContent,
        onAddMenuTag = viewModel::addMenuTag,
        onRemoveMenuTag = viewModel::removeMenuTag,
        onMenuTagChange = viewModel::updateMenuTag,
        onAddReview = viewModel::submitReview,
        onAddImages = viewModel::addImageUris,
        onRemoveImage = viewModel::removeImageUri,
        onNavigationIconClick = onNavigateBack
    )
}

@Composable
private fun ReviewAddScreen(
    shopName: String,
    rating: Int,
    reviewContent: String,
    menuTag: String,
    menuTags: ImmutableList<String>,
    imageUris: ImmutableList<String>,
    modifier: Modifier = Modifier,
    onRatingChange: (Int) -> Unit = { },
    onReviewContentChange: (String) -> Unit = { },
    onAddMenuTag: () -> Unit = { },
    onRemoveMenuTag: (Int) -> Unit = { },
    onMenuTagChange: (String) -> Unit = { },
    onAddImages: (List<String>) -> Unit = {},
    onRemoveImage: (Int) -> Unit = {},
    onAddReview: () -> Unit = { },
    onNavigationIconClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KoinStoreTopAppBar(
                title = stringResource(R.string.review_write),
                onNavigationIconClick = onNavigationIconClick
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 24.dp)
                    .shadow(
                        elevation = 1.dp,
                        shape = RebrandKoinTheme.shapes.small
                    )
                    .background(
                        color = RebrandKoinTheme.colors.primary500,
                        shape = RebrandKoinTheme.shapes.small
                    )
                    .clickable(
                        onClick = onAddReview
                    ),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = stringResource(R.string.review_write_label),
                    modifier = Modifier.padding(vertical = 11.dp),
                    style = RebrandKoinTheme.typography.bold15.copy(color = RebrandKoinTheme.colors.neutral0)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = RebrandKoinTheme.colors.neutral50)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ReviewHeaderSection(
                    storeName = shopName,
                    rating = rating,
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 50.dp),
                    onRatingChange = onRatingChange
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    thickness = 1.dp,
                    color = RebrandKoinTheme.colors.neutral300
                )

                ReviewImageSection(
                    imageUris = imageUris,
                    modifier = Modifier.padding(start = 24.dp, bottom = 24.dp),
                    onAddImages = onAddImages,
                    onRemoveImage = onRemoveImage
                )

                ReviewTextFieldSection(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    content = reviewContent,
                    menuTag = menuTag,
                    menuTags = menuTags,
                    onAddMenuTag = onAddMenuTag,
                    onRemoveMenuTag = onRemoveMenuTag,
                    onContentChange = onReviewContentChange,
                    onMenuTagChange = onMenuTagChange
                )
            }
        }
    }
}

private fun handleSideEffect(
    sideEffect: ReviewAddSideEffect,
    onNavigateBack: () -> Unit
) {
    when (sideEffect) {
        is ReviewAddSideEffect.ShowToast -> {
            ToastUtil.getInstance().makeShort(sideEffect.message)
        }
        is ReviewAddSideEffect.NavigateToReview -> {
            onNavigateBack()
        }
        is ReviewAddSideEffect.ShowImageUploadFailedToast -> {
            ToastUtil.getInstance().makeShort(sideEffect.message)
        }
        is ReviewAddSideEffect.ShowOneReviewPerDayToast -> {
            ToastUtil.getInstance().makeShort(sideEffect.message)
        }
        is ReviewAddSideEffect.ShowRatingValidationToast -> {
            ToastUtil.getInstance().makeShort(sideEffect.message)
        }
        is ReviewAddSideEffect.ShowReviewWriteFailedToast -> {
            ToastUtil.getInstance().makeShort(sideEffect.message)
        }
        is ReviewAddSideEffect.ShowReviewWrittenToast -> {
            ToastUtil.getInstance().makeShort(sideEffect.message)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewAddScreenPreview() {
    ReviewAddScreen()
}
