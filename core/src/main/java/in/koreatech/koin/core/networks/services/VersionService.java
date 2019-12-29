package in.koreatech.koin.core.networks.services;


import in.koreatech.koin.core.networks.entity.Version;
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

import static in.koreatech.koin.core.constants.URLConstant.VERSION;

/**
 * Created by hyerim on 2018. 5. 14....
 */
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
