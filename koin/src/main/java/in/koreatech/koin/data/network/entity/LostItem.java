package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LostItem implements Serializable {
    @SerializedName("comment_count")
    @Expose
    private int commentCount;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("is_phone_open")
    @Expose
    private boolean isPhoneOpen;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("ip")
    @Expose
    private String ip;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("image_urls")
    @Expose
    private String imageUrls;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("hit")
    @Expose
    private int hit;

    @SerializedName("is_deleted")
    @Expose
    private boolean isDeleted;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("state")
    @Expose
    private int state;

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

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhoneOpen(boolean phoneOpen) {
        isPhoneOpen = phoneOpen;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }
}
