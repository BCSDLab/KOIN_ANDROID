package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreWithMenuV2
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class GetStoreWithMenuV2UseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(storeId: Int): Result<StoreWithMenuV2> {
        return suspendRunCatching {
            storeRepository.getStoreWithMenuV2(storeId)
        }
    }
}
