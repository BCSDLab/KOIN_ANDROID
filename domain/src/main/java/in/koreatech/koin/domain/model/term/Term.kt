package `in`.koreatech.koin.domain.model.term

data class Term(
    val header: String,
    val articles: List<TermArticle>,
    val footer: String
)
