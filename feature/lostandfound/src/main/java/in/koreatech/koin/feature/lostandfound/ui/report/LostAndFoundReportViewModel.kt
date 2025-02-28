package `in`.koreatech.koin.feature.lostandfound.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem
import `in`.koreatech.koin.domain.usecase.article.lostandfound.ReportLostAndFoundArticleUseCase
import `in`.koreatech.koin.feature.lostandfound.enums.ReportReason
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LostAndFoundReportViewModel @Inject constructor(
    private val reportLostAndFoundArticleUseCase: ReportLostAndFoundArticleUseCase
) : ViewModel(), ContainerHost<LostAndFoundReportState, LostAndFoundReportSideEffect> {
    override val container =
        container<LostAndFoundReportState, LostAndFoundReportSideEffect>(LostAndFoundReportState())

    fun setReportReason(reportReason: ReportReason) = intent {
        reduce {
            state.copy(reportReason = reportReason)
        }
    }

    fun setReportReasonTitle(reportReasonTitle: String) = intent {
        reduce {
            state.copy(reportReasonTitle = reportReasonTitle)
        }
    }

    fun setReportReasonDescription(reportReasonDescription: String) = intent {
        reduce {
            state.copy(reportReasonDescription = reportReasonDescription)
        }
    }

    fun reportArticle(articleId: Int) = viewModelScope.launch {
        intent {
            reportLostAndFoundArticleUseCase(
                articleId, listOf(
                    ArticleLostAndFoundReportItem(
                        state.reportReasonTitle,
                        state.reportReasonDescription
                    )
                )
            ).onSuccess {
                postSideEffect(LostAndFoundReportSideEffect.ReportSuccess)
            }.onFailure {
                postSideEffect(LostAndFoundReportSideEffect.ReportFailure(it.message ?: ""))
            }
        }
    }
}