package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hansol on 2020.1.3...
 */
public class AdDetail {

    @SerializedName("comment_count")
    @Expose
    public int comentCount;

    @SerializedName("end_date")
    @Expose
    public String endDate;

    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("comments")
    @Expose
    public ArrayList<Comment> comments;

    @SerializedName("grantEdit")
    @Expose
    public boolean grantEdit;

    @SerializedName("ip")
    @Expose
    public String ip;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("event_title")
    @Expose
    public String eventTitle;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("shop_id")
    @Expose
    public int shopId;

    @SerializedName("hit")
    @Expose
    public int hit;

    @SerializedName("is_deleted")
    @Expose
    public boolean isDeleted;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    @SerializedName("user_id")
    @Expose
    public int userId;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("start_date")
    @Expose
    public String startDate;

    @SerializedName("error")
    @Expose
    public String error;

    // Create
    public AdDetail() {
    }

    public AdDetail(String title, String eventTitle, String content, int shopId, String startDate, String endDate) {
        this.title = title;
        this.eventTitle = eventTitle;
        this.content = content;
        this.shopId = shopId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Create Wit Thumbnail
    public AdDetail(String title, String eventTitle, String content, int shopId, String startDate, String endDate, String thumbnail) {
        this.title = title;
        this.eventTitle = eventTitle;
        this.content = content;
        this.shopId = shopId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
    }

    public int getComentCount() {
        return comentCount;
    }

    public void setComentCount(int comentCount) {
        this.comentCount = comentCount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
