package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreWithMenuUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(storeId: Int) : StoreWithMenu {
        return storeRepository.getStoreWithMenu(storeId)
    }
}