package `in`.koreatech.koin.domain.repository.business

import `in`.koreatech.koin.domain.model.business.mystore.MyStore

interface MyStoreRepository {
    suspend fun getMyStores(): MyStore
}