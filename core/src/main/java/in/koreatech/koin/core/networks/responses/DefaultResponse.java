package in.koreatech.koin.core.networks.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 5. 28....
 */
public class DefaultResponse {
    @SerializedName("success")
    @Expose
    public boolean success;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("grantEdit")
    @Expose
    public boolean isGrantEdit;


    public boolean getSuccess() {
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

    public boolean isGrantEdit() {
        return isGrantEdit;
    }
}
