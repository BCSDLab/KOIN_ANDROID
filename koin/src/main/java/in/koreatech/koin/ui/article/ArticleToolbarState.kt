package `in`.koreatech.koin.ui.article

import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.R

enum class ArticleToolbarState(
    @StringRes val title: Int,
    @MenuRes val menuRes: Int? = null
) {
    ARTICLE_LIST(R.string.navigation_title_article, R.menu.menu_article_list),
    ARTICLE_DETAIL(R.string.navigation_title_article),
    ARTICLE_SEARCH(R.string.navigation_title_article_search),
    ARTICLE_KEYWORD(R.string.navigation_title_article_keyword)
}