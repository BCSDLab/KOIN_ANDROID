package `in`.koreatech.koin.domain.usecase.business.businessauth

import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.formatBusinessNumber
import javax.inject.Inject

class CheckExistsCompanyNumberUseCase
    @Inject
    constructor(
        private val signUpRepository: OwnerSignupRepository,
    ) {
        suspend operator fun invoke(companyNumber: String): Result<SignupContinuationState> {
            try {
                signUpRepository.checkExistsCompanyNumber(companyNumber.formatBusinessNumber())
                return Result.success(SignupContinuationState.CheckComplete)
            } catch (throwable: Throwable) {
                if (throwable is OwnerError.CompanyNumberIsDuplicatedException) {
                    return Result.failure(OwnerError.CompanyNumberIsDuplicatedException)
                }
                return Result.failure(throwable)
            }
        }
    }
