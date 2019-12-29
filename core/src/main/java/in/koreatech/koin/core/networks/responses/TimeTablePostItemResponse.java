package in.koreatech.koin.core.networks.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeTablePostItemResponse extends DefaultResponse {
    @SerializedName("id")
    @Expose
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
