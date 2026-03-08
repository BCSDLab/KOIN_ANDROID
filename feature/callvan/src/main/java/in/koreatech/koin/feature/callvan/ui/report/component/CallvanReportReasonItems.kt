package `in`.koreatech.koin.feature.callvan.ui.report.component

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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason

const val CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH = 150

@Composable
fun CallvanReportReasonItem(
    reason: CallvanReportReason,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.padding(start = 8.dp),
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = RebrandKoinTheme.colors.primary500,
                unselectedColor = KoinTheme.colors.neutral500
            )
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = reason.title,
                style = KoinTheme.typography.medium18,
                color = KoinTheme.colors.neutral800
            )
            Text(
                text = reason.description,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral600
            )
        }
    }
}

@Composable
fun CallvanReportReasonTextFieldItem(
    reason: CallvanReportReason,
    isSelected: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                modifier = Modifier.padding(start = 8.dp),
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = RebrandKoinTheme.colors.primary500,
                    unselectedColor = KoinTheme.colors.neutral500
                )
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reason.title,
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.neutral800
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.callvan_report_other_reason_length, value.length, CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH),
                    style = KoinTheme.typography.regular12,
                    color = Color(0xFF8E8E8E)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        CallvanReportTextField(
            modifier = Modifier.padding(horizontal = 8.dp),
            value = value,
            onValueChange = onValueChange,
            placeholder = stringResource(R.string.callvan_report_other_placeholder),
            onClick = { if (!isSelected) onClick() }
        )
    }
}

@Composable
private fun CallvanReportTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    maxLength: Int = CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) onClick()
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
            if (it.length <= maxLength) onValueChange(it) else onValueChange(it.take(maxLength))
        },
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = KoinTheme.typography.regular14,
                        color = Color(0xFF8E8E8E)
                    )
                } else {
                    innerTextField()
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonItemPreview() {
    CallvanReportReasonItem(
        reason = CallvanReportReason.NO_SHOW,
        isSelected = false,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonItemSelectedPreview() {
    CallvanReportReasonItem(
        reason = CallvanReportReason.NO_SHOW,
        isSelected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonTextFieldItemPreview() {
    CallvanReportReasonTextFieldItem(
        reason = CallvanReportReason.OTHER,
        isSelected = false,
        value = "",
        onValueChange = {},
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonTextFieldItemSelectedPreview() {
    CallvanReportReasonTextFieldItem(
        reason = CallvanReportReason.OTHER,
        isSelected = true,
        value = "신고 사유 입력 예시",
        onValueChange = {},
        onClick = {}
    )
}
