package `in`.koreatech.koin.domain.usecase.dept

import `in`.koreatech.koin.domain.model.user.Dept
import `in`.koreatech.koin.domain.repository.FakeDeptRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetDeptNamesUseCaseTest {

    private lateinit var deptRepository: FakeDeptRepository

    private val fakeDepts = listOf(
        Dept(name = "컴퓨터공학부", curriculumUrl = "https://example.com/cs", codes = listOf("36")),
        Dept(name = "기계공학부", curriculumUrl = "https://example.com/me", codes = listOf("30")),
        Dept(name = "전기전자통신공학부", curriculumUrl = "https://example.com/ee", codes = listOf("10"))
    )

    @Before
    fun setUp() {
        deptRepository = FakeDeptRepository()
    }

    @Test
    fun `학과 이름 목록을 반환한다`() = runTest {
        deptRepository.setFakeDepts(fakeDepts)

        val result = GetDeptNamesUseCase(deptRepository)()

        assertEquals(fakeDepts.size, result.size)
        assertEquals(fakeDepts.map { it.name }, result)
    }

    @Test
    fun `학과가 없으면 빈 목록을 반환한다`() = runTest {
        deptRepository.setFakeDepts(emptyList())

        val result = GetDeptNamesUseCase(deptRepository)()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `학과 이름만 추출하여 반환한다`() = runTest {
        deptRepository.setFakeDepts(fakeDepts)

        val result = GetDeptNamesUseCase(deptRepository)()

        assertTrue(result.contains("컴퓨터공학부"))
        assertTrue(result.contains("기계공학부"))
        assertTrue(result.contains("전기전자통신공학부"))
    }
}
