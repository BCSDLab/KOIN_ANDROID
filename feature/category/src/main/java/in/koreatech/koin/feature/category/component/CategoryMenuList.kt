package `in`.koreatech.koin.feature.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.card.FeatureRow
import `in`.koreatech.koin.core.designsystem.component.card.FeatureRowDefaults
import `in`.koreatech.koin.core.designsystem.component.icon.IconBadge
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.category.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryMenuList(
    title: String,
    items: ImmutableList<CategoryMenu>,
    onItemClick: (CategoryMenu) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicText(
            text = title,
            style = RebrandKoinTheme.typography.bold15.copy(color = RebrandKoinTheme.colors.neutral800)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(RebrandKoinTheme.colors.neutral0)
        ) {
            items.forEach { menu ->
                key(menu.id) {
                    CategoryMenuRow(
                        menu = menu,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryMenuRow(
    menu: CategoryMenu,
    onItemClick: (CategoryMenu) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(menu.titleRes)
    FeatureRow(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = { onItemClick(menu) }),
        icon = {
            IconBadge(
                imageVector = ImageVector.vectorResource(menu.iconRes),
                contentDescription = null
            )
        },
        title = {
            BasicText(
                text = title,
                style = RebrandKoinTheme.typography.bold15.copy(color = RebrandKoinTheme.colors.neutral800)
            )
        },
        trailingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_category_arrow_right),
                contentDescription = null,
                tint = RebrandKoinTheme.colors.neutral500
            )
        },
        colors = FeatureRowDefaults.colors(
            backgroundColor = Color.Transparent,
            borderColor = Color.Transparent
        ),
        shape = RectangleShape,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        borderWidth = 0.dp,
        iconSpacing = 16.dp
    )
}

@Preview
@Composable
private fun CategoryMenuListPreview() {
    RebrandKoinTheme {
        CategoryMenuList(
            title = stringResource(R.string.campus),
            items = CAMPUS_MENUS,
            onItemClick = {}
        )
    }
}
