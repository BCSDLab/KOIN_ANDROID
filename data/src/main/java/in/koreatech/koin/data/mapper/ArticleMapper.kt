package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundRequest
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload

fun List<ArticleLostAndFoundUpload>.toArticleLostAndFoundRequest(): ArticleLostAndFoundRequest {
    return ArticleLostAndFoundRequest(
        articles = this.map { it.toArticleLostAndFoundBody() },
    )
}

fun ArticleLostAndFoundUpload.toArticleLostAndFoundBody(): ArticleLostAndFoundRequest.ArticleLostAndFoundBody {
    return ArticleLostAndFoundRequest.ArticleLostAndFoundBody(
        category = category,
        foundPlace = foundPlace,
        foundDate = foundDate,
        content = content,
        images = images,
        type = type,
    )
}
