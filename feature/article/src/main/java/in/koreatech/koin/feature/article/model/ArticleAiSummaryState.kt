package `in`.koreatech.koin.feature.article.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleAiSummary
import kotlinx.parcelize.Parcelize

enum class AiSummaryStatus {
    SUCCESS,
    PENDING,
    UNAVAILABLE;

    companion object {
        fun from(status: String) = entries.find { it.name == status } ?: UNAVAILABLE
    }
}

@Parcelize
data class ArticleAiSummaryState(
    val status: AiSummaryStatus,
    val summaryItems: List<SummaryItemState>
) : Parcelable

fun ArticleAiSummary.toArticleAiSummaryState() = ArticleAiSummaryState(
    status = AiSummaryStatus.from(status),
    summaryItems = summaryItems.map { it.toSummaryItemState() }
)

fun List<SummaryItemState>.toSummaryString() = this.joinToString(separator = "\n\n") { "${it.icon} ${it.text}" }

@Parcelize
data class SummaryItemState(
    val icon: String,
    val text: String
) : Parcelable

fun ArticleAiSummary.SummaryItem.toSummaryItemState() = SummaryItemState(
    icon = icon,
    text = text
)
