package in.koreatech.koin.data.network.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketItem {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("state")
    @Expose
    private int state;

    @SerializedName("price")
    @Expose
    private int price;


    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("is_phone_open")
    @Expose
    private int isPhoneOpen;

    @SerializedName("image_urls")
    @Expose
    private String urls;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("type")
    @Expose
    private int type;


    public MarketItem() {
    }

    public MarketItem(boolean grantEdit) {
    }

    public MarketItem(String title, String content, int state
            , int price, String phone, int isPhoneOpen, String urls) {
        this.title = title;
        this.content = content;
        this.state = state;
        this.price = price;
        this.phone = phone;
        this.isPhoneOpen = isPhoneOpen;
        this.urls = urls;
    }

    public MarketItem(int id, int type, String title, String nickname, int state
            , int price, String createdAt, String thumbnail) {
        this.title = title;
        this.state = state;
        this.price = price;


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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsPhoneOpen() {
        return isPhoneOpen;
    }

    public void setIsPhoneOpen(int isPhoneOpen) {
        this.isPhoneOpen = isPhoneOpen;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}


