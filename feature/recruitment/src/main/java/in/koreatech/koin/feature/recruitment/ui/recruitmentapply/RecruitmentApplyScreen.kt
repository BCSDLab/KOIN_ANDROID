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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRoleOption
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentActivitiesSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentConfirmDialog
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDepartmentSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilledActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFormSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentLoadMemberInfoSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentNicknameSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentSelfIntroductionSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentSkillsSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentStepIndicator
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentStudentIdSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentTextField
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Stable
data class RecruitmentApplyStepOneActions(
    val onLoadMemberInfoClick: () -> Unit = {},
    val onNicknameChange: (String) -> Unit = {},
    val onAgeChange: (String) -> Unit = {},
    val onDepartmentDropdownExpandChange: (Boolean) -> Unit = {},
    val onDepartmentSelected: (String) -> Unit = {},
    val onStudentIdChange: (String) -> Unit = {},
    val onAddSkillClick: () -> Unit = {},
    val onSkillTextChange: (Long, String) -> Unit = { _, _ -> },
    val onSkillRemoved: (Long) -> Unit = {},
    val onAddActivityClick: () -> Unit = {},
    val onEditActivityClick: (RecruitmentActivityEntry) -> Unit = {},
    val onCancelActivityForm: () -> Unit = {},
    val onActivityAdded: (RecruitmentActivityEntry) -> Unit = {},
    val onActivityEdited: (RecruitmentActivityEntry) -> Unit = {},
    val onActivityRemoved: (RecruitmentActivityEntry) -> Unit = {},
    val onSelfIntroductionChange: (String) -> Unit = {}
)

@Stable
data class RecruitmentApplyStepTwoActions(
    val onRoleSelected: (TeamRecruitmentRoleOption) -> Unit = {},
    val onMotivationChange: (String) -> Unit = {},
    val onAvailableTimeChange: (String) -> Unit = {}
)

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

    val stepOneActions = remember {
        RecruitmentApplyStepOneActions(
            onLoadMemberInfoClick = { viewModel.loadMemberInfo() },
            onNicknameChange = { viewModel.setNickname(it) },
            onAgeChange = { viewModel.setAge(it) },
            onDepartmentDropdownExpandChange = { viewModel.setDepartmentDropdownExpanded(it) },
            onDepartmentSelected = { viewModel.setDepartment(it) },
            onStudentIdChange = { viewModel.setStudentId(it) },
            onAddSkillClick = { viewModel.addSkill() },
            onSkillTextChange = { id, text -> viewModel.setSkillText(id, text) },
            onSkillRemoved = { viewModel.removeSkill(it) },
            onAddActivityClick = { viewModel.showActivityAddForm() },
            onEditActivityClick = { viewModel.showActivityEditForm(it) },
            onCancelActivityForm = { viewModel.hideActivityForm() },
            onActivityAdded = { viewModel.addActivity(it) },
            onActivityEdited = { viewModel.editActivity(it) },
            onActivityRemoved = { viewModel.removeActivity(it) },
            onSelfIntroductionChange = { viewModel.setSelfIntroduction(it) }
        )
    }

    val stepTwoActions = remember {
        RecruitmentApplyStepTwoActions(
            onRoleSelected = { viewModel.selectRole(it) },
            onMotivationChange = { viewModel.setMotivation(it) },
            onAvailableTimeChange = { viewModel.setAvailableTime(it) }
        )
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
            stepOneActions = stepOneActions,
            stepTwoActions = stepTwoActions,
            onNextStepClick = { viewModel.goToNextStep() },
            onSubmitClick = { viewModel.showSubmitConfirmDialog() },
            onDismissSubmitConfirmDialog = { viewModel.dismissSubmitConfirmDialog() },
            onConfirmSubmit = { viewModel.submitApplication() },
            onDismissCancelConfirmDialog = { viewModel.dismissCancelConfirmDialog() },
            onConfirmCancel = { viewModel.confirmCancel() }
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun RecruitmentApplyScreenImpl(
    state: RecruitmentApplyState,
    modifier: Modifier = Modifier,
    stepOneActions: RecruitmentApplyStepOneActions = RecruitmentApplyStepOneActions(),
    stepTwoActions: RecruitmentApplyStepTwoActions = RecruitmentApplyStepTwoActions(),
    onNextStepClick: () -> Unit = {},
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
                    actions = stepOneActions
                )
            } else {
                RecruitmentApplyStepTwo(
                    state = state,
                    actions = stepTwoActions
                )
            }
        }

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.danger700,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
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

