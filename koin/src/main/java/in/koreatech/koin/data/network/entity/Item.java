package in.koreatech.koin.data.network.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Item {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("state")
    @Expose
    private int state;


    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("is_phone_open")
    @Expose
    private boolean isPhoneOpen;

    @SerializedName("image_urls")
    @Expose
    private ArrayList<String> urls;

    @SerializedName("url")
    @Expose
    private String url;


    @SerializedName("updated_at")
    @Expose
    private String updateAt;

    @SerializedName("hit")
    @Expose
    private String hit;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> comments;

    @SerializedName("grantEdit")
    @Expose
    private boolean grantEdit;


    public Item() {
    }

    public Item(boolean grantEdit) {
        this.grantEdit = grantEdit;
    }

    public Item(int id, int type, String title, String content, String nickname, int state
            , int price, String phone, boolean isPhoneOpen, ArrayList<String> urls, String createdAt, String updateAt, String thumbnail, String hit, ArrayList<Comment> comments) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.state = state;
        this.price = price;
        this.phone = phone;
        this.isPhoneOpen = isPhoneOpen;
        this.urls = urls;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.hit = hit;
        this.comments = comments;
    }

    public Item(int id, int type, String title, String nickname, int state
            , int price, String createdAt, String thumbnail) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.nickname = nickname;
        this.state = state;
        this.price = price;
        this.createdAt = createdAt;
        this.thumbnail = thumbnail;


    }

    public boolean isGrantEdit() {
        return grantEdit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneOpen() {
        return isPhoneOpen;
    }

    public void setPhoneOpen(boolean phoneOpen) {
        isPhoneOpen = phoneOpen;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setGrantEdit(boolean grantEdit) {
        this.grantEdit = grantEdit;
    }
}
