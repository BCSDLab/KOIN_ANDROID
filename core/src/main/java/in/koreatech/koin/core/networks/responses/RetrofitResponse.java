package in.koreatech.koin.core.networks.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 4. 29....
 */
public class RetrofitResponse {

    @SerializedName("status")
    public String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @SuppressWarnings({"unused", "used by Retrofit"})
    public RetrofitResponse() {
    }

    public RetrofitResponse(String status) {
        this.status = status;
    }

}
