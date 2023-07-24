package `in`.koreatech.koin.domain.model.business.mystore

import java.time.LocalTime

data class MyStore (
    var id: Int,
    var name: String,
    var phoneNumber: String?,
    var openTime: LocalTime,
    var closeTime: LocalTime,
    var dayOfHoliday: String?,
    var address: String?,
    var deliveryPrice: Int,     //배달비
    var description: String?,
    var isDeliveryOk: Boolean,  //배달 가능 여부
    var isCardOk: Boolean,      //카드결제 여부
    var isBankOk: Boolean,       //계좌이체 여부
    var images: List<String>,
    var categories: List<Map<String, Any>>
)