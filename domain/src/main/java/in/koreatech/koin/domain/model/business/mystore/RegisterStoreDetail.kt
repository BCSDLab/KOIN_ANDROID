package `in`.koreatech.koin.domain.model.business.mystore

data class RegisterStoreDetail(
    var phoneNumber: String?,
    var deliveryPrice: String?,     //배달비
    var description: String?,
    var isDeliveryOk: Boolean,  //배달 가능 여부
    var isCardOk: Boolean,      //카드결제 여부
    var isBankOk: Boolean       //계좌이체 여부
)