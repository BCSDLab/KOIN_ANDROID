package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Event {
    @SerializedName("event_articles")
    @Expose
    private ArrayList<Event> eventArrayList;

    @SerializedName("shops")
    @Expose
    private ArrayList<Event> myShopList;

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
    private int commentCount;

    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;

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
    private int totalCount;

    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> comments;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("name")
    @Expose
    private String shopName;

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

    // Create With Thumbnail
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

    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }

    public ArrayList<Event> getMyShopList() {
        return myShopList;
    }

    public void setMyShopList(ArrayList<Event> myShopList) {
        this.myShopList = myShopList;
    }

    public boolean isGrantEdit() {
        return grantEdit;
    }

    public void setGrantEdit(boolean grantEdit) {
        this.grantEdit = grantEdit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

}
