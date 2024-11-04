package `in`.koreatech.koin.domain.usecase.store.benefit

import `in`.koreatech.koin.domain.model.store.StoreBenefit
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class BenefitStoreListUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(uid: Int): Result<StoreBenefit> = runCatching {
        storeRepository.getStoreBenefitShopList(uid)
    }
}