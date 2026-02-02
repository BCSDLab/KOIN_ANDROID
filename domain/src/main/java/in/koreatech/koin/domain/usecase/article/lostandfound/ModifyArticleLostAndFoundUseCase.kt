package `in`.koreatech.koin.domain.usecase.article.lostandfound

import `in`.koreatech.koin.domain.repository.ArticleRepository
import javax.inject.Inject

class ModifyArticleLostAndFoundUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        category: String,
        foundPlace: String,
        foundDate: String,
        content: String?,
        newImage: List<String>?,
        deleteImageIds: List<Int>?
    ): Result<Unit> {
        return articleRepository.modifyArticleLostAndFound(
            articleId,
            category,
            foundPlace.ifBlank { "장소 미상" },
            foundDate,
            content,
            newImage,
            deleteImageIds
        )
    }
}
