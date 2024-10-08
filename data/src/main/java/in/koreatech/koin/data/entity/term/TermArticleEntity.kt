package `in`.koreatech.koin.data.entity.term

import kotlinx.serialization.Serializable

@Serializable
data class TermArticleEntity(
    val article: String,
    val content: List<String>
)