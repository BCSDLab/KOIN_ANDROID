package `in`.koreatech.koin.domain.model.business.mystore

import java.time.LocalTime

data class RegisterStore(
    var name: String,
    var category: String?,
    var address: String,
    var imageUri: String?,
    var phoneNumber: String?,
    var deliveryPrice: String?,     //배달비
    var description: String?,
    var dayOfHoliday: String?,
    var openTime: LocalTime,
    var closeTime: LocalTime,
    var isDeliveryOk: Boolean,  //배달 가능 여부
    var isCardOk: Boolean,      //카드결제 여부
    var isBankOk: Boolean       //계좌이체 여부
)