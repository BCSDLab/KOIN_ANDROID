package `in`.koreatech.koin.feature.article.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.model.ArticleHeaderState

@Composable
fun ArticleItem(
    article: ArticleHeaderState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(article.board.simpleKoreanName),
            style = RebrandKoinTheme.typography.bold12,
            color = RebrandKoinTheme.colors.primary800
        )
        Text(
            text = article.title,
            style = RebrandKoinTheme.typography.medium14,
            color = RebrandKoinTheme.colors.neutral800,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = article.author,
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
            Text(
                text = stringResource(R.string.divider_dot),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral400
            )
            Text(
                text = remember(article.registeredAt) {
                    runCatching {
                        val monthDay = DateFormatUtil.getSimpleMonthAndDay(article.registeredAt)
                        val dayOfWeek = DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(article.registeredAt))
                        "$monthDay $dayOfWeek"
                    }.getOrDefault(article.registeredAt)
                },
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
            Text(
                text = stringResource(R.string.divider_dot),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral400
            )
            Icon(
                painter = painterResource(R.drawable.ic_view),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.Unspecified
            )
            Text(
                text = "${article.viewCount}",
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
    }
}
