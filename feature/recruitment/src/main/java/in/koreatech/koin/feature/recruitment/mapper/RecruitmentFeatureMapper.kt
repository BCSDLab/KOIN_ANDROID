package `in`.koreatech.koin.feature.recruitment.mapper

import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivity
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProfile
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.toPersistentList

private val ISO_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

fun TeamRecruitmentProfile.toRecruitmentProfile() = RecruitmentProfile(
    nickname = profileNickname,
    department = department,
    studentId = studentNumber,
    preferredRole = preferredRole,
    skills = skills.toPersistentList(),
    activities = activities.map { it.toRecruitmentActivityEntry() }.toPersistentList(),
    selfIntroduction = selfIntroduction
)

fun TeamRecruitmentActivity.toRecruitmentActivityEntry() = RecruitmentActivityEntry(
    id = id.toLong(),
    name = title,
    startDate = LocalDate.parse(startedAt, ISO_DATE_FORMATTER),
    endDate = endedAt?.let { LocalDate.parse(it, ISO_DATE_FORMATTER) },
    isOngoing = isOngoing,
    content = description
)

fun RecruitmentActivityEntry.toTeamRecruitmentActivityInput() = TeamRecruitmentActivityInput(
    title = name,
    startedAt = startDate.format(ISO_DATE_FORMATTER),
    endedAt = endDate?.format(ISO_DATE_FORMATTER),
    isOngoing = isOngoing,
    description = content
)

