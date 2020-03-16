package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LostAndFound {
    @SerializedName("commtent_count")
    @Expose
    private int commentCount;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("hit")
    @Expose
    private int hit;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("image_urls")
    @Expose
    private String imageUrls;

    @SerializedName("is_phone_open")
    @Expose
    private boolean isPhoneOpen;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("state")
    @Expose
    private int state;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("user_id")
    @Expose
    private int userId;

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setPhoneOpen(boolean phoneOpen) {
        isPhoneOpen = phoneOpen;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
