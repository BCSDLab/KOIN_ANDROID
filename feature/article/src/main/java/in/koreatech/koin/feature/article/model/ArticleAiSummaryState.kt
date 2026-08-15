package `in`.koreatech.koin.feature.article.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleAiSummary
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleAiSummaryState(
    val status: String,
    val summaryItems: List<SummaryItemState>
) : Parcelable

fun ArticleAiSummary.toArticleAiSummaryState() = ArticleAiSummaryState(
    status = status,
    summaryItems = summaryItems.map { it.toSummaryItemState() }
)

private const val AI_SUMMARY_SUCCESS = "SUCCESS"

fun ArticleAiSummaryState.isSuccess() = status == AI_SUMMARY_SUCCESS

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
