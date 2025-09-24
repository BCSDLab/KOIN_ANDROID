package `in`.koreatech.koin.feature.article.ui.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.ReportReason

@Composable
fun LostAndFoundReportContent(
    itemList: List<ReportReason>,
    selectedItemIndex: IntArray,
    onSelectedItemChange: (Int) -> Unit = {},
    otherReason: String,
    onOtherReasonChange: (String) -> Unit = {},
    onReport: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
        modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
            .height(IntrinsicSize.Max)
    ) {
        LostAndFoundReportHeader()
        LostAndFoundReportReasons(
            itemList = itemList,
            selectedItem = selectedItemIndex,
            onSelectedItemChange = onSelectedItemChange,
            otherReason = otherReason,
            onOtherReasonChange = onOtherReasonChange
        )

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            text = stringResource(id = R.string.report_submit),
            onClick = onReport,
            modifier =
            Modifier
                .padding(vertical = 20.dp, horizontal = 24.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun LostAndFoundReportContentPreview() {
    KoinSurface {
        LostAndFoundReportContent(
            itemList = lostAndFoundReportReasonList,
            selectedItemIndex = intArrayOf(0),
            onSelectedItemChange = {},
            otherReason = "",
            onOtherReasonChange = {}
        )
    }
}

val lostAndFoundReportReasonList =
    listOf(
        ReportReason.OFF_TOPIC,
        ReportReason.SPAM,
        ReportReason.INAPPROPRIATE_LANGUAGE,
        ReportReason.PRIVACY,
        ReportReason.OTHER
    )
