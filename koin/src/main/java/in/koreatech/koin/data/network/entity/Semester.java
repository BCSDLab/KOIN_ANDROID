package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.SerializedName;

public class Semester {
    @SerializedName("id")
    public int id;

    @SerializedName("semester")
    public String semester;

    public boolean isSelected;
}
