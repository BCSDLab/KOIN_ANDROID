package `in`.koreatech.koin.feature.store.review

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.SortBottomSheet
import `in`.koreatech.koin.feature.store.review.component.ReviewCheckbox
import `in`.koreatech.koin.feature.store.review.component.ReviewRatingHeader
import `in`.koreatech.koin.feature.store.review.model.LocalReviewRating
import `in`.koreatech.koin.feature.store.review.model.LocalReviewRatings
import `in`.koreatech.koin.feature.store.review.model.ReviewOrderOption
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()

    ReviewScreen(
        reviewRatings = uiState.reviewRatings,
        orderOption = uiState.orderOption,
        filterMyReview = uiState.filterMyReview,
        showReviewOrderOptionChooser = viewModel::showReviewOrderOptionChooser,
        hideReviewOrderOptionChooser = viewModel::hideReviewOrderOptionChooser,
        setReviewOrderOption = viewModel::setReviewOrderOption,
        setFilterMyReview = viewModel::setFilterMyReview
    )
}

@Composable
private fun ReviewScreen(
    reviewRatings: LocalReviewRatings,
    orderOption: ReviewState.OrderOption,
    filterMyReview: Boolean,
    modifier: Modifier = Modifier,
    showReviewOrderOptionChooser: () -> Unit = {},
    hideReviewOrderOptionChooser: () -> Unit = {},
    setReviewOrderOption: (ReviewOrderOption) -> Unit = {},
    setFilterMyReview: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        modifier = modifier
    ) {
        KoinStoreTopAppBar(
            title = stringResource(R.string.store_title_review),
            onNavigationIconClick = {
                onBackPressedDispatcher?.onBackPressed()
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                OutlinedButton(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    onClick = {},
                    shape = RebrandKoinTheme.shapes.small,
                    border = BorderStroke(width = 1.dp, color = RebrandKoinTheme.colors.primary500)
                ) {
                    BasicText(
                        text = stringResource(R.string.review_write),
                        style = RebrandKoinTheme.typography.medium14.copy(
                            color = RebrandKoinTheme.colors.primary500
                        )
                    )
                }

                ReviewRatingHeader(
                    modifier = Modifier.padding(top = 12.dp, bottom = 14.dp, start = 24.dp, end = 24.dp),
                    reviewRatings = reviewRatings
                )

                HorizontalDivider(color = RebrandKoinTheme.colors.neutral300, modifier = Modifier.padding(horizontal = 20.dp))

                Row(
                    modifier = Modifier.padding(top = 18.dp, start = 24.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KoinStoreChip(
                        text = stringResource(orderOption.reviewOrderOption.stringResId),
                        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                            textColor = RebrandKoinTheme.colors.neutral500,
                            borderWidth = 0.dp,
                            elevation = 0.dp,
                            containerColor = Color.Unspecified,
                            paddingValues = PaddingValues(horizontal = 4.dp)
                        ),
                        trailingIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.ic_store_arrow_down)),
                        trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
                            iconColor = RebrandKoinTheme.colors.neutral500,
                            iconSize = 20.dp
                        )
                    ) {
                        showReviewOrderOptionChooser()
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ReviewCheckbox(
                        checked = filterMyReview,
                        onCheckedChange = setFilterMyReview
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    BasicText(
                        text = stringResource(R.string.review_mine),
                        style = RebrandKoinTheme.typography.regular14.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                }
            }

            reviewItems(
                reviewRatings.reviews,
                reviewItemCount = reviewRatings.totalReview,
                emptyContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_review_empty),
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        BasicText(
                            text = stringResource(R.string.review_empty_title),
                            style = RebrandKoinTheme.typography.bold18.copy(color = RebrandKoinTheme.colors.primary500)
                        )

                        BasicText(
                            text = stringResource(R.string.review_empty_summary),
                            style = RebrandKoinTheme.typography.medium14.copy(color = RebrandKoinTheme.colors.neutral600)
                        )
                    }
                },
                itemContent = {
                    // TODO
                }
            )
        }
    }

    if (orderOption.showOrderOptionChooser) {
        SortBottomSheet(
            currentIndex = orderOption.reviewOrderOption.ordinal,
            options = ReviewOrderOption.entries.map { context.getString(it.stringResId) }.toImmutableList(),
            onSelect = { index ->
                setReviewOrderOption(ReviewOrderOption.entries[index])
            },
            onClose = {
                hideReviewOrderOptionChooser()
            }
        )
    }
}

private inline fun <T> LazyListScope.reviewItems(
    items: List<T>,
    reviewItemCount: Int,
    crossinline emptyContent: @Composable LazyItemScope.() -> Unit,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = if (reviewItemCount == 0) {
    item {
        emptyContent()
    }
} else {
    items(items) {
        itemContent(it)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenPreview() {
    ReviewScreen(
        modifier = Modifier.padding(horizontal = 24.dp),
        filterMyReview = false,
        reviewRatings = LocalReviewRatings(
            reviews = persistentListOf(
                LocalReviewRating(
                    rating = 5,
                    quantity = 21
                ),
                LocalReviewRating(
                    rating = 4,
                    quantity = 30
                ),
                LocalReviewRating(
                    rating = 3,
                    quantity = 4
                ),
                LocalReviewRating(
                    rating = 2,
                    quantity = 20
                ),
                LocalReviewRating(
                    rating = 1,
                    quantity = 1
                )
            ),
            average = 4.0,
            totalReview = 76
        ),
        orderOption = ReviewState.OrderOption()
    )
}
