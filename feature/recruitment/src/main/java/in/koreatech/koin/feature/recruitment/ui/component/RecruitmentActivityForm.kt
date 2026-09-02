package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.model.toStable
import `in`.koreatech.koin.feature.recruitment.utils.toDateText

private val FormShape = RoundedCornerShape(16.dp)

@Composable
fun RecruitmentActivityForm(
    onCancel: () -> Unit,
    onConfirm: (RecruitmentActivityEntry) -> Unit,
    modifier: Modifier = Modifier,
    existingActivity: RecruitmentActivityEntry? = null
) {
    var name by remember { mutableStateOf(existingActivity?.name.orEmpty()) }
    var startDate by remember { mutableStateOf(existingActivity?.startDate?.toStable()) }
    var endDate by remember { mutableStateOf(existingActivity?.endDate?.toStable()) }
    var isOngoing by remember { mutableStateOf(existingActivity?.isOngoing ?: false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingStartDate by remember { mutableStateOf(true) }
    var content by remember { mutableStateOf(existingActivity?.content.orEmpty()) }

    if (showDatePicker) {
        RecruitmentDatePickerDialog(
            defaultDate = (if (isSelectingStartDate) startDate else endDate) ?: StableLocalDate.now(),
            onPositive = { date ->
                if (isSelectingStartDate) startDate = date else endDate = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            onNegative = { showDatePicker = false }
        )
    }

    Column(
        modifier = modifier
            .background(RebrandKoinTheme.colors.neutral0, FormShape)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.recruitment_activity_name),
                        style = RebrandKoinTheme.typography.medium16,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                    Text(
                        text = stringResource(R.string.recruitment_activity_required_mark),
                        style = RebrandKoinTheme.typography.medium16,
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.recruitment_activity_close_content_description),
                    tint = RebrandKoinTheme.colors.neutral500,
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable { onCancel() }
                )
            }
            RecruitmentTextField(
                value = name,
                onValueChange = { name = it },
                hint = stringResource(R.string.recruitment_activity_name_hint)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                Text(
                    text = stringResource(R.string.recruitment_activity_period),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Text(
                    text = stringResource(R.string.recruitment_activity_required_mark),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.primary500
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecruitmentDateSelectBox(
                    text = startDate?.value?.toDateText() ?: stringResource(R.string.recruitment_activity_start_date),
                    isPlaceholder = startDate == null,
                    onClick = {
                        isSelectingStartDate = true
                        showDatePicker = true
                    },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.recruitment_activity_period_separator),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.neutral400
                )
                RecruitmentDateSelectBox(
                    text = endDate?.value?.toDateText() ?: stringResource(R.string.recruitment_activity_end_date),
                    isPlaceholder = endDate == null,
                    onClick = {
                        if (!isOngoing) {
                            isSelectingStartDate = false
                            showDatePicker = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    RadioButton(
                        selected = isOngoing,
                        onClick = { isOngoing = !isOngoing },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = RebrandKoinTheme.colors.primary500
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.recruitment_activity_ongoing),
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral700
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                Text(
                    text = stringResource(R.string.recruitment_activity_content),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Text(
                    text = stringResource(R.string.recruitment_activity_required_mark),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.primary500
                )
            }
            RecruitmentTextField(
                value = content,
                onValueChange = { content = it },
                hint = stringResource(R.string.recruitment_activity_content_hint),
                singleLine = false,
                minLines = 4,
                maxLength = RecruitmentActivityEntry.CONTENT_MAX_LENGTH
            )
        }

        RecruitmentFilledActionButton(
            text = stringResource(R.string.recruitment_activity_confirm),
            enabled = name.isNotBlank() &&
                content.isNotBlank() &&
                startDate != null &&
                (isOngoing || endDate != null),
            onClick = {
                onConfirm(
                    RecruitmentActivityEntry(
                        id = existingActivity?.id ?: System.currentTimeMillis(),
                        name = name,
                        startDate = requireNotNull(startDate).value,
                        endDate = if (isOngoing) null else endDate?.value,
                        isOngoing = isOngoing,
                        content = content
                    )
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentActivityFormPreview() {
    RebrandKoinTheme {
        RecruitmentActivityForm(onCancel = {}, onConfirm = {})
    }
}
