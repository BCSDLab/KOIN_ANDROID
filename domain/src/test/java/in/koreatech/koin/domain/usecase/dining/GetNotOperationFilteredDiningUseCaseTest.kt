package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.FakeDiningRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetNotOperationFilteredDiningUseCaseTest {

    private lateinit var diningRepository: FakeDiningRepository

    private fun makeDining(menu: List<String>) = Dining(
        id = 1,
        date = "2024-01-01",
        type = "조식",
        place = "A코너",
        priceCard = "3000",
        priceCash = "3000",
        kcal = "600",
        menu = menu,
        imageUrl = "",
        createdAt = "",
        updatedAt = "",
        soldOutAt = "",
        changedAt = ""
    )

    @Before
    fun setUp() {
        diningRepository = FakeDiningRepository()
    }

    @Test
    fun `정상 운영 중인 식단만 포함된 목록을 반환한다`() = runTest {
        val operating1 = makeDining(listOf("김치찌개", "된장국", "밥"))
        val operating2 = makeDining(listOf("비빔밥", "미역국"))
        diningRepository.setFakeDinings(listOf(operating1, operating2))

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `미운영 식단은 목록에서 제외된다`() = runTest {
        val operating = makeDining(listOf("김치찌개", "된장국"))
        val notOperating = makeDining(listOf("미운영"))
        diningRepository.setFakeDinings(listOf(operating, notOperating))

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        val dinings = result.getOrThrow()
        assertEquals(1, dinings.size)
        assertEquals(operating, dinings.first())
    }

    @Test
    fun `메뉴가 비어 있는 식단은 목록에서 제외된다`() = runTest {
        val operating = makeDining(listOf("비빔밥"))
        val emptyMenu = makeDining(emptyList())
        diningRepository.setFakeDinings(listOf(operating, emptyMenu))

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        val dinings = result.getOrThrow()
        assertEquals(1, dinings.size)
        assertEquals(operating, dinings.first())
    }

    @Test
    fun `모든 식단이 미운영이면 빈 목록을 반환한다`() = runTest {
        val notOperating1 = makeDining(listOf("미운영"))
        val notOperating2 = makeDining(listOf("미운영"))
        diningRepository.setFakeDinings(listOf(notOperating1, notOperating2))

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `저장소에 식단이 없으면 빈 목록을 반환한다`() = runTest {
        diningRepository.setFakeDinings(emptyList())

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `저장소 호출 실패 시 실패 결과를 반환한다`() = runTest {
        diningRepository.setShouldThrow(true)

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isFailure)
    }

    @Test
    fun `미운영이 첫 번째 항목이 아닌 경우 제외되지 않는다`() = runTest {
        // "미운영"이 첫 번째 항목이 아니면 필터링 대상이 아님
        val dining = makeDining(listOf("김치찌개", "미운영"))
        diningRepository.setFakeDinings(listOf(dining))

        val result = GetNotOperationFilteredDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }
}
