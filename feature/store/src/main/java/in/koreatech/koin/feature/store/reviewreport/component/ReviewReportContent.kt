package `in`.koreatech.koin.feature.store.reviewreport.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.reviewreport.enums.ReportReason
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReviewReportContent(
    selectedItem: ImmutableList<ReportReason>,
    otherReason: String,
    modifier: Modifier = Modifier,
    itemList: ImmutableList<ReportReason> = reviewReportReasonList,
    onSelectedItemChange: (ReportReason) -> Unit = {},
    onOtherReasonChange: (String) -> Unit = {},
    onReport: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
            .height(IntrinsicSize.Max)
    ) {
        ReviewReportHeader()
        ReviewReportReasons(
            selectedItem = selectedItem,
            itemList = itemList,
            onSelectedItemChange = onSelectedItemChange,
            otherReason = otherReason,
            onOtherReasonChange = onOtherReasonChange
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            onClick = onReport,
            shape = RebrandKoinTheme.shapes.small,
            contentPadding = PaddingValues(vertical = 12.dp),
            enabled = if (selectedItem.contains(ReportReason.OTHER)) {
                otherReason.isNotBlank()
            } else {
                selectedItem.isNotEmpty()
            }
        ) {
            BasicText(
                text = stringResource(id = R.string.review_report),
                style = RebrandKoinTheme.typography.bold15.copy(
                    color = RebrandKoinTheme.colors.neutral0
                )
            )
        }
    }
}

@Preview
@Composable
private fun ReviewReportContentPreview() {
    KoinSurface {
        ReviewReportContent(
            itemList = reviewReportReasonList,
            selectedItem = persistentListOf(),
            onSelectedItemChange = {},
            otherReason = "",
            onOtherReasonChange = {}
        )
    }
}

val reviewReportReasonList =
    persistentListOf(
        ReportReason.OFF_TOPIC,
        ReportReason.SPAM,
        ReportReason.INAPPROPRIATE_LANGUAGE,
        ReportReason.PRIVACY,
        ReportReason.OTHER
    )
