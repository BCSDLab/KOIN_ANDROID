package `in`.koreatech.koin.feature.lostandfound.ui.write

data class LostAndFoundWriteArticleState(
    val itemList: List<LostAndFoundWriteArticleItemState> = emptyList(),
    val isAllFieldValid: Boolean = false,
)