package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import javax.inject.Inject

class OwnerRegisterUseCase @Inject constructor(
    private val ownerRegisterRepository: OwnerRegisterRepository,
    private val ownerErrorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int?,
        shopName: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            ownerRegisterRepository.ownerRegister(
                attachments,
                companyNumber,
                name,
                password,
                phoneNumber,
                shopId,
                shopName
            )
            Unit to null
        } catch (t: Throwable) {
            null to ownerErrorHandler.handleOwnerRegisterError(t)
        }

    }
}