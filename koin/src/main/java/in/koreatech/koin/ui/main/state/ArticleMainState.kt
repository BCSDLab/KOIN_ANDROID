package `in`.koreatech.koin.ui.main.state

import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticleNoti
import `in`.koreatech.koin.domain.model.article.ArticleNotiType

sealed class ArticleMainState {
    data class Noti(val title: String, val sub: String, val value: String, val type: ArticleNotiType): ArticleMainState()
    data class Content(val title: String, val id: Int, val boardId: Int): ArticleMainState()
}

fun ArticleHeader.toContent() = ArticleMainState.Content(
    title = title,
    id = id,
    boardId = boardId
)

fun ArticleNoti.toNoti() = ArticleMainState.Noti(
    title = title,
    sub = sub,
    value = value,
    type = type
)