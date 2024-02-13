package `in`.koreatech.koin.data.source.remote.business

import `in`.koreatech.koin.data.api.business.MyStoreApi
import `in`.koreatech.koin.data.api.business.MyStoreRegisterApi
import `in`.koreatech.koin.data.response.business.MyStoreRegisterResponse
import javax.inject.Inject

class MyStoreRegisterDataSource @Inject constructor(
    private val myStoreRegisterApi: MyStoreRegisterApi
    // api 연결
) {
    suspend fun putMyStoreItems(myStoreRegisterResponse: MyStoreRegisterResponse): MyStoreRegisterResponse {
       return myStoreRegisterApi.putMyStore(myStoreRegisterResponse)
    }
}