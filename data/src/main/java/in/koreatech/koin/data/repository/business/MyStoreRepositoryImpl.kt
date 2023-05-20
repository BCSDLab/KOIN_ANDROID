package `in`.koreatech.koin.data.repository.business

import `in`.koreatech.koin.data.mapper.business.toMyStore
import `in`.koreatech.koin.data.source.remote.business.MyStoreRemoteDataSource
import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import `in`.koreatech.koin.domain.repository.business.MyStoreRepository
import javax.inject.Inject

class MyStoreRepositoryImpl @Inject constructor(
    private val myStoreRemoteDataSource: MyStoreRemoteDataSource
):MyStoreRepository {
    override suspend fun getMyStores(): List<MyStore> {
        return myStoreRemoteDataSource.getMyStoreItems().map {
            it.toMyStore()
        }
    }
}