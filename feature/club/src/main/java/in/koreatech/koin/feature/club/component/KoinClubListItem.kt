package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun KoinClubListItem(
    id: Int,
    name: String,
    category: String,
    likes: Int,
    logoUrl: String,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier
            .border(width = 1.dp, color = KoinTheme.colors.primary300, shape = KoinTheme.shapes.small)
            .clip(KoinTheme.shapes.small)
            .clickable { onClick(id) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = name,
                style = KoinTheme.typography.bold20
            )

            Text(
                text = category,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral600
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Image(
                    painter = painterResource(R.drawable.ic_club_like),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$likes",
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral0
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        SubcomposeAsyncImage(
            model = logoUrl,
            contentDescription = null,
            modifier = modifier.size(100.dp),
            loading = {
                CircularProgressIndicator()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KoinClubListItemPreview() {
    KoinTheme {
        Box {
            KoinClubListItem(
                id = 1,
                name = "BCSD",
                category = "학술",
                likes = 100,
                logoUrl = "https://example.com/logo.png"
            )
        }
    }
}
