package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.koreatech.koin.data.network.entity.User;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class AuthResponse{
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("code")
    @Expose
    private String code;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public String getCode() {
        return code;
    }


}
