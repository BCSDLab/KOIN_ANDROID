package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

val RECRUITMENT_DEPARTMENTS: ImmutableList<String> =
    persistentListOf("컴퓨터공학부", "전전통", "고용", "산경", "등등..")

@Composable
fun RecruitmentLoadMemberInfoSection(
    onLoadMemberInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_load_member_info),
        titleHint = stringResource(R.string.recruitment_apply_load_member_info_hint),
        content = {
            RecruitmentOutlinedActionButton(
                text = stringResource(R.string.recruitment_apply_load_member_info_button),
                onClick = onLoadMemberInfoClick
            )
        }
    )
}

@Composable
fun RecruitmentNicknameSection(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_nickname),
        isRequired = true,
        trailingContent = {
            Text(
                text = stringResource(R.string.recruitment_apply_char_count, nickname.length, maxLength),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral400
            )
        },
        content = {
            RecruitmentTextField(
                value = nickname,
                onValueChange = onNicknameChange,
                hint = stringResource(R.string.recruitment_apply_nickname_hint),
                maxLength = maxLength
            )
        }
    )
}

@Composable
fun RecruitmentDepartmentSection(
    department: String,
    isDropdownExpanded: Boolean,
    onDropdownExpandChange: (Boolean) -> Unit,
    onDepartmentSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    departments: ImmutableList<String> = RECRUITMENT_DEPARTMENTS
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_department),
        isRequired = true,
        content = {
            RecruitmentDropdown(
                text = department.ifEmpty { stringResource(R.string.recruitment_apply_department_hint) },
                isPlaceholder = department.isBlank(),
                items = departments,
                isExpanded = isDropdownExpanded,
                onExpandedChange = onDropdownExpandChange,
                onItemSelected = { index -> onDepartmentSelected(departments[index]) }
            )
        }
    )
}

@Composable
fun RecruitmentStudentIdSection(
    studentId: String,
    onStudentIdChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_student_id),
        isRequired = true,
        content = {
            RecruitmentTextField(
                value = studentId,
                onValueChange = onStudentIdChange,
                hint = stringResource(R.string.recruitment_apply_student_id_hint)
            )
        }
    )
}

@Composable
fun RecruitmentSkillsSection(
    skills: ImmutableList<String>,
    onSkillTextChange: (Int, String) -> Unit,
    onSkillRemoved: (Int) -> Unit,
    onAddSkillClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_skills),
        titleHint = stringResource(R.string.recruitment_apply_skills_hint),
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                skills.forEachIndexed { index, skill ->
                    key(index, skill) {
                        RecruitmentSkillFieldRow(
                            value = skill,
                            onValueChange = { text -> onSkillTextChange(index, text) },
                            onRemove = { onSkillRemoved(index) }
                        )
                    }
                }
                RecruitmentOutlinedActionButton(
                    text = stringResource(R.string.recruitment_apply_add_skill),
                    onClick = onAddSkillClick
                )
            }
        }
    )
}

@Composable
fun RecruitmentActivitiesSection(
    activities: ImmutableList<RecruitmentActivityEntry>,
    isAddingActivity: Boolean,
    editingActivityId: Long?,
    onAddActivityClick: () -> Unit,
    onEditActivityClick: (RecruitmentActivityEntry) -> Unit,
    onCancelActivityForm: () -> Unit,
    onActivityAdded: (RecruitmentActivityEntry) -> Unit,
    onActivityEdited: (RecruitmentActivityEntry) -> Unit,
    onActivityRemoved: (RecruitmentActivityEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_activities),
        titleHint = stringResource(R.string.recruitment_apply_activities_hint),
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                activities.forEach { activity ->
                    key(activity.id) {
                        if (editingActivityId == activity.id) {
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
                if (isAddingActivity) {
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
}

@Composable
fun RecruitmentSelfIntroductionSection(
    selfIntroduction: String,
    onSelfIntroductionChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier
) {
    RecruitmentFormSection(
        modifier = modifier,
        title = stringResource(R.string.recruitment_apply_self_introduction),
        isRequired = true,
        trailingContent = {
            Text(
                text = stringResource(R.string.recruitment_apply_char_count, selfIntroduction.length, maxLength),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral400
            )
        },
        content = {
            RecruitmentTextField(
                value = selfIntroduction,
                onValueChange = onSelfIntroductionChange,
                hint = stringResource(R.string.recruitment_apply_self_introduction_hint),
                singleLine = false,
                minLines = 6,
                maxLength = maxLength
            )
        }
    )
}