@Composable
private fun RecruitmentApplyStepOne(
    state: RecruitmentApplyState,
    actions: RecruitmentApplyStepOneActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        RecruitmentLoadMemberInfoSection(onLoadMemberInfoClick = actions.onLoadMemberInfoClick)

        RecruitmentNicknameSection(
            nickname = state.nickname,
            onNicknameChange = actions.onNicknameChange,
            maxLength = NICKNAME_MAX_LENGTH
        )

        RecruitmentFormSection(
            title = stringResource(R.string.recruitment_apply_age),
            isRequired = true,
            content = {
                RecruitmentTextField(
                    value = state.age,
                    onValueChange = actions.onAgeChange,
                    hint = stringResource(R.string.recruitment_apply_age_hint),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )

        RecruitmentDepartmentSection(
            department = state.department,
            departments = state.departments,
            isDropdownExpanded = state.isDepartmentDropdownExpanded,
            onDropdownExpandChange = actions.onDepartmentDropdownExpandChange,
            onDepartmentSelected = actions.onDepartmentSelected
        )

        RecruitmentStudentIdSection(
            studentId = state.studentId,
            onStudentIdChange = actions.onStudentIdChange
        )

        RecruitmentSkillsSection(
            skills = state.skills,
            onSkillTextChange = actions.onSkillTextChange,
            onSkillRemoved = actions.onSkillRemoved,
            onAddSkillClick = actions.onAddSkillClick
        )

        RecruitmentActivitiesSection(
            activities = state.activities,
            isAddingActivity = state.activityFormState is ActivityFormState.Adding,
            editingActivityId = (state.activityFormState as? ActivityFormState.Editing)?.activityId,
            onAddActivityClick = actions.onAddActivityClick,
            onEditActivityClick = actions.onEditActivityClick,
            onCancelActivityForm = actions.onCancelActivityForm,
            onActivityAdded = actions.onActivityAdded,
            onActivityEdited = actions.onActivityEdited,
            onActivityRemoved = actions.onActivityRemoved
        )

        RecruitmentSelfIntroductionSection(
            selfIntroduction = state.selfIntroduction,
            onSelfIntroductionChange = actions.onSelfIntroductionChange,
            maxLength = SELF_INTRODUCTION_MAX_LENGTH
        )
    }
}

@Composable
private fun RecruitmentApplyStepTwo(
    state: RecruitmentApplyState,
    actions: RecruitmentApplyStepTwoActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        RecruitmentFormSection(
            title = stringResource(R.string.recruitment_apply_select_role),
            isRequired = true,
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.availableRoles.forEach { role ->
                        key(role.id) {
                            RecruitmentRoleRadioItem(
                                role = role,
                                isSelected = state.selectedRole == role,
                                onClick = { actions.onRoleSelected(role) }
                            )
                        }
                    }
                }
            }
        )

        RecruitmentFormSection(
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
            },
            content = {
                RecruitmentTextField(
                    value = state.motivation,
                    onValueChange = actions.onMotivationChange,
                    hint = stringResource(R.string.recruitment_apply_motivation_hint),
                    singleLine = false,
                    minLines = 5,
                    maxLength = MOTIVATION_MAX_LENGTH
                )
            }
        )

        RecruitmentFormSection(
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
            },
            content = {
                RecruitmentTextField(
                    value = state.availableTime,
                    onValueChange = actions.onAvailableTimeChange,
                    hint = stringResource(R.string.recruitment_apply_available_time_hint),
                    singleLine = false,
                    minLines = 2,
                    maxLength = AVAILABLE_TIME_MAX_LENGTH
                )
            }
        )
    }
}

@Composable
private fun RecruitmentRoleRadioItem(
    role: TeamRecruitmentRoleOption,
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
        RecruitmentApplyScreenImpl(
            state = RecruitmentApplyState(
                currentStep = 2,
                availableRoles = persistentListOf(
                    TeamRecruitmentRoleOption(id = 1, name = "프론트엔드"),
                    TeamRecruitmentRoleOption(id = 2, name = "백엔드"),
                    TeamRecruitmentRoleOption(id = 3, name = "디자인", isClosed = true)
                )
            )
        )
    }
}
