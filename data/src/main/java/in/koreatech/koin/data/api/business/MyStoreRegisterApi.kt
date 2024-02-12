package `in`.koreatech.koin.data.api.business

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.business.MyStoreRegisterResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MyStoreRegisterApi {
    @POST(URLConstant.OWNER.SHOPS)
    suspend fun putMyStore(@Body myStoreRegisterResponse: MyStoreRegisterResponse): MyStoreRegisterResponse
}