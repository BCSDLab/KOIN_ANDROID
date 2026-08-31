package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentActivityForm
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentConfirmDialog
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDropdown
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilledActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentOutlinedActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentSkillFieldRow
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentTextField
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.component.RecruitmentActivityCard
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.component.RecruitmentStepIndicator
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private const val SELF_INTRODUCTION_MAX_LENGTH = 1000
private const val MOTIVATION_MAX_LENGTH = 1000
private const val AVAILABLE_TIME_MAX_LENGTH = 100

private val DEPARTMENTS = persistentListOf("컴퓨터공학부", "전전통", "고용", "산경", "등등..")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentApplyScreen(
    modifier: Modifier = Modifier,
    viewModel: RecruitmentApplyViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onApplySuccess: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            RecruitmentApplySideEffect.NavigateUp -> onNavigateUp()
            RecruitmentApplySideEffect.ApplySuccess -> onApplySuccess()
            RecruitmentApplySideEffect.ApplyFailure -> Unit
        }
    }

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_apply_title),
                onNavigationIconClick = {
                    if (state.currentStep == 1) viewModel.showCancelConfirmDialog() else viewModel.goToPreviousStep()
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { contentPadding ->
        RecruitmentApplyScreenImpl(
            state = state,
            modifier = Modifier.padding(contentPadding),
            onLoadMemberInfoClick = viewModel::loadMemberInfo,
            onNicknameChange = viewModel::setNickname,
            onAgeChange = viewModel::setAge,
            onDepartmentDropdownExpandChange = viewModel::setDepartmentDropdownExpanded,
            onDepartmentSelected = viewModel::setDepartment,
            onStudentIdChange = viewModel::setStudentId,
            onAddSkillClick = viewModel::addSkill,
            onSkillTextChange = viewModel::setSkillText,
            onSkillRemoved = viewModel::removeSkill,
            onAddActivityClick = viewModel::showActivityAddForm,
            onEditActivityClick = viewModel::showActivityEditForm,
            onCancelActivityForm = viewModel::hideActivityForm,
            onActivityAdded = viewModel::addActivity,
            onActivityEdited = viewModel::editActivity,
            onActivityRemoved = viewModel::removeActivity,
            onSelfIntroductionChange = viewModel::setSelfIntroduction,
            onNextStepClick = viewModel::goToNextStep,
            onRoleSelected = viewModel::selectRole,
            onMotivationChange = viewModel::setMotivation,
            onAvailableTimeChange = viewModel::setAvailableTime,
            onSubmitClick = viewModel::showSubmitConfirmDialog,
            onDismissSubmitConfirmDialog = viewModel::dismissSubmitConfirmDialog,
            onConfirmSubmit = viewModel::submitApplication,
            onDismissCancelConfirmDialog = viewModel::dismissCancelConfirmDialog,
            onConfirmCancel = viewModel::confirmCancel
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun RecruitmentApplyScreenImpl(
    state: RecruitmentApplyState,
    modifier: Modifier = Modifier,
    onLoadMemberInfoClick: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onAgeChange: (String) -> Unit = {},
    onDepartmentDropdownExpandChange: (Boolean) -> Unit = {},
    onDepartmentSelected: (String) -> Unit = {},
    onStudentIdChange: (String) -> Unit = {},
    onAddSkillClick: () -> Unit = {},
    onSkillTextChange: (Int, String) -> Unit = { _, _ -> },
    onSkillRemoved: (Int) -> Unit = {},
    onAddActivityClick: () -> Unit = {},
    onEditActivityClick: (RecruitmentActivityEntry) -> Unit = {},
    onCancelActivityForm: () -> Unit = {},
    onActivityAdded: (RecruitmentActivityEntry) -> Unit = {},
    onActivityEdited: (RecruitmentActivityEntry) -> Unit = {},
    onActivityRemoved: (RecruitmentActivityEntry) -> Unit = {},
    onSelfIntroductionChange: (String) -> Unit = {},
    onNextStepClick: () -> Unit = {},
    onRoleSelected: (TeamRecruitmentRole) -> Unit = {},
    onMotivationChange: (String) -> Unit = {},
    onAvailableTimeChange: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onDismissSubmitConfirmDialog: () -> Unit = {},
    onConfirmSubmit: () -> Unit = {},
    onDismissCancelConfirmDialog: () -> Unit = {},
    onConfirmCancel: () -> Unit = {}
) {
    if (state.showSubmitConfirmDialog) {
        RecruitmentConfirmDialog(
            title = stringResource(R.string.recruitment_apply_submit_dialog_title),
            positiveButtonText = stringResource(R.string.recruitment_apply_submit),
            negativeButtonText = stringResource(R.string.recruitment_apply_submit_dialog_cancel),
            onPositive = onConfirmSubmit,
            onNegative = onDismissSubmitConfirmDialog
        )
    }

    if (state.showCancelConfirmDialog) {
        RecruitmentConfirmDialog(
            title = stringResource(R.string.recruitment_apply_cancel_dialog_title),
            description = stringResource(R.string.recruitment_apply_cancel_dialog_description),
            positiveButtonText = stringResource(R.string.recruitment_create_dialog_yes),
            negativeButtonText = stringResource(R.string.recruitment_create_dialog_no),
            onPositive = onConfirmCancel,
            onNegative = onDismissCancelConfirmDialog
        )
    }

    Column(modifier = modifier) {
        RecruitmentStepIndicator(
            currentStep = state.currentStep,
            totalSteps = RECRUITMENT_APPLY_STEP_COUNT,
            stepLabels = persistentListOf(
                stringResource(R.string.recruitment_apply_step_one_label),
                stringResource(R.string.recruitment_apply_step_two_label)
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            if (state.currentStep == 1) {
                RecruitmentApplyStepOne(
                    state = state,
                    onLoadMemberInfoClick = onLoadMemberInfoClick,
                    onNicknameChange = onNicknameChange,
                    onAgeChange = onAgeChange,
                    onDepartmentDropdownExpandChange = onDepartmentDropdownExpandChange,
                    onDepartmentSelected = onDepartmentSelected,
                    onStudentIdChange = onStudentIdChange,
                    onAddSkillClick = onAddSkillClick,
                    onSkillTextChange = onSkillTextChange,
                    onSkillRemoved = onSkillRemoved,
                    onAddActivityClick = onAddActivityClick,
                    onEditActivityClick = onEditActivityClick,
                    onCancelActivityForm = onCancelActivityForm,
                    onActivityAdded = onActivityAdded,
                    onActivityEdited = onActivityEdited,
                    onActivityRemoved = onActivityRemoved,
                    onSelfIntroductionChange = onSelfIntroductionChange
                )
            } else {
                RecruitmentApplyStepTwo(
                    state = state,
                    onRoleSelected = onRoleSelected,
                    onMotivationChange = onMotivationChange,
                    onAvailableTimeChange = onAvailableTimeChange
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.currentStep == 1) {
                RecruitmentFilledActionButton(
                    text = stringResource(R.string.recruitment_apply_next),
                    enabled = state.isStepOneValid,
                    onClick = onNextStepClick
                )
            } else {
                RecruitmentFilledActionButton(
                    text = stringResource(R.string.recruitment_apply_submit),
                    enabled = state.isSubmitEnabled && !state.isSubmitting,
                    onClick = onSubmitClick
                )
            }
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun RecruitmentApplyStepOne(
    state: RecruitmentApplyState,
    modifier: Modifier = Modifier,
    onLoadMemberInfoClick: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onAgeChange: (String) -> Unit = {},
    onDepartmentDropdownExpandChange: (Boolean) -> Unit = {},
    onDepartmentSelected: (String) -> Unit = {},
    onStudentIdChange: (String) -> Unit = {},
    onAddSkillClick: () -> Unit = {},
    onSkillTextChange: (Int, String) -> Unit = { _, _ -> },
    onSkillRemoved: (Int) -> Unit = {},
    onAddActivityClick: () -> Unit = {},
    onEditActivityClick: (RecruitmentActivityEntry) -> Unit = {},
    onCancelActivityForm: () -> Unit = {},
    onActivityAdded: (RecruitmentActivityEntry) -> Unit = {},
    onActivityEdited: (RecruitmentActivityEntry) -> Unit = {},
    onActivityRemoved: (RecruitmentActivityEntry) -> Unit = {},
    onSelfIntroductionChange: (String) -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        FormSection(
            title = stringResource(R.string.recruitment_apply_load_member_info),
            titleHint = stringResource(R.string.recruitment_apply_load_member_info_hint)
        ) {
            RecruitmentOutlinedActionButton(
                text = stringResource(R.string.recruitment_apply_load_member_info_button),
                onClick = onLoadMemberInfoClick
            )
        }

        FormSection(
            title = stringResource(R.string.recruitment_apply_nickname),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_apply_char_count,
                        state.nickname.length,
                        NICKNAME_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.nickname,
                onValueChange = onNicknameChange,
                hint = stringResource(R.string.recruitment_apply_nickname_hint),
                maxLength = NICKNAME_MAX_LENGTH
            )
        }

        FormSection(title = stringResource(R.string.recruitment_apply_age), isRequired = true) {
            RecruitmentTextField(
                value = state.age,
                onValueChange = onAgeChange,
                hint = stringResource(R.string.recruitment_apply_age_hint)
            )
        }

        FormSection(title = stringResource(R.string.recruitment_apply_department), isRequired = true) {
            RecruitmentDropdown(
                text = state.department.ifEmpty { stringResource(R.string.recruitment_apply_department_hint) },
                isPlaceholder = state.department.isBlank(),
                items = DEPARTMENTS,
                isExpanded = state.isDepartmentDropdownExpanded,
                onExpandedChange = onDepartmentDropdownExpandChange,
                onItemSelected = { index -> onDepartmentSelected(DEPARTMENTS[index]) }
            )
        }

        FormSection(title = stringResource(R.string.recruitment_apply_student_id), isRequired = true) {
            RecruitmentTextField(
                value = state.studentId,
                onValueChange = onStudentIdChange,
                hint = stringResource(R.string.recruitment_apply_student_id_hint)
            )
        }

        FormSection(
            title = stringResource(R.string.recruitment_apply_skills),
            titleHint = stringResource(R.string.recruitment_apply_skills_hint)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.skills.forEachIndexed { index, skill ->
                    RecruitmentSkillFieldRow(
                        value = skill,
                        onValueChange = { text -> onSkillTextChange(index, text) },
                        onRemove = { onSkillRemoved(index) }
                    )
                }
                RecruitmentOutlinedActionButton(
                    text = stringResource(R.string.recruitment_apply_add_skill),
                    onClick = onAddSkillClick
                )
            }
        }

        FormSection(
            title = stringResource(R.string.recruitment_apply_activities),
            titleHint = stringResource(R.string.recruitment_apply_activities_hint)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.activities.forEach { activity ->
                    key(activity.id) {
                        val editState = state.activityFormState
                        if (editState is ActivityFormState.Editing && editState.activityId == activity.id) {
                            RecruitmentActivityForm(
                                onCancel = onCancelActivityForm,
                                onConfirm = onActivityEdited,
                                existingActivity = activity
                            )
                        } else {
                            RecruitmentActivityCard(
                                activity = activity,
                                onRemove = { onActivityRemoved(activity) },
                                onEdit = { onEditActivityClick(activity) }
                            )
                        }
                    }
                }
                if (state.activityFormState is ActivityFormState.Adding) {
                    RecruitmentActivityForm(
                        onCancel = onCancelActivityForm,
                        onConfirm = onActivityAdded
                    )
                }
                RecruitmentOutlinedActionButton(
                    text = stringResource(R.string.recruitment_apply_add_activity),
                    onClick = onAddActivityClick
                )
            }
        }

        FormSection(
            title = stringResource(R.string.recruitment_apply_self_introduction),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_apply_char_count,
                        state.selfIntroduction.length,
                        SELF_INTRODUCTION_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.selfIntroduction,
                onValueChange = onSelfIntroductionChange,
                hint = stringResource(R.string.recruitment_apply_self_introduction_hint),
                singleLine = false,
                minLines = 6,
                maxLength = SELF_INTRODUCTION_MAX_LENGTH
            )
        }
    }
}

@Composable
private fun RecruitmentApplyStepTwo(
    state: RecruitmentApplyState,
    modifier: Modifier = Modifier,
    onRoleSelected: (TeamRecruitmentRole) -> Unit = {},
    onMotivationChange: (String) -> Unit = {},
    onAvailableTimeChange: (String) -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        FormSection(title = stringResource(R.string.recruitment_apply_select_role), isRequired = true) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val roles = state.availableRoles.ifEmpty {
                    persistentListOf(
                        TeamRecruitmentRole("프론트엔드", 1),
                        TeamRecruitmentRole("백엔드", 1),
                        TeamRecruitmentRole("디자인", 1, isClosed = true)
                    )
                }
                roles.forEach { role ->
                    key(role.id) {
                        RecruitmentRoleRadioItem(
                            role = role,
                            isSelected = state.selectedRole == role,
                            onClick = { onRoleSelected(role) }
                        )
                    }
                }
            }
        }

        FormSection(
            title = stringResource(R.string.recruitment_apply_motivation),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_apply_char_count,
                        state.motivation.length,
                        MOTIVATION_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.motivation,
                onValueChange = onMotivationChange,
                hint = stringResource(R.string.recruitment_apply_motivation_hint),
                singleLine = false,
                minLines = 5,
                maxLength = MOTIVATION_MAX_LENGTH
            )
        }

        FormSection(
            title = stringResource(R.string.recruitment_apply_available_time),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_apply_char_count,
                        state.availableTime.length,
                        AVAILABLE_TIME_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
        ) {
            RecruitmentTextField(
                value = state.availableTime,
                onValueChange = onAvailableTimeChange,
                hint = stringResource(R.string.recruitment_apply_available_time_hint),
                singleLine = false,
                minLines = 2,
                maxLength = AVAILABLE_TIME_MAX_LENGTH
            )
        }
    }
}

