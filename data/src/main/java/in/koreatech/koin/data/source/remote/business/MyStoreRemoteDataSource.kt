package `in`.koreatech.koin.data.source.remote.business

import `in`.koreatech.koin.data.api.business.MyStoreApi
import `in`.koreatech.koin.data.response.business.mystore.OwnerShopResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class MyStoreRemoteDataSource @Inject constructor(
    private val myStoreApi: MyStoreApi
    // api 연결
) {
    // TestItems -> OwnerShopResponse
    fun getMyStoreItems(): List<TestItems>{
        val id: Int = 1
        val name: String = "가장 맛있는 족발"
        val phoneNumber: String? = "041-523-5849"
        val openTime: String? = "16:00"
        val closeTime: String? = "20:00"
        val dayOfHoliday: String? = "MONDAY"
        val address: String? = "천안시 동남구 병천면"
        val deliveryPrice: Int = 1000     //배달비
        val description: String? = "대대로 내려오는 맛"
        val isDeliveryOk: Boolean = true
        val isCardOk: Boolean = true
        val isBankOk: Boolean = true
        val images: List<String>? = null
        val categories: List<Map<String, Any>>? = null
        return listOf(TestItems(
            id,
            name,
            phoneNumber,
            openTime, closeTime, dayOfHoliday, address, deliveryPrice, description, isDeliveryOk, isCardOk, isBankOk, images, categories
        ))

    }
}

data class TestItems(
    val id: Int,
    val name: String,
    val phoneNumber: String?,
    val openTime: String?,
    val closeTime: String?,
    val dayOfHoliday: String?,
    val address: String?,
    val deliveryPrice: Int,
    val description: String?,
    val isDeliveryOk: Boolean,
    val isCardOk: Boolean,
    val isBankOk: Boolean,
    val images: List<String>?,
    val categories: List<Map<String, Any>>?
)