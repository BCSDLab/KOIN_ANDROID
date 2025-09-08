package `in`.koreatech.koin.feature.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.KoinCoilImageLoader
import `in`.koreatech.koin.domain.model.store.StoreCategories

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainStoreWidget(
    categories: List<StoreCategories>,
    modifier: Modifier = Modifier,
    onClick: (categoryId: Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        BasicText(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.store_main_widget),
            style = KoinTheme.typography.bold18.copy(fontSize = 15.sp, color = KoinTheme.colors.primary500)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            maxItemsInEachRow = 6,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically)
        ) {
            categories.forEach { category ->
                CategoryItem(
                    modifier = Modifier.weight(1f),
                    categoryImageUrl = category.imageUrl,
                    categoryName = category.name,
                    onClick = {
                        onClick(category.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    categoryImageUrl: Any,
    categoryName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(KoinTheme.shapes.small)
                .background(
                    color = KoinTheme.colors.neutral100,
                    shape = KoinTheme.shapes.small
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                ) {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = categoryImageUrl,
                contentDescription = categoryName,
                modifier = Modifier.size(32.dp),
                imageLoader = KoinCoilImageLoader.getImageLoader(context)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        BasicText(
            text = categoryName,
            style = KoinTheme.typography.regular12.copy(
                color = KoinTheme.colors.neutral800,
                textAlign = TextAlign.Center
            )
        )
    }
}
