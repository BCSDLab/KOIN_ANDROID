package `in`.koreatech.koin.domain.usecase.business.businessauth

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.util.ext.formatBusinessNumber
import javax.inject.Inject

class CheckExistsCompanyNumberUseCase @Inject constructor(
    private val signUpRepository: OwnerSignupRepository,
) {
    suspend operator fun invoke(companyNumber : String): Result<Unit> {
        try {
            signUpRepository.checkExistsCompanyNumber(companyNumber.formatBusinessNumber())
            return Result.success(Unit)
        } catch (throwable: Throwable) {
            return Result.failure(throwable)
        }
    }
}
