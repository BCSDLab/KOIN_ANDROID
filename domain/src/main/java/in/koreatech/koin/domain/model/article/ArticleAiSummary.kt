package `in`.koreatech.koin.domain.model.article

data class ArticleAiSummary(
    val status: String,
    val summaryItems: List<SummaryItem>
) {
    data class SummaryItem(
        val icon: String,
        val text: String
    )
}
