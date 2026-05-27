package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.home.R

@Composable
fun HomeSection(
    text: @Composable () -> Unit,
    more: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 16.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            text()
            more()
        }
        content()
    }
}

@Preview
@Composable
private fun HomeSectionPreview() {
    Surface {
        HomeSection(
            text = {
                Text(
                    text = "오늘의 식단",
                    style = RebrandKoinTheme.typography.medium18
                )
            },
            more = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "전체보기",
                        style = RebrandKoinTheme.typography.regular13
                    )
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_home_card_arrow),
                        contentDescription = null
                    )
                }
            }
        ) {
            Text("Content")
        }
    }
}
