package in.koreatech.koin.data.network.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Item {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("type")
    @Expose
    public int type;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("user_id")
    @Expose
    public int userId;

    @SerializedName("price")
    @Expose
    public int price;

    @SerializedName("state")
    @Expose
    public int state;


    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("is_phone_open")
    @Expose
    public boolean isPhoneOpen;

    @SerializedName("image_urls")
    @Expose
    public ArrayList<String> urls;

    @SerializedName("url")
    @Expose
    public String url;


    @SerializedName("updated_at")
    @Expose
    public String updateAt;

    @SerializedName("hit")
    @Expose
    public String hit;

    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("comments")
    @Expose
    public ArrayList<Comment> comments;

    @SerializedName("grantEdit")
    @Expose
    public boolean grantEdit;


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

//    public int getImageURLIndex() {
//        return urls.size();
//    }


}
