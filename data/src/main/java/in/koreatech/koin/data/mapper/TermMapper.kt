package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.entity.term.TermArticleEntity
import `in`.koreatech.koin.data.entity.term.TermEntity
import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.term.TermArticle

fun TermEntity.toTerm(): Term = Term(
    header = header,
    articles = articles.map(TermArticleEntity::toTermArticle),
    footer = footer
)

fun TermArticleEntity.toTermArticle(): TermArticle = TermArticle(
    article = article,
    content = content
)