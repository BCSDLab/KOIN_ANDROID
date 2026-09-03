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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentActivityCard
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentActivityForm
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentConfirmDialog
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDropdown
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilledActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentOutlinedActionButton
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentSkillFieldRow
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentStepIndicator
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentTextField
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private const val SELF_INTRODUCTION_MAX_LENGTH = 1000

private val DEPARTMENTS = persistentListOf("컴퓨터공학부", "전전통", "고용", "산경", "등등..")

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
            onLoadMemberInfoClick = viewModel::loadMemberInfo,
            onNicknameChange = viewModel::setNickname,
            onDepartmentDropdownExpandChange = viewModel::setDepartmentDropdownExpanded,
            onDepartmentSelected = viewModel::setDepartment,
            onStudentIdChange = viewModel::setStudentId,
            onPreferredRoleChange = viewModel::setPreferredRole,
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
            onPreviousStepClick = viewModel::goToPreviousStep,
            onSaveClick = viewModel::showSaveConfirmDialog,
            onDismissSaveConfirmDialog = viewModel::dismissSaveConfirmDialog,
            onConfirmSave = viewModel::saveProfile,
            onDismissCancelConfirmDialog = viewModel::dismissCancelConfirmDialog,
            onConfirmCancel = viewModel::confirmCancel
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun ProfileCreateScreenImpl(
    state: ProfileCreateState,
    modifier: Modifier = Modifier,
    onLoadMemberInfoClick: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onDepartmentDropdownExpandChange: (Boolean) -> Unit = {},
    onDepartmentSelected: (String) -> Unit = {},
    onStudentIdChange: (String) -> Unit = {},
    onPreferredRoleChange: (String) -> Unit = {},
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
                    onLoadMemberInfoClick = onLoadMemberInfoClick,
                    onNicknameChange = onNicknameChange,
                    onDepartmentDropdownExpandChange = onDepartmentDropdownExpandChange,
                    onDepartmentSelected = onDepartmentSelected,
                    onStudentIdChange = onStudentIdChange
                )
            } else {
                ProfileCreateStepTwo(
                    state = state,
                    onPreferredRoleChange = onPreferredRoleChange,
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
    modifier: Modifier = Modifier,
    onLoadMemberInfoClick: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onDepartmentDropdownExpandChange: (Boolean) -> Unit = {},
    onDepartmentSelected: (String) -> Unit = {},
    onStudentIdChange: (String) -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        FormSection(
            title = stringResource(R.string.recruitment_apply_load_member_info),
            titleHint = stringResource(R.string.recruitment_apply_load_member_info_hint),
            content = {
                RecruitmentOutlinedActionButton(
                    text = stringResource(R.string.recruitment_apply_load_member_info_button),
                    onClick = onLoadMemberInfoClick
                )
            }
        )

        FormSection(
            title = stringResource(R.string.recruitment_apply_nickname),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_profile_char_count,
                        state.nickname.length,
                        PROFILE_NICKNAME_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            },
            content = {
                RecruitmentTextField(
                    value = state.nickname,
                    onValueChange = onNicknameChange,
                    hint = stringResource(R.string.recruitment_apply_nickname_hint),
                    maxLength = PROFILE_NICKNAME_MAX_LENGTH
                )
            }
        )

        FormSection(
            title = stringResource(R.string.recruitment_apply_department),
            isRequired = true,
            content = {
                RecruitmentDropdown(
                    text = state.department.ifEmpty { stringResource(R.string.recruitment_apply_department_hint) },
                    isPlaceholder = state.department.isBlank(),
                    items = DEPARTMENTS,
                    isExpanded = state.isDepartmentDropdownExpanded,
                    onExpandedChange = onDepartmentDropdownExpandChange,
                    onItemSelected = { index -> onDepartmentSelected(DEPARTMENTS[index]) }
                )
            }
        )

        FormSection(
            title = stringResource(R.string.recruitment_apply_student_id),
            isRequired = true,
            content = {
                RecruitmentTextField(
                    value = state.studentId,
                    onValueChange = onStudentIdChange,
                    hint = stringResource(R.string.recruitment_apply_student_id_hint)
                )
            }
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun ProfileCreateStepTwo(
    state: ProfileCreateState,
    modifier: Modifier = Modifier,
    onPreferredRoleChange: (String) -> Unit = {},
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
            title = stringResource(R.string.recruitment_profile_preferred_role),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_profile_char_count,
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
                    onValueChange = onPreferredRoleChange,
                    hint = stringResource(R.string.recruitment_profile_preferred_role_hint),
                    maxLength = PROFILE_PREFERRED_ROLE_MAX_LENGTH
                )
            }
        )

        FormSection(
            title = stringResource(R.string.recruitment_apply_skills),
            titleHint = stringResource(R.string.recruitment_apply_skills_hint),
            content = {
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
        )

        FormSection(
            title = stringResource(R.string.recruitment_apply_activities),
            titleHint = stringResource(R.string.recruitment_apply_activities_hint),
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.activities.forEach { activity ->
                        val formState = state.activityFormState
                        if (formState is ProfileActivityFormState.Editing && formState.activityId == activity.id) {
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
                    if (state.activityFormState is ProfileActivityFormState.Adding) {
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
        )

        FormSection(
            title = stringResource(R.string.recruitment_apply_self_introduction),
            isRequired = true,
            trailingContent = {
                Text(
                    text = stringResource(
                        R.string.recruitment_profile_char_count,
                        state.selfIntroduction.length,
                        SELF_INTRODUCTION_MAX_LENGTH
                    ),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral400
                )
            },
            content = {
                RecruitmentTextField(
                    value = state.selfIntroduction,
                    onValueChange = onSelfIntroductionChange,
                    hint = stringResource(R.string.recruitment_apply_self_introduction_hint),
                    singleLine = false,
                    minLines = 6,
                    maxLength = SELF_INTRODUCTION_MAX_LENGTH
                )
            }
        )
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    titleHint: String? = null,
    trailingContent: (@Composable () -> Unit)? = null
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
