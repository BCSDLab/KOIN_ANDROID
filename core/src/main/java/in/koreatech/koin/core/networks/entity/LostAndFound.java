package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LostAndFound {
    @SerializedName("commtent_count")
    @Expose
    public int commentCount;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("hit")
    @Expose
    public int hit;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("image_urls")
    @Expose
    public String imageUrls;

    @SerializedName("is_phone_open")
    @Expose
    public boolean isPhoneOpen;

    @SerializedName("location")
    @Expose
    public String location;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("state")
    @Expose
    public int state;

    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("type")
    @Expose
    public int type;

    @SerializedName("user_id")
    @Expose
    public int userId;

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
