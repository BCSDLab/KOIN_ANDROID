package `in`.koreatech.koin.feature.recruitment.ui.profilecreate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentActivitiesSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentConfirmDialog
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDepartmentSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilledActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFormSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentLoadMemberInfoSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentNicknameSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentOutlinedActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentSelfIntroductionSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentSkillsSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentStepIndicator
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentStudentIdSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentTextField
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private const val SELF_INTRODUCTION_MAX_LENGTH = 1000

data class ProfileCreateStepOneActions(
    val onLoadMemberInfoClick: () -> Unit = {},
    val onNicknameChange: (String) -> Unit = {},
    val onDepartmentDropdownExpandChange: (Boolean) -> Unit = {},
    val onDepartmentSelected: (String) -> Unit = {},
    val onStudentIdChange: (String) -> Unit = {}
)

data class ProfileCreateStepTwoActions(
    val onPreferredRoleChange: (String) -> Unit = {},
    val onAddSkillClick: () -> Unit = {},
    val onSkillTextChange: (Int, String) -> Unit = { _, _ -> },
    val onSkillRemoved: (Int) -> Unit = {},
    val onAddActivityClick: () -> Unit = {},
    val onEditActivityClick: (RecruitmentActivityEntry) -> Unit = {},
    val onCancelActivityForm: () -> Unit = {},
    val onActivityAdded: (RecruitmentActivityEntry) -> Unit = {},
    val onActivityEdited: (RecruitmentActivityEntry) -> Unit = {},
    val onActivityRemoved: (RecruitmentActivityEntry) -> Unit = {},
    val onSelfIntroductionChange: (String) -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreateScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileCreateViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ProfileCreateSideEffect.NavigateUp -> onNavigateUp()
            ProfileCreateSideEffect.SaveSuccess -> onSaveSuccess()
            ProfileCreateSideEffect.SaveFailure -> Unit
        }
    }

    val stepOneActions = remember {
        ProfileCreateStepOneActions(
            onLoadMemberInfoClick = { viewModel.loadMemberInfo() },
            onNicknameChange = { viewModel.setNickname(it) },
            onDepartmentDropdownExpandChange = { viewModel.setDepartmentDropdownExpanded(it) },
            onDepartmentSelected = { viewModel.setDepartment(it) },
            onStudentIdChange = { viewModel.setStudentId(it) }
        )
    }

    val stepTwoActions = remember {
        ProfileCreateStepTwoActions(
            onPreferredRoleChange = { viewModel.setPreferredRole(it) },
            onAddSkillClick = { viewModel.addSkill() },
            onSkillTextChange = { index, text -> viewModel.setSkillText(index, text) },
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

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = if (state.isEditMode) {
                    stringResource(R.string.recruitment_profile_create_edit_title)
                } else {
                    stringResource(R.string.recruitment_profile_create_title)
                },
                onNavigationIconClick = {
                    if (state.currentStep == 1) viewModel.showCancelConfirmDialog() else viewModel.goToPreviousStep()
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { contentPadding ->
        ProfileCreateScreenImpl(
            state = state,
            modifier = Modifier.padding(contentPadding),
            stepOneActions = stepOneActions,
            stepTwoActions = stepTwoActions,
            onNextStepClick = { viewModel.goToNextStep() },
            onPreviousStepClick = { viewModel.goToPreviousStep() },
            onSaveClick = { viewModel.showSaveConfirmDialog() },
            onDismissSaveConfirmDialog = { viewModel.dismissSaveConfirmDialog() },
            onConfirmSave = { viewModel.saveProfile() },
            onDismissCancelConfirmDialog = { viewModel.dismissCancelConfirmDialog() },
            onConfirmCancel = { viewModel.confirmCancel() }
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun ProfileCreateScreenImpl(
    state: ProfileCreateState,
    modifier: Modifier = Modifier,
    stepOneActions: ProfileCreateStepOneActions = ProfileCreateStepOneActions(),
    stepTwoActions: ProfileCreateStepTwoActions = ProfileCreateStepTwoActions(),
    onNextStepClick: () -> Unit = {},
    onPreviousStepClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDismissSaveConfirmDialog: () -> Unit = {},
    onConfirmSave: () -> Unit = {},
    onDismissCancelConfirmDialog: () -> Unit = {},
    onConfirmCancel: () -> Unit = {}
) {
    if (state.showSaveConfirmDialog) {
        RecruitmentConfirmDialog(
            title = stringResource(R.string.recruitment_profile_save_dialog_title),
            positiveButtonText = if (state.isEditMode) {
                stringResource(R.string.recruitment_profile_update)
            } else {
                stringResource(R.string.recruitment_profile_save)
            },
            negativeButtonText = stringResource(R.string.recruitment_create_dialog_no),
            onPositive = onConfirmSave,
            onNegative = onDismissSaveConfirmDialog
        )
    }

    if (state.showCancelConfirmDialog) {
        RecruitmentConfirmDialog(
            title = stringResource(R.string.recruitment_profile_cancel_dialog_title),
            description = stringResource(R.string.recruitment_profile_cancel_dialog_description),
            positiveButtonText = stringResource(R.string.recruitment_create_dialog_yes),
            negativeButtonText = stringResource(R.string.recruitment_create_dialog_no),
            onPositive = onConfirmCancel,
            onNegative = onDismissCancelConfirmDialog
        )
    }

    Column(modifier = modifier) {
        RecruitmentStepIndicator(
            currentStep = state.currentStep,
            totalSteps = PROFILE_CREATE_STEP_COUNT,
            stepLabels = persistentListOf(
                stringResource(R.string.recruitment_profile_create_step_one_label),
                stringResource(R.string.recruitment_profile_create_step_two_label)
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
                ProfileCreateStepOne(
                    state = state,
                    actions = stepOneActions
                )
            } else {
                ProfileCreateStepTwo(
                    state = state,
                    actions = stepTwoActions
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (state.currentStep == 1) {
                RecruitmentFilledActionButton(
                    text = stringResource(R.string.recruitment_apply_next),
                    enabled = state.isStepOneValid,
                    onClick = onNextStepClick
                )
            } else {
                val saveText = if (state.isEditMode) {
                    stringResource(R.string.recruitment_profile_update)
                } else {
                    stringResource(R.string.recruitment_profile_save)
                }
                RecruitmentOutlinedActionButton(
                    text = stringResource(R.string.recruitment_profile_previous),
                    onClick = onPreviousStepClick,
                    modifier = Modifier.weight(1f),
                    height = 48.dp,
                    contentPadding = PaddingValues(top = 8.dp, end = 24.dp, bottom = 8.dp, start = 24.dp)
                )
                RecruitmentFilledActionButton(
                    text = saveText,
                    enabled = state.isSaveEnabled && !state.isSaving,
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ProfileCreateStepOne(
    state: ProfileCreateState,
    actions: ProfileCreateStepOneActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        RecruitmentLoadMemberInfoSection(onLoadMemberInfoClick = actions.onLoadMemberInfoClick)

        RecruitmentNicknameSection(
            nickname = state.nickname,
            onNicknameChange = actions.onNicknameChange,
            maxLength = PROFILE_NICKNAME_MAX_LENGTH
        )

        RecruitmentDepartmentSection(
            department = state.department,
            isDropdownExpanded = state.isDepartmentDropdownExpanded,
            onDropdownExpandChange = actions.onDepartmentDropdownExpandChange,
            onDepartmentSelected = actions.onDepartmentSelected
        )

        RecruitmentStudentIdSection(
            studentId = state.studentId,
            onStudentIdChange = actions.onStudentIdChange
        )
    }
}

@Composable
private fun ProfileCreateStepTwo(
    state: ProfileCreateState,
    actions: ProfileCreateStepTwoActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        RecruitmentFormSection(
            title = stringResource(R.string.recruitment_profile_preferred_role),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_apply_char_count,
                        state.preferredRole.length,
                        PROFILE_PREFERRED_ROLE_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            },
            content = {
                RecruitmentTextField(
                    value = state.preferredRole,
                    onValueChange = actions.onPreferredRoleChange,
                    hint = stringResource(R.string.recruitment_profile_preferred_role_hint),
                    maxLength = PROFILE_PREFERRED_ROLE_MAX_LENGTH
                )
            }
        )

        RecruitmentSkillsSection(
            skills = state.skills,
            onSkillTextChange = actions.onSkillTextChange,
            onSkillRemoved = actions.onSkillRemoved,
            onAddSkillClick = actions.onAddSkillClick
        )

        RecruitmentActivitiesSection(
            activities = state.activities,
            isAddingActivity = state.activityFormState is ProfileActivityFormState.Adding,
            editingActivityId = (state.activityFormState as? ProfileActivityFormState.Editing)?.activityId,
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

@Preview(showBackground = true)
@Composable
private fun ProfileCreateScreenStepOnePreview() {
    RebrandKoinTheme {
        ProfileCreateScreenImpl(state = ProfileCreateState(currentStep = 1))
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileCreateScreenStepTwoPreview() {
    RebrandKoinTheme {
        ProfileCreateScreenImpl(state = ProfileCreateState(currentStep = 2))
    }
}
