package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.FakeDiningRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetDiningUseCaseTest {

    private lateinit var diningRepository: FakeDiningRepository

    private fun makeDining(id: Int, type: String, menu: List<String>) = Dining(
        id = id,
        date = "2024-01-01",
        type = type,
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
    fun `식단 목록을 그대로 반환한다`() = runTest {
        val dinings = listOf(
            makeDining(1, "조식", listOf("김치찌개", "밥")),
            makeDining(2, "중식", listOf("비빔밥")),
            makeDining(3, "석식", listOf("미운영"))
        )
        diningRepository.setFakeDinings(dinings)

        val result = GetDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertEquals(dinings.size, result.getOrThrow().size)
        assertEquals(dinings, result.getOrThrow())
    }

    @Test
    fun `미운영 식단도 필터링 없이 반환한다`() = runTest {
        val dinings = listOf(
            makeDining(1, "조식", listOf("미운영")),
            makeDining(2, "중식", listOf("미운영"))
        )
        diningRepository.setFakeDinings(dinings)

        val result = GetDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `식단이 없으면 빈 목록을 반환한다`() = runTest {
        diningRepository.setFakeDinings(emptyList())

        val result = GetDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `저장소 호출 실패 시 실패 결과를 반환한다`() = runTest {
        diningRepository.setShouldThrow(true)

        val result = GetDiningUseCase(diningRepository)("2024-01-01")

        assertTrue(result.isFailure)
    }
}
