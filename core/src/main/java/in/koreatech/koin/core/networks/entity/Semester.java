package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.SerializedName;

public class Semester {
    @SerializedName("id")
    public int id;

    @SerializedName("semester")
    public String semester;

    public boolean isSelected;
}
