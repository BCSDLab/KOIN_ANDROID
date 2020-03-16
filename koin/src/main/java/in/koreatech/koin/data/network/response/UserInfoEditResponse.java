package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoEditResponse {

    @SerializedName("success")
    @Expose
    public String success;

    @SerializedName("error")
    @Expose
    public String error;


    @SerializedName("message")
    @Expose
    public String message;

}
