package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OpenStatus
import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.repository.FakeStoreRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetNearbyShopUseCaseTest {
    private lateinit var storeRepository: FakeStoreRepository

    private val fakeShops = listOf(
        Shop(
            shopId = 178,
            orderableShopId = 178,
            name = "test",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 7),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        ),
        Shop(
            shopId = 232,
            orderableShopId = 232,
            name = "asd",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 2),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        ),
        Shop(
            shopId = 172,
            orderableShopId = 172,
            name = "asdf",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = false,
            categoryIds = listOf(1, 10, 11),
            images = emptyList(),
            openStatus = OpenStatus.CLOSED
        ),
        Shop(
            shopId = 202,
            orderableShopId = 202,
            name = "tea",
            isDeliveryAvailable = true,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = false,
            categoryIds = listOf(1, 7, 9),
            images = emptyList(),
            openStatus = OpenStatus.CLOSED
        ),
        Shop(
            shopId = 193,
            orderableShopId = 193,
            name = "hamburger",
            isDeliveryAvailable = true,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = false,
            categoryIds = listOf(1, 2, 4),
            images = emptyList(),
            openStatus = OpenStatus.CLOSED
        ),
        Shop(
            shopId = 210,
            orderableShopId = 210,
            name = "salad",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 5),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        ),
        Shop(
            shopId = 213,
            orderableShopId = 213,
            name = "shop2",
            isDeliveryAvailable = true,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 3.0,
            reviewCount = 4,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 2),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        ),
        Shop(
            shopId = 221,
            orderableShopId = 221,
            name = "shop10",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 4.3,
            reviewCount = 3,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 10),
            images = emptyList(),
            openStatus = OpenStatus.OPERATING
        ),
        Shop(
            shopId = 225,
            orderableShopId = 225,
            name = "shop11",
            isDeliveryAvailable = false,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 4.0,
            reviewCount = 1,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = false,
            categoryIds = listOf(1, 11),
            images = emptyList(),
            openStatus = OpenStatus.PREPARING
        ),
        Shop(
            shopId = 256,
            orderableShopId = 256,
            name = "shop12",
            isDeliveryAvailable = true,
            isTakeoutAvailable = true,
            serviceEvent = false,
            minimumOrderAmount = 0,
            ratingAverage = 0.0,
            reviewCount = 0,
            minimumDeliveryTip = 0,
            maximumDeliveryTip = 0,
            isOpen = true,
            categoryIds = listOf(1, 12),
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
    fun `전체 주변상점 목록을 가져온다`() = runTest {
        val getNearbyShopUseCase = GetNearbyShopUseCase(storeRepository)
        val result = getNearbyShopUseCase(
            isOperating = false
        )
        assert(result.isSuccess)
        val shops = result.getOrThrow()
        assertEquals(fakeShops.size, shops.size)
    }

    @Test
    fun `영업 중인 주변상점 목록을 가져온다`() = runTest {
        val getNearbyShopUseCase = GetNearbyShopUseCase(storeRepository)
        val result = getNearbyShopUseCase()
        assert(result.isSuccess)
        val shops = result.getOrThrow()
        val expected = fakeShops.filter { it.isOpen }
        assertEquals(expected.size, shops.size)
    }

    @Test
    fun `주변상점 목록을 리뷰 갯수로 정렬해서 가져온다`() = runTest {
        val getNearbyShopUseCase = GetNearbyShopUseCase(storeRepository)
        val result = getNearbyShopUseCase(
            sorter = StoreSorter.COUNT
        )
        assert(result.isSuccess)
        val shops = result.getOrThrow()

        var reviewCount = Int.MAX_VALUE
        for (shop in shops) {
            assert(reviewCount >= shop.reviewCount)
            reviewCount = shop.reviewCount
        }
    }

    @Test
    fun `주변상점 목록을 별점으로 정렬해서 가져온다`() = runTest {
        val getNearbyShopUseCase = GetNearbyShopUseCase(storeRepository)
        val result = getNearbyShopUseCase(
            sorter = StoreSorter.RATING
        )
        assert(result.isSuccess)
        val shops = result.getOrThrow()

        var rating = Double.MAX_VALUE
        for (shop in shops) {
            assert(rating >= shop.ratingAverage)
            rating = shop.ratingAverage
        }
    }

    @Test
    fun `10번 카테고리의 상점 목록을 가져온다`() = runTest {
        val getNearbyShopUseCase = GetNearbyShopUseCase(storeRepository)
        val result = getNearbyShopUseCase(
            categoryId = 10,
            isOperating = false
        )
        assert(result.isSuccess)
        val shops = result.getOrThrow()
        assert(shops.all { it.categoryIds.contains(10) })
        val expected = fakeShops.filter { it.categoryIds.contains(10) }
        assertEquals(expected.size, shops.size)
    }
}
