package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun KoinClubCategoryItem(
    categoryName: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(100)).clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(),
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .background(KoinTheme.colors.neutral100)
        ) {
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp),
                painter = icon,
                contentDescription = categoryName
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            style = KoinTheme.typography.regular14,
            text = categoryName
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKoinClubCategoryItem() {
    KoinClubCategoryItem(
        categoryName = "운동",
        icon = painterResource(id = R.drawable.ic_club_category_sports),
        modifier = Modifier
    )
}
