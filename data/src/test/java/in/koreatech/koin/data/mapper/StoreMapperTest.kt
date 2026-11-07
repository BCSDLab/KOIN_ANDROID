package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.store.StoreItemResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class StoreMapperTest {
    @Test
    fun `오늘 요일 운영시간이 여러 개면 첫 번째 항목을 사용한다`() {
        val open = dayOfWeekNames().flatMap { dayOfWeek ->
            listOf(
                createOpenData(dayOfWeek = dayOfWeek, openTime = "11:00", closeTime = "15:00"),
                createOpenData(dayOfWeek = dayOfWeek, openTime = "17:00", closeTime = "22:00")
            )
        }

        val openData = createStoreItemResponse(open = open).toStore().open

        assertEquals("11:00", openData.openTime)
        assertEquals("15:00", openData.closeTime)
        assertFalse(openData.closed)
    }

    @Test
    fun `오늘 요일 운영시간이 없으면 기본 운영시간을 사용한다`() {
        val openData = createStoreItemResponse(open = emptyList()).toStore().open

        assertEquals("00:00", openData.openTime)
        assertEquals("00:00", openData.closeTime)
        assertFalse(openData.closed)
    }

    @Test
    fun `운영시간 자체가 없으면 기본 운영시간을 사용한다`() {
        val openData = createStoreItemResponse(open = null).toStore().open

        assertEquals("00:00", openData.openTime)
        assertEquals("00:00", openData.closeTime)
        assertFalse(openData.closed)
    }

    @Test
    fun `운영시간의 null 필드는 빈 값으로 채운다`() {
        val open = dayOfWeekNames().map { dayOfWeek ->
            StoreItemResponse.OpenResponseDTO(
                dayOfWeek = dayOfWeek,
                closed = null,
                openTime = null,
                closeTime = null
            )
        }

        val openData = createStoreItemResponse(open = open).toStore().open

        assertEquals("", openData.openTime)
        assertEquals("", openData.closeTime)
        assertFalse(openData.closed)
    }

    @Test
    fun `benefitDetails가 없으면 benefitDetail 단일 값을 사용한다`() {
        val store = createStoreItemResponse(
            open = null,
            benefitDetails = null,
            benefitDetail = "카드 결제 할인"
        ).toStore()

        assertEquals(listOf("카드 결제 할인"), store.benefitDetails)
    }

    private fun dayOfWeekNames(): List<String> =
        listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")

    private fun createStoreItemResponse(
        open: List<StoreItemResponse.OpenResponseDTO>?,
        benefitDetails: List<String>? = emptyList(),
        benefitDetail: String? = null
    ): StoreItemResponse {
        return StoreItemResponse(
            uid = 1,
            name = "테스트 가게",
            phone = "041-000-0000",
            isDeliveryOk = null,
            isCardOk = null,
            isBankOk = null,
            open = open,
            categoryIds = listOf(1),
            isEvent = null,
            isOpen = null,
            averageRate = 0.0,
            reviewCount = 0,
            benefitDetails = benefitDetails,
            benefitDetail = benefitDetail,
            images = null
        )
    }

    private fun createOpenData(
        dayOfWeek: String,
        openTime: String,
        closeTime: String
    ): StoreItemResponse.OpenResponseDTO {
        return StoreItemResponse.OpenResponseDTO(
            dayOfWeek = dayOfWeek,
            closed = false,
            openTime = openTime,
            closeTime = closeTime
        )
    }
}
