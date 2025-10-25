package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEachIndexed
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.review.model.LocalReviewRating
import `in`.koreatech.koin.feature.store.review.model.LocalReviewRatings
import kotlin.math.floor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReviewRatingHeader(
    reviewRatings: LocalReviewRatings,
    modifier: Modifier = Modifier
) {
    reviewRatings.apply {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicText(
                    text = "$average",
                    style = KoinTheme.typography.medium18.copy(
                        fontSize = 32.sp,
                        lineHeight = 51.2.sp
                    )
                )

                RatingBar(
                    rating = floor(average).toInt(),
                    iconSize = 16.dp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            ReviewRatingHeaderDetailBar(
                reviewRatingList = reviews,
                totalReviewQuantity = totalReview
            )
        }
    }
}

@Composable
private fun ReviewRatingHeaderDetailBar(
    reviewRatingList: ImmutableList<LocalReviewRating>,
    totalReviewQuantity: Int,
    modifier: Modifier = Modifier,
    ratingBackgroundColor: Color = KoinTheme.colors.neutral300,
    ratingContentColor: Color = KoinTheme.colors.warning500
) {
    Layout(
        modifier = modifier,
        content = {
            (5 downTo 1).forEach { rating ->
                BasicText(
                    modifier = Modifier.layoutId("${RATING_TEXT}$rating"),
                    text = stringResource(R.string.review_rating, rating),
                    style = KoinTheme.typography.regular12
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("${RATING_BAR}$rating")
                ) {
                    drawLine(
                        color = ratingBackgroundColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 7.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    reviewRatingList.fastFirstOrNull { it.rating == rating }?.apply {
                        require(quantity >= 0) { "Rating quantity must be positive" }
                        if (quantity == 0) return@apply
                        drawLine(
                            color = ratingContentColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width / totalReviewQuantity * quantity, size.height),
                            strokeWidth = 7.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }

                reviewRatingList.fastFirstOrNull { it.rating == rating }?.apply {
                    BasicText(
                        modifier = Modifier.layoutId("${RATING_QUANTITY_TEXT}$rating"),
                        text = "$quantity",
                        style = KoinTheme.typography.regular12.merge(color = KoinTheme.colors.neutral500)
                    )
                } ?: throw IllegalArgumentException("$rating-star rating data is not declared")
            }
        }
    ) { measurables, constraints ->
        val ratingText5Placeable = measurables.fastFirst { it.layoutId == "${RATING_TEXT}5" }.measure(constraints)
        val ratingText4Placeable = measurables.fastFirst { it.layoutId == "${RATING_TEXT}4" }.measure(constraints)
        val ratingText3Placeable = measurables.fastFirst { it.layoutId == "${RATING_TEXT}3" }.measure(constraints)
        val ratingText2Placeable = measurables.fastFirst { it.layoutId == "${RATING_TEXT}2" }.measure(constraints)
        val ratingText1Placeable = measurables.fastFirst { it.layoutId == "${RATING_TEXT}1" }.measure(constraints)

        val ratingQuantityText5Placeable = measurables.fastFirst { it.layoutId == "${RATING_QUANTITY_TEXT}5" }.measure(constraints)
        val ratingQuantityText4Placeable = measurables.fastFirst { it.layoutId == "${RATING_QUANTITY_TEXT}4" }.measure(constraints)
        val ratingQuantityText3Placeable = measurables.fastFirst { it.layoutId == "${RATING_QUANTITY_TEXT}3" }.measure(constraints)
        val ratingQuantityText2Placeable = measurables.fastFirst { it.layoutId == "${RATING_QUANTITY_TEXT}2" }.measure(constraints)
        val ratingQuantityText1Placeable = measurables.fastFirst { it.layoutId == "${RATING_QUANTITY_TEXT}1" }.measure(constraints)

        val innerPadding = innerPadding.roundToPx()

        val ratingTextWidth = maxOf(
            ratingText1Placeable.width,
            ratingText2Placeable.width,
            ratingText3Placeable.width,
            ratingText4Placeable.width,
            ratingText5Placeable.width
        )

        val ratingTextHeight = maxOf(
            ratingText1Placeable.height,
            ratingText2Placeable.height,
            ratingText3Placeable.height,
            ratingText4Placeable.height,
            ratingText5Placeable.height
        )

        val ratingQuantityTextWidth = maxOf(
            ratingQuantityText1Placeable.width,
            ratingQuantityText2Placeable.width,
            ratingQuantityText3Placeable.width,
            ratingQuantityText4Placeable.width,
            ratingQuantityText5Placeable.width
        )

        val ratingQuantityTextHeight = maxOf(
            ratingQuantityText1Placeable.height,
            ratingQuantityText2Placeable.height,
            ratingQuantityText3Placeable.height,
            ratingQuantityText4Placeable.height,
            ratingQuantityText5Placeable.height
        )

        val ratingBarMaxConstraints = constraints.copy(
            maxWidth = constraints.maxWidth - ratingTextWidth - ratingQuantityTextWidth - innerPadding * 2
        )

        val ratingBar5Placeable = measurables.fastFirst { it.layoutId == "${RATING_BAR}5" }.measure(ratingBarMaxConstraints)
        val ratingBar4Placeable = measurables.fastFirst { it.layoutId == "${RATING_BAR}4" }.measure(ratingBarMaxConstraints)
        val ratingBar3Placeable = measurables.fastFirst { it.layoutId == "${RATING_BAR}3" }.measure(ratingBarMaxConstraints)
        val ratingBar2Placeable = measurables.fastFirst { it.layoutId == "${RATING_BAR}2" }.measure(ratingBarMaxConstraints)
        val ratingBar1Placeable = measurables.fastFirst { it.layoutId == "${RATING_BAR}1" }.measure(ratingBarMaxConstraints)

        val ratingBarWidth = maxOf(
            ratingBar1Placeable.width,
            ratingBar2Placeable.width,
            ratingBar3Placeable.width,
            ratingBar4Placeable.width,
            ratingBar5Placeable.width
        )

        val ratingBarHeight = maxOf(
            ratingBar1Placeable.height,
            ratingBar2Placeable.height,
            ratingBar3Placeable.height,
            ratingBar4Placeable.height,
            ratingBar5Placeable.height
        )

        val maxRowHeight = maxOf(ratingTextHeight, ratingQuantityTextHeight, ratingBarHeight)

        layout(constraints.maxWidth, maxRowHeight * 5) {
            listOf(
                ratingText5Placeable,
                ratingText4Placeable,
                ratingText3Placeable,
                ratingText2Placeable,
                ratingText1Placeable
            ).fastForEachIndexed { index, placeable ->
                placeable.placeRelative(ratingTextWidth - placeable.width, ratingTextHeight * index)
            }

            listOf(
                ratingBar5Placeable,
                ratingBar4Placeable,
                ratingBar3Placeable,
                ratingBar2Placeable,
                ratingBar1Placeable
            ).fastForEachIndexed { index, placeable ->
                placeable.placeRelative(ratingTextWidth + innerPadding, ratingTextHeight * index + (ratingTextHeight / 2))
            }

            listOf(
                ratingQuantityText5Placeable,
                ratingQuantityText4Placeable,
                ratingQuantityText3Placeable,
                ratingQuantityText2Placeable,
                ratingQuantityText1Placeable
            ).fastForEachIndexed { index, placeable ->
                placeable.placeRelative(ratingTextWidth + innerPadding * 2 + ratingBarWidth, ratingQuantityTextHeight * index)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewRatingHeaderPreview() {
    ReviewRatingHeader(
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
        )
    )
}

private val innerPadding = 12.dp
private const val RATING_TEXT = "RatingText"
private const val RATING_BAR = "RatingBar"
private const val RATING_QUANTITY_TEXT = "RatingQuantityText"
