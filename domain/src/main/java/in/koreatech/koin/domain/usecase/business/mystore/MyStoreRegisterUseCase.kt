package `in`.koreatech.koin.domain.usecase.business.mystore

import `in`.koreatech.koin.domain.model.business.mystore.MyStoreDayOff
import `in`.koreatech.koin.domain.repository.MyStoreRegisterRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class MyStoreRegisterUseCase @Inject constructor(
    private val mystoreRegisterRepository: MyStoreRegisterRepository
) {
    suspend operator fun invoke(
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
    ): Result<Unit> {
        return mystoreRegisterRepository.registerOwnerStore(
            name = name,
            category = category,
            address = address,
            imageUri = imageUri,
            phoneNumber = phoneNumber,
            deliveryPrice = deliveryPrice,     //배달비
            description = description,
            dayOff = dayOff,
            openTime = openTime,
            closeTime = closeTime,
            isDeliveryOk = isDeliveryOk,  //배달 가능 여부
            isCardOk = isCardOk,      //카드결제 여부
            isBankOk = isBankOk       //계좌이체 여부
        )
    }
}