package `in`.koreatech.koin.feature.recruitment.model

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRole as DomainRecruitmentRole

data class RecruitmentRole(val name: String, val count: Int)

fun DomainRecruitmentRole.toRecruitmentRole() = RecruitmentRole(name = name, count = maxParticipants)
