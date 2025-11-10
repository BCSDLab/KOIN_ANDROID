package `in`.koreatech.koin.feature.store.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MenuCategoryChips(
    modifier: Modifier = Modifier,
    menuCategories: ImmutableList<MenuCategoryModel>,
    onCategoryClicked: (Int) -> Unit = { }
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .background(color = colorResource(id = R.color.store_detail_background))
            .horizontalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        menuCategories.forEachIndexed { i, tag ->
            key(tag.menuGroupId) {
                MenuCategoryChip(
                    menuCategory = tag,
                    modifier = Modifier.padding(end = 8.dp),
                    onCategoryClicked = {
                        onCategoryClicked(it)
                    }
                )
            }
        }
    }
}

@Composable
fun MenuCategoryChip(
    menuCategory: MenuCategoryModel,
    modifier: Modifier = Modifier,
    onCategoryClicked: (Int) -> Unit = {}
) {
    val borderColor = if (menuCategory.isChecked) colorResource(id = R.color.store_detail_chip) else Color.Transparent
    val textColor = if (menuCategory.isChecked) colorResource(id = R.color.store_detail_chip) else KoinTheme.colors.neutral400

    val onClick = remember(menuCategory.menuGroupId) { { onCategoryClicked(menuCategory.menuGroupId) } }

    Surface(
        elevation = 1.dp,
        shape = RoundedCornerShape(50),
        color = Color.White,
        modifier = modifier
            .heightIn(min = 24.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = menuCategory.menuGroupName,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
private fun MenuCategoryChipPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        MenuCategoryChip(
            menuCategory = MenuCategoryModel(
                menuGroupId = 1,
                menuGroupName = "음료",
                menus = persistentListOf(),
                isChecked = true
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}
