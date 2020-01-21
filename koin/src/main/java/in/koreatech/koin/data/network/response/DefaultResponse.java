package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
