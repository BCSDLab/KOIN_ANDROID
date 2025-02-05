package `in`.koreatech.koin.feature.lostandfound.ui.report

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.ReportReason
import `in`.koreatech.koin.feature.lostandfound.ui.report.component.LostAndFoundReportContent
import `in`.koreatech.koin.feature.lostandfound.ui.report.component.lostAndFoundReportReasonList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostAndFoundReport(
    articleId: Int,
    onSuccess: () -> Unit,
    viewModel: LostAndFoundReportViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(sideEffect = it, context = context, onSuccess = onSuccess)
    }

    KoinTheme {
        Scaffold(
            topBar = {
                KoinTopAppBar(
                    title = stringResource(R.string.report_title),
                    onNavigationIconClick = { (context as Activity).finish() },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                        containerColor = KoinTheme.colors.primary500,
                        navigationIconContentColor = KoinTheme.colors.neutral0,
                        titleContentColor = KoinTheme.colors.neutral0,
                        actionIconContentColor = KoinTheme.colors.neutral0,
                    )
                )
            },
            containerColor = KoinTheme.colors.neutral0
        ) { contentPadding ->
            LostAndFoundReportContent(
                modifier = Modifier.padding(contentPadding),
                selectedItemIndex = lostAndFoundReportReasonList.indexOf(uiState.reportReason),
                onSelectedItemChange = { index ->
                    viewModel.setReportReason(lostAndFoundReportReasonList[index])
                    viewModel.setReportReasonTitle(context.getString(lostAndFoundReportReasonList[index].titleRes))
                    viewModel.setReportReasonDescription(
                        context.getString(
                            lostAndFoundReportReasonList[index].descriptionRes
                        )
                    )
                },
                otherReason = if (uiState.reportReason == ReportReason.OTHER) uiState.reportReasonDescription else "",
                onOtherReasonChange = {
                    viewModel.setReportReasonDescription(it)
                },
                onReport = {
                    viewModel.reportArticle(articleId)
                }
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: LostAndFoundReportSideEffect,
    context: Context,
    onSuccess: () -> Unit
) {
    when (sideEffect) {
        is LostAndFoundReportSideEffect.ReportSuccess -> {
            onSuccess()
        }

        is LostAndFoundReportSideEffect.ReportFailure -> {
            Timber.d("Failed to report article ${sideEffect.reason}")
            Toast.makeText(
                context,
                context.getString(R.string.report_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}