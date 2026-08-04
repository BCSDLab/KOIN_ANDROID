package `in`.koreatech.koin.feature.department.mock

import `in`.koreatech.koin.feature.department.state.DepartmentState
import `in`.koreatech.koin.feature.department.state.DepartmentTaskState
import kotlinx.collections.immutable.persistentListOf

internal val studentSupportTeamMock = DepartmentState(
    name = "학생지원팀",
    isSingleContact = false,
    tasks = persistentListOf(
        DepartmentTaskState("학생지원팀 총괄", "041-560-2530"),
        DepartmentTaskState("학생지도", "041-560-2531"),
        DepartmentTaskState("교외장학(국가장학 등), 학자금대출, 장애학생지원", "041-560-1400"),
        DepartmentTaskState("교내장학", "041-560-1300")
    )
)

/** 대표번호만 있는 부서 (단순 카드) */
internal val computerScienceOfficeMock = DepartmentState(
    name = "컴퓨터공학부 학부사무실",
    isSingleContact = true,
    tasks = persistentListOf(
        DepartmentTaskState("학부사무실", "041-560-1461")
    )
)

internal val mechanicalEngineeringOfficeMock = DepartmentState(
    name = "기계공학부 학부사무실",
    isSingleContact = true,
    tasks = persistentListOf(
        DepartmentTaskState("학부사무실", "041-560-1121")
    )
)

/** Preview 전용 */
internal val departmentsPreviewMock = persistentListOf(
    studentSupportTeamMock,
    computerScienceOfficeMock,
    mechanicalEngineeringOfficeMock
)
