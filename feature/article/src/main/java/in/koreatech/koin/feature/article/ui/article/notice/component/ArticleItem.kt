package `in`.koreatech.koin.feature.article.ui.article.notice.component

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType

@Composable
fun ArticleItem(
    boardType: ArticleBoardType,
    title: String,
    author: String,
    registeredAt: String,
    viewCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.noRippleClickable {
            onClick()
        }
    ) {
        val convertedRegisteredAt = remember(key1 = registeredAt) { "${TextUtils.concat(DateFormatUtil.getSimpleMonthAndDay(registeredAt), " ", DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(registeredAt)))}" }

        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(boardType.koreanName),
                color = KoinTheme.colors.primary600,
                style = KoinTheme.typography.medium12.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = KoinTheme.typography.medium14
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$author • $convertedRegisteredAt • ",
                    color = KoinTheme.colors.neutral500,
                    style = KoinTheme.typography.regular12
                )
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_view),
                    contentDescription = null
                )
                Text(
                    text = "$viewCount",
                    color = KoinTheme.colors.neutral500,
                    style = KoinTheme.typography.regular12
                )
            }
        }
        HorizontalDivider(color = KoinTheme.colors.neutral100)
    }
}
