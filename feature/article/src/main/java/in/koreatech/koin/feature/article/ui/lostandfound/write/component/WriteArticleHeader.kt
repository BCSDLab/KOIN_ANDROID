package `in`.koreatech.koin.feature.article.ui.lostandfound.write.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType

@Composable
fun WriteArticleHeader(
    type: LostOrFoundType,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 12.dp, horizontal = 24.dp)) {
        Row {
            Text(
                style = KoinTheme.typography.regular18,
                text = when (type) {
                    LostOrFoundType.LOST -> stringResource(R.string.header_lost_title)
                    LostOrFoundType.FOUND -> stringResource(R.string.header_found_title)
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Image(
                modifier = Modifier.size(24.dp),
                painter = when (type) {
                    LostOrFoundType.LOST -> painterResource(R.drawable.ic_lost)
                    LostOrFoundType.FOUND -> painterResource(R.drawable.ic_found)
                },
                contentDescription = null
            )
        }
        Text(
            color = KoinTheme.colors.neutral500,
            style = KoinTheme.typography.regular12,
            text = when (type) {
                LostOrFoundType.LOST -> stringResource(R.string.header_lost_description)
                LostOrFoundType.FOUND -> stringResource(R.string.header_found_description)
            }
        )
    }
}
