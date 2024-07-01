package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OwnerAuthApi{
    @POST(URLConstant.OWNER.SHOPS)
    suspend fun postMyStore(@Body storeRegisterResponse: StoreRegisterResponse): StoreRegisterResponse
}