package `in`.koreatech.koin.data.api.business

import `in`.koreatech.koin.domain.model.business.mystore.RegisterStore
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface RegisterStoreInterface {
    @Multipart
    @POST("경로")
    fun registeMyStore(
        @Part image: MultipartBody.Part?,
        @PartMap data: HashMap<RegisterStore, RequestBody>
    ) : Call<RegisterStore>
}