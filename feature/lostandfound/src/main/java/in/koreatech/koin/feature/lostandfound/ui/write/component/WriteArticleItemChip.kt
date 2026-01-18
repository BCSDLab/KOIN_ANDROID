package `in`.koreatech.koin.feature.lostandfound.ui.write.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType

@Composable
fun WriteArticleItemChip(
    type: LostOrFoundType,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier =
            Modifier
                .background(
                    color = KoinTheme.colors.info200,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${
                    if (type == LostOrFoundType.LOST) {
                        stringResource(R.string.lost_item)
                    } else {
                        stringResource(R.string.found_item)
                    }
                } ${index + 1}",
                color = KoinTheme.colors.primary600,
                style = KoinTheme.typography.medium14,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}
