package `in`.koreatech.koin.feature.article.ui.write.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType

@Composable
fun WriteArticleItemChip(
    type: LostOrFoundType,
    index: Int,
    modifier: Modifier = Modifier,
    shouldShowDelete: Boolean = false,
    onDeleteItemClick: () -> Unit = {}
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
        if (shouldShowDelete) {
            Image(
                modifier =
                Modifier
                    .width(36.dp)
                    .height(28.dp)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .clickable {
                        onDeleteItemClick()
                    },
                painter = painterResource(id = R.drawable.ic_item_delete),
                contentDescription = null
            )
        }
    }
}
