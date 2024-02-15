package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerRepository
import javax.inject.Inject

class GetOwnerNameUseCase @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val ownerErrorHandler: OwnerErrorHandler
) {
    suspend operator fun invoke(): Pair<String?, ErrorHandler?> {
        return try {
            ownerRepository.getOwnerName() to null
        } catch (t: Throwable) {
            null to ownerErrorHandler.handleGetOwnerInfoError(t)
        }
    }
}