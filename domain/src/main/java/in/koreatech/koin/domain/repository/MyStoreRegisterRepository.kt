package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.business.mystore.MyStoreDayOff
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

interface MyStoreRegisterRepository {
    suspend fun registerOwnerStore(
        name: String,
        category: String,
        address: String,
        imageUri: String,
        phoneNumber: String,
        deliveryPrice: String,     //배달비
        description: String,
        dayOff: ArrayList<MyStoreDayOff>,
        openTime: String,
        closeTime: String,
        isDeliveryOk: Boolean,  //배달 가능 여부
        isCardOk: Boolean,      //카드결제 여부
        isBankOk: Boolean       //계좌이체 여부
    ): Result<Unit>
}