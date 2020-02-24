package in.koreatech.koin.data.network.service;

import in.koreatech.koin.data.network.entity.Image;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static in.koreatech.koin.constant.URLConstant.TEMP.TEMP_IMAGE_UPLOAD;

public interface TemporaryCommunityService {

    @Multipart
    @POST(TEMP_IMAGE_UPLOAD)
    Observable<Image> postUploadImage(@Part MultipartBody.Part file);
}
