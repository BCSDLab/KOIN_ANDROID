package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 4. 29....
 */

public class Auth {
    private final String Tag = Auth.class.getSimpleName();

    @SerializedName("token")
    @Expose
    public final String token;

    @SerializedName("user")
    @Expose
    public final User user;

    public Auth(String token, User user) {
        this.token = token;
        this.user = user;
    }
}

