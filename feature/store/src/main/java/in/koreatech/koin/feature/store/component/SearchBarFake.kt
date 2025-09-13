package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun SearchBarFake(
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.store_search_hint),
    query: String = "",
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RebrandKoinTheme.shapes.medium,
                ambientColor = RebrandKoinTheme.colors.neutral400,
                spotColor = RebrandKoinTheme.colors.neutral500
            )
            .clip(RebrandKoinTheme.shapes.medium)
            .background(RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.medium)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "검색",
                tint = RebrandKoinTheme.colors.neutral500
            )
            Spacer(modifier = Modifier.width(8.dp))

            if (query.isEmpty()) {
                Text(
                    text = hint,
                    style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral400)
                )
            } else {
                Text(
                    text = query,
                    style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral600)
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchBarFakePreview() {
    SearchBarFake(
        query = "족발"
    )
}

@Preview
@Composable
fun SearchBarFakePreview2() {
    SearchBarFake()
}
