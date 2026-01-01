package `in`.koreatech.koin.feature.store.reviewadd.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.review.component.RatingBar

@Composable
fun ReviewHeaderSection(
    storeName: String,
    rating: Int,
    modifier: Modifier = Modifier,
    onRatingChange: (Int) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        BasicText(
            text = storeName,
            style = RebrandKoinTheme.typography.bold20
        )

        Spacer(modifier = Modifier.height(5.dp))

        BasicText(
            text = stringResource(R.string.review_submit_notice),
            style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral500)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(
                rating = rating,
                onRatingChanged = onRatingChange,
                minRating = 1
            )

            BasicText(
                text = rating.toString(),
                style = RebrandKoinTheme.typography.medium16
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewHeaderSectionPreview() {
    ReviewHeaderSection(
        storeName = "가장 맛있는 족발",
        rating = 1
    )
}