@Composable
private fun RecruitmentRoleRadioItem(
    role: TeamRecruitmentRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = if (role.isClosed) RebrandKoinTheme.colors.neutral100 else RebrandKoinTheme.colors.neutral0,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = !role.isClosed) { onClick() }
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                enabled = !role.isClosed,
                colors = RadioButtonDefaults.colors(selectedColor = RebrandKoinTheme.colors.primary500)
            )
            Text(
                text = role.name,
                style = RebrandKoinTheme.typography.regular15,
                color = if (role.isClosed) RebrandKoinTheme.colors.neutral400 else RebrandKoinTheme.colors.neutral800
            )
        }
        if (role.isClosed) {
            Text(
                text = stringResource(R.string.recruitment_apply_role_closed),
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral400
            )
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    titleHint: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                if (titleHint != null) {
                    Text(
                        text = "  $titleHint",
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
            trailingContent?.invoke()
        }
        content()
    }
}

@Preview(showBackground = true, heightDp = 1150)
@Composable
private fun RecruitmentApplyScreenStepOnePreview() {
    RebrandKoinTheme {
        RecruitmentApplyScreenImpl(state = RecruitmentApplyState(currentStep = 1))
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentApplyScreenStepTwoPreview() {
    RebrandKoinTheme {
        RecruitmentApplyScreenImpl(state = RecruitmentApplyState(currentStep = 2))
    }
}
