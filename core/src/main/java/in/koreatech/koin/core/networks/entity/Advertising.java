package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Advertising {
    @SerializedName("event_articles")
    @Expose
    public ArrayList<Advertising> ads;

    @SerializedName("comment_count")
    @Expose
    public int comentCount;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("start_date")
    @Expose
    public String startDate;

    @SerializedName("end_date")
    @Expose
    public String endDate;

    @SerializedName("event_title")
    @Expose
    public String eventTitle;

    @SerializedName("hit")
    @Expose
    public int hit;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("shop_id")
    @Expose
    public int shopId;

    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("user_id")
    @Expose
    public int userId;

    @SerializedName("totalPage")
    @Expose
    public int totalPadge;

    @SerializedName("totalItemCount")
    @Expose
    public int totalCount;

    @SerializedName("created_at")
    @Expose
    public String PublishedDate;

    public ArrayList<Advertising> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Advertising> ads) {
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
