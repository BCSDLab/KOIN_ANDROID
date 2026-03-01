package `in`.koreatech.koin.domain.usecase.dept

import `in`.koreatech.koin.domain.error.dept.FakeDeptErrorHandler
import `in`.koreatech.koin.domain.repository.FakeDeptRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetDeptNameFromStudentIdUseCaseTest {

    private lateinit var deptRepository: FakeDeptRepository
    private lateinit var deptErrorHandler: FakeDeptErrorHandler

    // Student ID of "2023136001": deptCode = substring(5..6) = "36"
    private val validStudentId10 = "2023136001"
    private val deptCode10 = "36"

    // Student ID of "202313001" (9 digits): deptCode = "30"
    private val validStudentId9 = "202313001"
    private val deptCode9 = "30"

    // Student ID of "20231001" (8 digits): deptCode = substring(5..6) = "00"
    private val validStudentId8 = "20231001"
    private val deptCode8 = "00"

    @Before
    fun setUp() {
        deptRepository = FakeDeptRepository()
        deptErrorHandler = FakeDeptErrorHandler()
    }

    @Test
    fun `유효한 10자리 학번으로 학과명을 가져온다`() = runTest {
        deptRepository.setDeptNameForCode(deptCode10, "컴퓨터공학부")

        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)(validStudentId10)

        assertEquals("컴퓨터공학부", result.first)
        assertNull(result.second)
    }

    @Test
    fun `유효한 9자리 학번으로 학과명을 가져온다`() = runTest {
        deptRepository.setDeptNameForCode(deptCode9, "기계공학부")

        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)(validStudentId9)

        assertEquals("기계공학부", result.first)
        assertNull(result.second)
    }

    @Test
    fun `유효한 8자리 학번으로 학과명을 가져온다`() = runTest {
        deptRepository.setDeptNameForCode(deptCode8, "전기전자통신공학부")

        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)(validStudentId8)

        assertEquals("전기전자통신공학부", result.first)
        assertNull(result.second)
    }

    @Test
    fun `학번 길이가 7자 이하이면 빈 문자열과 null 에러를 반환한다`() = runTest {
        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)("2023130")

        assertEquals("", result.first)
        assertNull(result.second)
    }

    @Test
    fun `학번 길이가 11자 이상이면 빈 문자열과 null 에러를 반환한다`() = runTest {
        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)("20231360012")

        assertEquals("", result.first)
        assertNull(result.second)
    }

    @Test
    fun `학번에 숫자가 아닌 문자가 포함되면 빈 문자열과 null 에러를 반환한다`() = runTest {
        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)("2023abcdef")

        assertEquals("", result.first)
        assertNull(result.second)
    }

    @Test
    fun `빈 문자열 학번이면 빈 문자열과 null 에러를 반환한다`() = runTest {
        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)("")

        assertEquals("", result.first)
        assertNull(result.second)
    }

    @Test
    fun `저장소 호출 실패 시 null과 ErrorHandler를 반환한다`() = runTest {
        deptRepository.setShouldThrow(true)

        val result = GetDeptNameFromStudentIdUseCase(deptRepository, deptErrorHandler)(validStudentId10)

        assertNull(result.first)
        assertNotNull(result.second)
    }
}
