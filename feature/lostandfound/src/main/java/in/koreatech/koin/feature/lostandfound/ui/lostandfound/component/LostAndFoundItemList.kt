package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.LostAndFoundItemState

@Composable
fun LostAndFoundItemList(
    data: List<LostAndFoundItemState>,
    modifier: Modifier = Modifier,
    onItemClick: (LostAndFoundItemState) -> Unit = {}
) {
    LazyColumn {
        items(data) {
            LostAndFoundItem(
                lostItemCategory = it.category,
                foundPlace = it.foundPlace,
                content = it.content,
                author = it.author,
                foundDate = it.foundDate,
                registeredAt = it.registeredAt,
                modifier = modifier
            ) {
                onItemClick(it)
            }
        }
    }
}
