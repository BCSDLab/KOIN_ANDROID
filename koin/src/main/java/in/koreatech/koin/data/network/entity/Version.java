package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {
    public static final int PRIORITY_HIGH = 0; // 우선순위 높음
    public static final int  PRIORITY_MIDDLE = 1; // 우선순위 중간
    public static final int  PRIORITY_LOW = 2; // 우선순위 낮음

    @SerializedName("version")
    @Expose
    private final String version;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updateAt;

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }


    public Version(String version) {
        this.version = version;
    }

    public Version(String version, String type) {
        this.version = version;
        this.type = type;
    }

}
