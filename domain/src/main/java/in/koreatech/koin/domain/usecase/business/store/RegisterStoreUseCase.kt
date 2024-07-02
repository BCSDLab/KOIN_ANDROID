package `in`.koreatech.koin.domain.usecase.business.store

import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import javax.inject.Inject

class RegisterStoreUseCase @Inject constructor(
    private val ownerRegisterRepository: OwnerRegisterRepository
) {
    suspend operator fun invoke(
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
    ): Result<Unit> {
        return ownerRegisterRepository.storeRegister(
            name = name,
            category = category,
            address = address,
            imageUri = imageUri,
            phoneNumber = phoneNumber,
            deliveryPrice = deliveryPrice,     //배달비
            description = description,
            operatingTime = operatingTime,
            isDeliveryOk = isDeliveryOk,  //배달 가능 여부
            isCardOk = isCardOk,      //카드결제 여부
            isBankOk = isBankOk       //계좌이체 여부
        )
    }
}