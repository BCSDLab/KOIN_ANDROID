package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinStoreCategoryItem(
    categoryName: String,
    categoryIcon: Painter,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.noRippleClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(48.dp)
                .background(if (isSelected) RebrandKoinTheme.colors.neutral300 else Color.Unspecified, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp),
                painter = categoryIcon,
                contentDescription = categoryName
            )
        }

        BasicText(
            text = categoryName,
            style = RebrandKoinTheme.typography.regular12
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinStoreCategoryItemPreview() {
    Row {
        KoinStoreCategoryItem(
            categoryName = "한식",
            categoryIcon = painterResource(id = R.drawable.ic_bcsd_symbol)
        )

        KoinStoreCategoryItem(
            categoryName = "한식",
            categoryIcon = painterResource(id = R.drawable.ic_bcsd_symbol),
            isSelected = true
        )
    }
}
