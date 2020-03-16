package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Circle {
    @SerializedName("circles")
    @Expose
    private ArrayList<Circle> circles;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("line_description")
    @Expose
    private String lineDescription;

    @SerializedName("logo_url")
    @Expose
    private String logoUrl;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("link_urls")
    @Expose
    private ArrayList<CircleUrl> linkUrls;

    @SerializedName("background_img_url")
    @Expose
    private String backgroundImgUrl;

    @SerializedName("professor")
    @Expose
    private String professor;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("major_business")
    @Expose
    private String majorBusiness;

    @SerializedName("introduce_url")
    @Expose
    private String introduceUrl;

    @SerializedName("is_deleted")
    @Expose
    private boolean isDeleted;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("totalPage")
    @Expose
    private int totalPage;

    @SerializedName("totalItemCount")
    @Expose
    private int totalItemCount;

    public static class CircleUrl {
        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("link")
        @Expose
        private String link;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    public ArrayList<Circle> getCircles() {
        return circles;
    }

    public void setCircles(ArrayList<Circle> circles) {
        this.circles = circles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLineDescription() {
        return lineDescription;
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<CircleUrl> getLinkUrls() {
        return linkUrls;
    }

    public void setLinkUrls(ArrayList<CircleUrl> linkUrls) {
        this.linkUrls = linkUrls;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMajorBusiness() {
        return majorBusiness;
    }

    public void setMajorBusiness(String majorBusiness) {
        this.majorBusiness = majorBusiness;
    }

    public String getIntroduceUrl() {
        return introduceUrl;
    }

    public void setIntroduceUrl(String introduceUrl) {
        this.introduceUrl = introduceUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}


