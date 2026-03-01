package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OpenStatus
import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.repository.FakeStoreRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetOrderableShopsUseCaseTest {

    private lateinit var storeRepository: FakeStoreRepository

    private val fakeShops = listOf(
        Shop(
            shopId = 1,
            orderableShopId = 1,
            name = "pizza",
            isDeliveryAvailable = true,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 10000,
            ratingAverage = 4.5,
            reviewCount = 10,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 2),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        ),
        Shop(
            shopId = 2,
            orderableShopId = 2,
            name = "chicken",
            isDeliveryAvailable = true,
            isTakeoutAvailable = false,
            serviceEvent = false,
            minimumOrderAmount = 15000,
            ratingAverage = 3.0,
            reviewCount = 5,
            minimumDeliveryTip = 1000,
            maximumDeliveryTip = 3000,
            isOpen = false,
            categoryIds = listOf(1, 3),
            images = emptyList(),
            openStatus = OpenStatus.CLOSED
        ),
        Shop(
            shopId = 3,
            orderableShopId = 3,
            name = "burger",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 5000,
            ratingAverage = 4.0,
            reviewCount = 20,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 4),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        )
    )

    @Before
    fun setUp() {
        storeRepository = FakeStoreRepository()
        storeRepository.setFakeShops(fakeShops)
    }

    @Test
    fun `주문 가능한 상점 목록을 가져온다`() = runTest {
        val getOrderableShopsUseCase = GetOrderableShopsUseCase(storeRepository)
        val result = getOrderableShopsUseCase()

        assertTrue(result.isSuccess)
        assertEquals(fakeShops.size, result.getOrThrow().size)
    }

    @Test
    fun `minimumOrderAmount가 Int MAX_VALUE이면 저장소에 null로 전달된다`() = runTest {
        val getOrderableShopsUseCase = GetOrderableShopsUseCase(storeRepository)

        getOrderableShopsUseCase(minimumOrderAmount = Int.MAX_VALUE)

        assertNull(storeRepository.lastCapturedMinimumOrderAmount)
    }

    @Test
    fun `minimumOrderAmount가 유효한 값이면 해당 값을 그대로 저장소에 전달한다`() = runTest {
        val getOrderableShopsUseCase = GetOrderableShopsUseCase(storeRepository)
        val minimumOrderAmount = 10000

        getOrderableShopsUseCase(minimumOrderAmount = minimumOrderAmount)

        assertEquals(minimumOrderAmount, storeRepository.lastCapturedMinimumOrderAmount)
    }

    @Test
    fun `정렬 옵션 없이 가져오면 기본값(NONE)으로 저장소에 전달된다`() = runTest {
        val getOrderableShopsUseCase = GetOrderableShopsUseCase(storeRepository)

        getOrderableShopsUseCase()

        assertEquals("NONE", storeRepository.lastCapturedSorter)
    }

    @Test
    fun `저장소가 비어있을 때 빈 리스트를 반환한다`() = runTest {
        storeRepository.setFakeShops(emptyList())
        val getOrderableShopsUseCase = GetOrderableShopsUseCase(storeRepository)

        val result = getOrderableShopsUseCase()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `저장소 호출 실패 시 실패 결과를 반환한다`() = runTest {
        storeRepository.setShouldFail(true)
        val getOrderableShopsUseCase = GetOrderableShopsUseCase(storeRepository)

        val result = getOrderableShopsUseCase()

        assertFalse(result.isSuccess)
    }
}
