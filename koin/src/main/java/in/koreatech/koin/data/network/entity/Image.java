package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Image {
    @SerializedName("url")
    @Expose
    private ArrayList<String> urls;

    private String uid;

    public ArrayList<String> getUrls() {
        return urls;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
