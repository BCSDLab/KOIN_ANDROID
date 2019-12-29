package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Circle {
    @SerializedName("circles")
    @Expose
    public ArrayList<Circle> circles;

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("category")
    @Expose
    public String category;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("line_description")
    @Expose
    public String lineDescription;

    @SerializedName("logo_url")
    @Expose
    public String logoUrl;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("link_urls")
    @Expose
    public ArrayList<CircleUrl> linkUrls;

    @SerializedName("background_img_url")
    @Expose
    public String backgroundImgUrl;

    @SerializedName("professor")
    @Expose
    public String professor;

    @SerializedName("location")
    @Expose
    public String location;

    @SerializedName("major_business")
    @Expose
    public String majorBusiness;

    @SerializedName("introduce_url")
    @Expose
    public String introduceUrl;

    @SerializedName("is_deleted")
    @Expose
    public boolean isDeleted;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    @SerializedName("totalPage")
    @Expose
    public int totalPage;

    @SerializedName("totalItemCount")
    @Expose
    public int totalItemCount;

    public static class CircleUrl
    {
        @SerializedName("type")
        @Expose
        public String type;

        @SerializedName("link")
        @Expose
        public String link;
    }
}


