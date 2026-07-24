package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.card.FeatureRow
import `in`.koreatech.koin.core.designsystem.component.card.FeatureRowDefaults
import `in`.koreatech.koin.core.designsystem.component.icon.IconBadge
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.R
import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DepartmentCategoryList(
    categories: ImmutableList<DepartmentCategory>,
    onCategoryClick: (DepartmentCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(RebrandKoinTheme.colors.neutral0)
    ) {
        categories.forEach { category ->
            key(category) {
                DepartmentCategoryRow(
                    category = category,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
private fun DepartmentCategoryRow(
    category: DepartmentCategory,
    onCategoryClick: (DepartmentCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(category.titleRes)

    FeatureRow(
        modifier = modifier.fillMaxWidth(),
        onClick = { onCategoryClick(category) },
        title = title,
        titleStyle = RebrandKoinTheme.typography.bold15.copy(
            color = RebrandKoinTheme.colors.neutral800
        ),
        icon = {
            IconBadge(
                imageVector = ImageVector.vectorResource(category.iconRes),
                contentDescription = title
            )
        },
        trailingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_department_arrow_right),
                contentDescription = null,
                tint = RebrandKoinTheme.colors.neutral500
            )
        },
        colors = FeatureRowDefaults.colors(
            backgroundColor = RebrandKoinTheme.colors.neutral0
        ),
        shape = RectangleShape,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        borderWidth = 0.dp,
        iconSpacing = 16.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun DepartmentCategoryListPreview() {
    RebrandKoinTheme {
        DepartmentCategoryList(
            categories = DepartmentCategory.ALL,
            onCategoryClick = {}
        )
    }
}
