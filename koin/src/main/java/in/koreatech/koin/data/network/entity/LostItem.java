package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LostItem implements Serializable {
    @SerializedName("comment_count")
    @Expose
    public int commentCount;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("is_phone_open")
    @Expose
    public boolean isPhoneOpen;

    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("ip")
    @Expose
    public String ip;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("image_urls")
    @Expose
    public String imageUrls;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("type")
    @Expose
    public int type;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("hit")
    @Expose
    public int hit;

    @SerializedName("is_deleted")
    @Expose
    public boolean isDeleted;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("user_id")
    @Expose
    public int userId;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("location")
    @Expose
    public String location;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("state")
    @Expose
    public int state;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @SerializedName("comments")
    @Expose
    public ArrayList<Comment> comments;

    public int getCommentCount() {
        return commentCount;
    }

    public String getDate() {
        return date;
    }

    public boolean isPhoneOpen() {
        return isPhoneOpen;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getIp() {
        return ip;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getHit() {
        return hit;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public int getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }
}
