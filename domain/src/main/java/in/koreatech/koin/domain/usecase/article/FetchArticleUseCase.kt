package `in`.koreatech.koin.domain.usecase.article

import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.article.html.ArticleContent
import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class FetchArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int) : Result<ArticlePagination> {
        return runCatching { articleRepository.fetchArticlePagination(boardId, page, limit) }
    }

    suspend fun fetchArticle(articleId: Int) : Result<ArticleContent> {
        return runCatching { articleRepository.fetchArticle(articleId) }
    }

    suspend fun fetchHotArticles() : Result<List<ArticleHeader>> {
        return runCatching { articleRepository.fetchHotArticles() }
    }

    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int) : Result<ArticlePagination> {
        return runCatching { articleRepository.fetchSearchedArticles(query, page, limit) }
    }

    suspend fun fetchHotKeywords(count: Int) : Result<List<String>> {
        return runCatching { articleRepository.fetchHotKeywords(count) }
    }
}