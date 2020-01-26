package in.koreatech.koin.core.networks.services;

import in.koreatech.koin.core.networks.entity.Image;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static in.koreatech.koin.core.constants.URLConstant.TEMP.TEMP_IMAGE_UPLOAD;

public interface TemporaryCommunityService {

    @Multipart
    @POST(TEMP_IMAGE_UPLOAD)
    Observable<Image> postUploadImage(@Part MultipartBody.Part file);
}
