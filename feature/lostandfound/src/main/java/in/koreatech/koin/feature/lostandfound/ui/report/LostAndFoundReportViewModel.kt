package `in`.koreatech.koin.feature.lostandfound.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem
import `in`.koreatech.koin.domain.usecase.article.lostandfound.ReportLostAndFoundArticleUseCase
import `in`.koreatech.koin.feature.lostandfound.enums.ReportReason
import `in`.koreatech.koin.feature.lostandfound.ui.report.component.lostAndFoundReportReasonList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LostAndFoundReportViewModel @Inject constructor(
    private val reportLostAndFoundArticleUseCase: ReportLostAndFoundArticleUseCase
) : ViewModel(), ContainerHost<LostAndFoundReportState, LostAndFoundReportSideEffect> {
    override val container =
        container<LostAndFoundReportState, LostAndFoundReportSideEffect>(LostAndFoundReportState())

    private fun addReportReason(reportReason: ReportReason) = intent {
        reduce {
            state.copy(selectedReason = state.selectedReason + lostAndFoundReportReasonList.indexOf(reportReason))
        }
    }

    private fun removeReportReason(reportReason: ReportReason) = intent {
        reduce {
            state.copy(selectedReason = state.selectedReason.filterNot { lostAndFoundReportReasonList[it] == reportReason }.toIntArray())
        }
    }

    fun setReportReason(reportReason: ReportReason) = intent {
        if (lostAndFoundReportReasonList.indexOf(reportReason) in state.selectedReason) {
            removeReportReason(reportReason)
        } else {
            addReportReason(reportReason)
        }
    }

    fun setReportReasonDescription(reportReasonDescription: String) = blockingIntent {
        reduce {
            state.copy(reportReasonDescription = reportReasonDescription)
        }
    }

    fun reportArticle(articleId: Int) = viewModelScope.launch {
        val reportReasonList = mutableListOf<ArticleLostAndFoundReportItem>()
        intent {
            state.selectedReason.forEach {
                if (lostAndFoundReportReasonList[it] == ReportReason.OTHER) {
                    reportReasonList.add(ArticleLostAndFoundReportItem(lostAndFoundReportReasonList[it].title, state.reportReasonDescription))
                } else {
                    reportReasonList.add(ArticleLostAndFoundReportItem(lostAndFoundReportReasonList[it].title, lostAndFoundReportReasonList[it].description))
                }
            }
            reportLostAndFoundArticleUseCase(
                articleId, reportReasonList
            ).onSuccess {
                postSideEffect(LostAndFoundReportSideEffect.ReportSuccess)
            }.onFailure {
                postSideEffect(LostAndFoundReportSideEffect.ReportFailure(it.message ?: ""))
            }
        }
    }
}