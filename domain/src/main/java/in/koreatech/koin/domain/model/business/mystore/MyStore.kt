package `in`.koreatech.koin.domain.model.business.mystore

import java.time.LocalDateTime
import java.time.LocalTime

data class MyStore (
    val id: Int,
    val name: String,
    val phoneNumber: String?,
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val dayOfHoliday: String?,
    val address: String?,
    val deliveryPrice: Int,     //배달비
    val description: String?,
    val isDeliveryOk: Boolean,  //배달 가능 여부
    val isCardOk: Boolean,      //카드결제 여부
    val isBankOk: Boolean,       //계좌이체 여부
    val images: List<String>,
    val categories: List<Map<String, Any>>
)