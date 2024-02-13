package `in`.koreatech.koin.domain.model.business.mystore

import java.time.LocalTime

data class RegisterStore(
    val name: String,
    val category: String?,
    val address: String,
    val imageUri: String?,
    val phoneNumber: String?,
    val deliveryPrice: String?,     //배달비
    val description: String?,
    val dayOff: ArrayList<MyStoreDayOff>,
    val openTime: String?,
    val closeTime: String?,
    val isDeliveryOk: Boolean,  //배달 가능 여부
    val isCardOk: Boolean,      //카드결제 여부
    val isBankOk: Boolean       //계좌이체 여부
)