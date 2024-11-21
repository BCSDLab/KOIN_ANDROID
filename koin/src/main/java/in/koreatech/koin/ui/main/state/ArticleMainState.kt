package `in`.koreatech.koin.ui.main.state

sealed class ArticleMainState {
    data class KeywordNoti(val title: String, val sub: String): ArticleMainState()
    data class Content(val title: String, val id: Int, val boardId: Int): ArticleMainState()
}