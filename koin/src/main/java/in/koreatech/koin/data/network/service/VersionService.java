package in.koreatech.koin.data.network.service;


import in.koreatech.koin.data.network.entity.Version;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static in.koreatech.koin.constant.URLConstant.VERSION;

public interface VersionService {
    //version code ex) "1.0.0"

    //Create Version code API
    @POST(VERSION)
    Call<Version> postVersion(@Header("x-access-token") String token, @Body Version version);

    //Get Version code API
    @GET(VERSION + "/{type}")
    Observable<Version> getVersion(@Path("type") String type);

    //Update Version code API
    @PUT(VERSION + "/{type}")
    Call<Version> putVersion(@Header("x-access-token") String token, @Path("type") String type, @Body Version version);

    //Delete Version API
    @DELETE(VERSION + "/{type}")
    Call<ResponseBody> deleteVersion(@Header("x-access-token") String token, @Path("type") String type);

}
