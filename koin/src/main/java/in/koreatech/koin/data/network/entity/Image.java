package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Image {
    @SerializedName("url")
    @Expose
    ArrayList<String> urls;

    public ArrayList<String> getUrls() {
        return urls;
    }
}
