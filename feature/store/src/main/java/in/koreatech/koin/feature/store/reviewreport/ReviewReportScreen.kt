package `in`.koreatech.koin.feature.store.reviewreport

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.reviewreport.component.ReviewReportContent
import `in`.koreatech.koin.feature.store.reviewreport.enums.ReportReason
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ReviewReportScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    viewModel.collectSideEffect {
        handleSideEffect(it, context, dispatcher) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    if (uiState.isLoading) {
        Popup(
            alignment = Alignment.Center
        ) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }
    }

    ReviewReportScreen(
        modifier = modifier,
        selectedReasons = uiState.selectedReasons,
        otherReason = uiState.etcReason,
        onSelectedItemChange = remember(viewModel) {
            {
                if (uiState.selectedReasons.contains(it)) {
                    viewModel.removeReportReason(it)
                } else {
                    viewModel.addReportReason(it)
                }
            }
        },
        onOtherReasonChange = viewModel::updateOtherReportReason,
        onReport = viewModel::reportReview
    )
}

@Composable
private fun ReviewReportScreen(
    selectedReasons: ImmutableList<ReportReason>,
    otherReason: String,
    modifier: Modifier = Modifier,
    onSelectedItemChange: (ReportReason) -> Unit = {},
    onOtherReasonChange: (String) -> Unit = {},
    onReport: () -> Unit = {}
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        modifier = modifier
    ) {
        KoinStoreTopAppBar(
            title = stringResource(R.string.store_title_review),
            onNavigationIconClick = {
                dispatcher?.onBackPressed()
            }
        )

        ReviewReportContent(
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            selectedItem = selectedReasons,
            otherReason = otherReason,
            onSelectedItemChange = onSelectedItemChange,
            onOtherReasonChange = onOtherReasonChange,
            onReport = onReport
        )
    }
}

private fun handleSideEffect(
    sideEffect: ReviewReportSideEffect,
    context: Context,
    onBackPressedDispatcher: OnBackPressedDispatcher?,
    showToast: (String) -> Unit = {}
) {
    when (sideEffect) {
        ReviewReportSideEffect.ReviewReportSuccess -> {
            onBackPressedDispatcher?.onBackPressed()
            showToast.invoke(context.getString(R.string.review_reported))
        }
    }
}
