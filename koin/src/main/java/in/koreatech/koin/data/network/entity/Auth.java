package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth {
    private final String Tag = "Auth";

    @SerializedName("token")
    @Expose
    private final String token;

    @SerializedName("user")
    @Expose
    private final User user;

    public Auth(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}

