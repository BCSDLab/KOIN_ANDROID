package `in`.koreatech.koin.domain.usecase.store.benefit

import `in`.koreatech.koin.domain.model.store.BenefitCategory
import `in`.koreatech.koin.domain.model.store.BenefitCategoryList
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class StoreBenefitCategoryUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<BenefitCategoryList> {
        return try {
            storeRepository.getStoreBenefitCategories().let {
                Result.success(it)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}