package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class OwnerTokenIsValidUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) {
        operator fun invoke(): Boolean {
            return userRepository.ownerTokenIsValid()
        }
    }
