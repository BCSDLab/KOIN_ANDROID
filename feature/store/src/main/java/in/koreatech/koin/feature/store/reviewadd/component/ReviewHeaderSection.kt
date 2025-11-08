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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
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
            text = "리뷰를 남겨주시면 사장님과 다른 분들에게 도움이 됩니다.\n또한, 악의적인 리뷰는 관리자에 의해 삭제될 수 있습니다.",
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
                minRating = 0
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
