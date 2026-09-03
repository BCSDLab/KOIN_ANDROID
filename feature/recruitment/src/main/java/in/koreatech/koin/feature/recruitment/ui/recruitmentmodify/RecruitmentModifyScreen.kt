package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDatePickerDialog
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDateSelectBox
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDropdown
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentTextField
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.component.RecruitmentAddRoleButton
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.component.RecruitmentParticipantCountRow
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.component.RecruitmentProgressTypeSelector
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.component.RecruitmentRoleRow
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.model.RecruitmentModifyRole
import `in`.koreatech.koin.feature.recruitment.utils.toDateText
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private const val TITLE_MAX_LENGTH = 50
private const val DESCRIPTION_MAX_LENGTH = 1000
private const val QUALIFICATION_MAX_LENGTH = 500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentModifyScreen(
    modifier: Modifier = Modifier,
    viewModel: RecruitmentModifyViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onRecruitmentModified: () -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            RecruitmentModifySideEffect.NavigateUp -> onNavigateUp()
            RecruitmentModifySideEffect.RecruitmentModifySuccess -> onRecruitmentModified()
            is RecruitmentModifySideEffect.RecruitmentModifyFailure ->
                ToastUtil.getInstance().makeShort(
                    sideEffect.message?.takeIf { it.isNotBlank() }
                        ?: context.getString(R.string.recruitment_modify_error)
                )

            is RecruitmentModifySideEffect.ShowLoadError ->
                ToastUtil.getInstance().makeShort(
                    sideEffect.message?.takeIf { it.isNotBlank() }
                        ?: context.getString(R.string.recruitment_load_error)
                )
        }
    }

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_modify_title),
                onNavigationIconClick = viewModel::showCancelConfirmDialog
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { contentPadding ->
        RecruitmentModifyScreenImpl(
            state = state,
            modifier = Modifier.padding(contentPadding),
            onCategorySelected = viewModel::setCategory,
            onCategoryDropdownExpandChange = viewModel::setCategoryDropdownExpanded,
            onTitleChange = viewModel::setTitle,
            onProgressTypeSelected = viewModel::setProgressType,
            onStartDateClick = { viewModel.showDatePickerDialog(DateSelectionTarget.RECRUIT_START) },
            onEndDateClick = { viewModel.showDatePickerDialog(DateSelectionTarget.RECRUIT_END) },
            onDeadlineClick = { viewModel.showDatePickerDialog(DateSelectionTarget.DEADLINE) },
            onDismissDatePickerDialog = viewModel::dismissDatePickerDialog,
            onDateSelected = { date ->
                when (state.dateSelectionTarget) {
                    DateSelectionTarget.RECRUIT_START -> viewModel.setRecruitStartDate(date)
                    DateSelectionTarget.RECRUIT_END -> viewModel.setRecruitEndDate(date)
                    DateSelectionTarget.DEADLINE -> viewModel.setApplicationDeadline(date)
                }
            },
            onAddRoleClick = viewModel::addRole,
            onRoleNameChange = viewModel::setRoleName,
            onRoleCountChange = viewModel::setRoleCount,
            onRoleRemoved = viewModel::removeRole,
            onRoleCountUndeterminedChange = viewModel::setRoleCountUndetermined,
            onParticipantCountChange = viewModel::setParticipantCount,
            onDescriptionChange = viewModel::setDescription,
            onRelatedUrlChange = viewModel::setRelatedUrl,
            onQualificationChange = viewModel::setQualification,
            onSubmitClick = viewModel::showSubmitConfirmDialog,
            onDismissSubmitConfirmDialog = viewModel::dismissSubmitConfirmDialog,
            onConfirmSubmit = viewModel::modifyRecruitment,
            onDismissCancelConfirmDialog = viewModel::dismissCancelConfirmDialog,
            onConfirmCancel = viewModel::confirmCancel
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun RecruitmentModifyScreenImpl(
    state: RecruitmentModifyState,
    modifier: Modifier = Modifier,
    onCategorySelected: (RecruitmentCategory) -> Unit = {},
    onCategoryDropdownExpandChange: (Boolean) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onProgressTypeSelected: (RecruitmentProgressType) -> Unit = {},
    onStartDateClick: () -> Unit = {},
    onEndDateClick: () -> Unit = {},
    onDeadlineClick: () -> Unit = {},
    onDismissDatePickerDialog: () -> Unit = {},
    onDateSelected: (StableLocalDate) -> Unit = {},
    onAddRoleClick: () -> Unit = {},
    onRoleNameChange: (String, String) -> Unit = { _, _ -> },
    onRoleCountChange: (String, Int) -> Unit = { _, _ -> },
    onRoleRemoved: (String) -> Unit = {},
    onRoleCountUndeterminedChange: (Boolean) -> Unit = {},
    onParticipantCountChange: (Int) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onRelatedUrlChange: (String) -> Unit = {},
    onQualificationChange: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onDismissSubmitConfirmDialog: () -> Unit = {},
    onConfirmSubmit: () -> Unit = {},
    onDismissCancelConfirmDialog: () -> Unit = {},
    onConfirmCancel: () -> Unit = {}
) {
    if (state.showDatePickerDialog) {
        RecruitmentDatePickerDialog(
            defaultDate = when (state.dateSelectionTarget) {
                DateSelectionTarget.RECRUIT_START -> state.recruitStartDate
                DateSelectionTarget.RECRUIT_END -> state.recruitEndDate
                DateSelectionTarget.DEADLINE -> state.applicationDeadline
            },
            onPositive = onDateSelected,
            onDismiss = onDismissDatePickerDialog,
            onNegative = onDismissDatePickerDialog
        )
    }

    if (state.showSubmitConfirmDialog) {
        ChoiceDialog(
            title = stringResource(R.string.recruitment_modify_submit_dialog_title),
            description = "",
            positiveButtonText = stringResource(R.string.recruitment_modify_submit_dialog_confirm),
            negativeButtonText = stringResource(R.string.recruitment_modify_submit_dialog_cancel),
            onPositive = onConfirmSubmit,
            onNegative = onDismissSubmitConfirmDialog
        )
    }

    if (state.showCancelConfirmDialog) {
        ChoiceDialog(
            title = stringResource(R.string.recruitment_modify_cancel_dialog_title),
            description = stringResource(R.string.recruitment_modify_cancel_dialog_description),
            positiveButtonText = stringResource(R.string.recruitment_modify_dialog_yes),
            negativeButtonText = stringResource(R.string.recruitment_modify_dialog_no),
            onPositive = onConfirmCancel,
            onNegative = onDismissCancelConfirmDialog
        )
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        FormSection(title = stringResource(R.string.recruitment_modify_category), isRequired = true) {
            RecruitmentDropdown(
                text = stringResource(state.category.labelRes),
                // category는 non-nullable이라 항상 선택된 값이 있음 → placeholder 상태 자체가 없음
                isPlaceholder = false,
                items = RecruitmentCategory.entries.map { stringResource(it.labelRes) }.toImmutableList(),
                isExpanded = state.isCategoryDropdownExpanded,
                onExpandedChange = onCategoryDropdownExpandChange,
                onItemSelected = { index -> onCategorySelected(RecruitmentCategory.entries[index]) }
            )
        }

        FormSection(
            title = stringResource(R.string.recruitment_modify_title_field),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(R.string.recruitment_modify_roles_count, state.title.length, TITLE_MAX_LENGTH),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.title,
                onValueChange = onTitleChange,
                hint = stringResource(R.string.recruitment_modify_title_hint),
                maxLength = TITLE_MAX_LENGTH
            )
        }

        FormSection(title = stringResource(R.string.recruitment_modify_progress_type), isRequired = true) {
            RecruitmentProgressTypeSelector(
                selected = state.progressType,
                onSelect = onProgressTypeSelected
            )
        }

        FormSection(title = stringResource(R.string.recruitment_modify_period), isRequired = true) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.recruitment_modify_period_range),
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RecruitmentDateSelectBox(
                            text = state.recruitStartDate.value.toDateText(),
                            onClick = onStartDateClick,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "-",
                            style = RebrandKoinTheme.typography.medium16,
                            color = RebrandKoinTheme.colors.neutral400,
                            modifier = Modifier.wrapContentHeight()
                        )
                        RecruitmentDateSelectBox(
                            text = state.recruitEndDate.value.toDateText(),
                            onClick = onEndDateClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.recruitment_modify_deadline),
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RecruitmentDateSelectBox(
                            text = state.applicationDeadline.value.toDateText(),
                            onClick = onDeadlineClick,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        FormSection(
            title = stringResource(R.string.recruitment_modify_roles),
            isRequired = true,
            titleSuffix = if (state.isRoleCountUndetermined) {
                null
            } else {
                stringResource(
                    R.string.recruitment_modify_roles_count,
                    state.roles.size,
                    RecruitmentModifyRole.MAX_ROLE_COUNT
                )
            },
            trailingContent = if (state.isRoleCountUndetermined) {
                null
            } else {
                {
                    RecruitmentAddRoleButton(
                        text = stringResource(R.string.recruitment_modify_add_role),
                        enabled = state.roles.size < RecruitmentModifyRole.MAX_ROLE_COUNT,
                        onClick = onAddRoleClick
                    )
                }
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.recruitment_modify_roles_description),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RadioButton(
                        selected = state.isRoleCountUndetermined,
                        onClick = { onRoleCountUndeterminedChange(!state.isRoleCountUndetermined) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = RebrandKoinTheme.colors.primary500
                        ),
                        // RadioButton은 기본적으로 48dp 최소 터치 영역을 가져서 시각적으로 좌측에
                        // 여백((48-20)/2=14dp)이 생깁니다. 디자인처럼 좌측 정렬시키기 위해 상쇄합니다.
                        modifier = Modifier.offset(x = (-14).dp)
                    )
                    Text(
                        text = stringResource(R.string.recruitment_modify_role_undetermined),
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral700,
                        modifier = Modifier.offset(x = (-28).dp)
                    )
                }
                if (state.isRoleCountUndetermined) {
                    RecruitmentParticipantCountRow(
                        count = state.participantCount,
                        onCountChange = onParticipantCountChange
                    )
                } else {
                    state.roles.forEach { role ->
                        RecruitmentRoleRow(
                            role = role,
                            onNameChange = { name -> onRoleNameChange(role.id, name) },
                            onCountChange = { count -> onRoleCountChange(role.id, count) },
                            onRemove = { onRoleRemoved(role.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        FormSection(
            title = stringResource(R.string.recruitment_modify_description),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_modify_roles_count,
                        state.description.length,
                        DESCRIPTION_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                hint = stringResource(R.string.recruitment_modify_description_hint),
                singleLine = false,
                minLines = 5,
                maxLength = DESCRIPTION_MAX_LENGTH
            )
        }

        FormSection(title = stringResource(R.string.recruitment_modify_related_url)) {
            RecruitmentTextField(
                value = state.relatedUrl,
                onValueChange = onRelatedUrlChange,
                hint = stringResource(R.string.recruitment_modify_related_url_hint)
            )
        }

        FormSection(
            title = stringResource(R.string.recruitment_modify_qualification),
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_modify_roles_count,
                        state.qualification.length,
                        QUALIFICATION_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.qualification,
                onValueChange = onQualificationChange,
                hint = stringResource(R.string.recruitment_modify_qualification_hint),
                singleLine = false,
                minLines = 3,
                maxLength = QUALIFICATION_MAX_LENGTH
            )
        }

        FilledButton(
            text = stringResource(R.string.recruitment_modify_submit),
            enabled = state.isSubmitEnabled && !state.isSubmitting,
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun FormSection(
    title: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    titleSuffix: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                if (isRequired) {
                    Text(
                        text = " *",
                        style = RebrandKoinTheme.typography.medium16,
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
                if (titleSuffix != null) {
                    Text(
                        text = "  $titleSuffix",
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
            trailingContent?.invoke()
        }
        content()
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun RecruitmentModifyScreenPreview() {
    RebrandKoinTheme {
        RecruitmentModifyScreenImpl(
            state = RecruitmentModifyState(
                category = RecruitmentCategory.PROJECT,
                roles = persistentListOf(RecruitmentModifyRole("프론트엔드", 2), RecruitmentModifyRole("디자인", 1))
            )
        )
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun RecruitmentModifyScreenRoleUndeterminedPreview() {
    RebrandKoinTheme {
        RecruitmentModifyScreenImpl(
            state = RecruitmentModifyState(
                category = RecruitmentCategory.PROJECT,
                isRoleCountUndetermined = true,
                participantCount = 5
            )
        )
    }
}
