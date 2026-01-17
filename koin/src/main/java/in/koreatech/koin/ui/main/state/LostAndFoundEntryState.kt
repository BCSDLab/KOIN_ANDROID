package `in`.koreatech.koin.ui.main.state

import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundStats

data class LostAndFoundEntryState(
    val postCount: Int,
    val foundCount: Int
)

fun ArticleLostAndFoundStats.toLostAndFoundEntryState() =
    LostAndFoundEntryState(
        postCount = foundCount + notFoundCount,
        foundCount = foundCount
    )