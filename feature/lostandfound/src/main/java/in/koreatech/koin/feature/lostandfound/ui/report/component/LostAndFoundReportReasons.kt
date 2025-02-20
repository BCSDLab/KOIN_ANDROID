package `in`.koreatech.koin.feature.lostandfound.ui.report.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.DESCRIPTION_MAX_LENGTH
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.REPORT_OTHER_REASON_MAX_LENGTH
import `in`.koreatech.koin.feature.lostandfound.enums.ReportReason

@Composable
fun LostAndFoundReportReasons(
    selectedItem: Int,
    onSelectedItemChange: (Int) -> Unit = {},
    otherReason: String,
    onOtherReasonChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 20.dp),
    ) {
        lostAndFoundReportReasonList.forEachIndexed { index, reportReason ->
            if (reportReason == ReportReason.OTHER) {
                LostAndFoundReportReasonOtherItem(
                    onFocused = {
                        onSelectedItemChange(index)
                    },
                    isSelected = selectedItem == index,
                    reason = otherReason,
                    onOtherReasonChange = onOtherReasonChange,
                )
            } else {
                LostAndFoundReportReasonItem(
                    modifier = Modifier.noRippleClickable {
                        onSelectedItemChange(index)
                    },
                    reportReason = reportReason,
                    isSelected = selectedItem == index
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = KoinTheme.colors.neutral200
                )
            }
        }
    }
}

@Composable
fun LostAndFoundReportReasonItem(
    modifier: Modifier = Modifier,
    reportReason: ReportReason = ReportReason.SPAM,
    isSelected: Boolean = false,
) {
    Row(
        modifier = modifier.padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(horizontal = 8.dp),
            painter = painterResource(id = if (isSelected) R.drawable.ic_report_item_selected else R.drawable.ic_report_item_unselected),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(id = reportReason.titleRes),
                style = KoinTheme.typography.medium16,
                color = KoinTheme.colors.neutral800
            )
            Text(
                text = stringResource(id = reportReason.descriptionRes),
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral500
            )
        }
    }
}

@Composable
fun LostAndFoundReportReasonOtherItem(
    reason: String,
    onOtherReasonChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onFocused: () -> Unit = {},
    isSelected: Boolean = false,
) {
    Column(
        modifier = modifier
            .padding(vertical = 14.dp)
            .noRippleClickable {
                onFocused()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(horizontal = 8.dp),
                painter = painterResource(id = if (isSelected) R.drawable.ic_report_item_selected else R.drawable.ic_report_item_unselected),
                contentDescription = null,
            )
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = ReportReason.OTHER.titleRes),
                    style = KoinTheme.typography.medium16,
                    color = KoinTheme.colors.neutral800
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${reason.length}/$REPORT_OTHER_REASON_MAX_LENGTH",
                    style = KoinTheme.typography.regular12,
                    color = Color(0xFF8E8E8E)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        ReportTextField(
            modifier = Modifier.padding(horizontal = 8.dp),
            value = reason,
            onValueChange = onOtherReasonChange,
            placeholder = stringResource(id = R.string.report_reason_other_placeholder),
            onFocused = onFocused
        )
    }
}

@Composable
fun ReportTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    maxLength: Int = REPORT_OTHER_REASON_MAX_LENGTH,
    onFocused: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onFocused()
        }
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = KoinTheme.colors.neutral300, shape = KoinTheme.shapes.extraSmall)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        interactionSource = interactionSource,
        value = value,
        textStyle = KoinTheme.typography.regular14,
        onValueChange = {
            if (value.length < maxLength) {
                onValueChange(it)
            } else {
                onValueChange(it.take(maxLength))
            }
        },
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = KoinTheme.typography.regular14,
                        color = Color(0xFF8E8E8E),
                    )
                } else {
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewLostAndFoundReportReasons() {
    KoinSurface {
        LostAndFoundReportReasons(
            selectedItem = 3,
            onSelectedItemChange = {},
            otherReason = "",
            onOtherReasonChange = {}
        )
    }
}