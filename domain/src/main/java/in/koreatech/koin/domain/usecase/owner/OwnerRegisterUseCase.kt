package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import javax.inject.Inject

class OwnerRegisterUseCase @Inject constructor(
    private val ownerRegisterRepository: OwnerRegisterRepository
) {
    suspend operator fun invoke(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int,
        shopName: String
    ): Result<Unit> {
        return ownerRegisterRepository.ownerRegister(attachments, companyNumber, email, name, password, phoneNumber, shopId, shopName)
    }
}