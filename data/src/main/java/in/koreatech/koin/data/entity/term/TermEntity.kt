package `in`.koreatech.koin.data.entity.term

import kotlinx.serialization.Serializable

@Serializable
data class TermEntity(
    val header: String,
    val articles: List<TermArticleEntity>,
    val footer: String
)
