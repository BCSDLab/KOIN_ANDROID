package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 권한 확인 Entitu
 */
public class GrantCheckResponse {
    @SerializedName("grantEdit")
    @Expose
    public boolean isGrantEdit;
}
