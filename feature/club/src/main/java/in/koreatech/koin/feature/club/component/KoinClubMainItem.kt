package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun KoinClubMainItem(
    title: String,
    description: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = KoinTheme.colors.neutral50)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = icon,
            contentDescription = title
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column {
            Text(
                text = title,
                style = KoinTheme.typography.medium14
            )
            Text(
                text = description,
                style = KoinTheme.typography.regular12
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = title
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewKoinClubMainItem() {
    KoinTheme {
        KoinClubMainItem(
            title = "동아리 목록",
            description = "바로가기",
            icon = painterResource(R.drawable.ic_club_list)
        )
    }
}
