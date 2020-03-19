package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Event {
    @SerializedName("event_articles")
    @Expose
    private ArrayList<Event> ads;

    @SerializedName("grantEdit")
    @Expose
    private boolean grantEdit;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("shop_id")
    @Expose
    private int shopId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("event_title")
    @Expose
    private String eventTitle;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("hit")
    @Expose
    private int hit;

    @SerializedName("ip")
    @Expose
    private String ip;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("comment_count")
    @Expose
    private int comentCount;

    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;

    @SerializedName("created_at")
    @Expose
    private String PublishedDate;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("totalPage")
    @Expose
    private int totalPadge;

    @SerializedName("totalItemCount")
    @Expose
    private int totalCount;

    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> comments;

    @SerializedName("error")
    @Expose
    private String error;

    // Create
    public Event() {
    }

    public Event(String title, String eventTitle, String content, int shopId, String startDate, String endDate) {
        this.title = title;
        this.eventTitle = eventTitle;
        this.content = content;
        this.shopId = shopId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Create Wit Thumbnail
    public Event(String title, String eventTitle, String content, int shopId, String startDate, String endDate, String thumbnail) {
        this.title = title;
        this.eventTitle = eventTitle;
        this.content = content;
        this.shopId = shopId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
    }

    // Update Event
    public Event(String title, String eventTitle, String content, String startDate, String endDate) {
        this.title = title;
        this.eventTitle = eventTitle;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isGrantEdit() {
        return grantEdit;
    }

    public void setGrantEdit(boolean grantEdit) {
        this.grantEdit = grantEdit;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Event> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Event> ads) {
        this.ads = ads;
    }

    public int getComentCount() {
        return comentCount;
    }

    public void setComentCount(int comentCount) {
        this.comentCount = comentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalPadge() {
        return totalPadge;
    }

    public void setTotalPadge(int totalPadge) {
        this.totalPadge = totalPadge;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getPublishedDate() {
        return PublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        PublishedDate = publishedDate;
    }
}
