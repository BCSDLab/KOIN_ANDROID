package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.LostAndFoundItemState

@Composable
fun LostAndFoundItemList(
    data: List<LostAndFoundItemState>,
    modifier: Modifier = Modifier,
    onItemClick: (LostAndFoundItemState) -> Unit = {}
) {
    if (data.isEmpty()) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.empty_articles)
        )
    }
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
