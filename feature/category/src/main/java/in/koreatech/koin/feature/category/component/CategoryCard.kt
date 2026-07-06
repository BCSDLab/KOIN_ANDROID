package `in`.koreatech.koin.feature.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.category.R

@Composable
fun CategoryCard(
    categoryIcon: @Composable () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(RebrandKoinTheme.colors.neutral0)
            .noRippleClickable(onClick = onClick)
            .padding(16.dp)
    ) {
        categoryIcon()

        Spacer(modifier = Modifier.height(8.dp))

        BasicText(
            text = title,
            style = RebrandKoinTheme.typography.bold14.copy(color = RebrandKoinTheme.colors.neutral800)
        )

        BasicText(
            text = description,
            style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.neutral500)
        )
    }
}

@Preview
@Composable
private fun CategoryCardPreview() {
    CategoryCard(
        categoryIcon = {
            CategoryIcon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                contentDescription = null
            )
        },
        title = stringResource(R.string.category_timetable),
        description = stringResource(R.string.timetable_description)
    )
}
