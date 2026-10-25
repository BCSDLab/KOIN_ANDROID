package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class GetStoreWithMenuUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(storeId: Int): Result<StoreWithMenu> {
        return suspendRunCatching {
            storeRepository.getStoreWithMenu(storeId)
        }
    }
}
