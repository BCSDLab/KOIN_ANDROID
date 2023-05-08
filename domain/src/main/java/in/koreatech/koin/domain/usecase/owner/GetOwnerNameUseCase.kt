package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerRepository
import javax.inject.Inject

class GetOwnerNameUseCase @Inject constructor(
    private val ownerRepository: OwnerRepository,
) {
    suspend operator fun invoke(): String =
        ownerRepository.getOwnerName()
}