package `in`.koreatech.koin.domain.usecase.business.mystore

import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import `in`.koreatech.koin.domain.repository.business.MyStoreRepository
import javax.inject.Inject

class GetMyStoreUseCase @Inject constructor(
    private val myStoreRepository: MyStoreRepository
) {
    suspend fun testUseCase(): MyStore{
        return myStoreRepository.getMyStores()
    }
}