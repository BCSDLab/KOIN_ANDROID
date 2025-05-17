package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.owner.MenuCategory
import `in`.koreatech.koin.feature.store.R

@Composable
fun MenuCategoryChips(menuCategories: List<MenuCategory>) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier.fillMaxWidth().background(color = colorResource(id = R.color.store_detail_background))
            .horizontalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        menuCategories.forEachIndexed { i, tag ->
            MenuCategoryChip(
                text = tag.categoryName,
                isSelected = i == 0,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun MenuCategoryChip(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val borderColor = if (isSelected) colorResource(id = R.color.store_detail_chip) else Color.Transparent
    val textColor = if (isSelected) colorResource(id = R.color.store_detail_chip) else KoinTheme.colors.neutral400

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
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
fun MenuCategoryChipPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        MenuCategoryChip(
            text = "족발",
            modifier = Modifier.padding(8.dp)
        )
    }
}
