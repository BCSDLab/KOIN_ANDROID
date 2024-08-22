package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice

interface OwnerRegisterRepository {
    suspend fun ownerRegister(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int?,
        shopName: String
    ): Result<Unit>

    suspend fun storeRegister(
        name: String,
        category: Int,
        address: String,
        imageUri: String,
        phoneNumber: String,
        deliveryPrice: String,     //배달비
        description: String,
        operatingTime: List<OperatingTime>,
        isDeliveryOk: Boolean,  //배달 가능 여부
        isCardOk: Boolean,      //카드결제 여부
        isBankOk: Boolean       //계좌이체 여부
    ): Result<Unit>

    suspend fun storeMenuRegister(
        storeId: Int,
        menuCategoryId: List<Int>,
        description: String,
        menuImageUrlList: List<String>,
        isSingle: Boolean,
        menuName: String,
        menuOptionPrice: List<StoreMenuOptionPrice>,
        menuSinglePrice: String
    ): Result<Unit>
}