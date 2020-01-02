package in.koreatech.koin.data.network.entity;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketItem {



    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("state")
    @Expose
    public int state;

    @SerializedName("price")
    @Expose
    public int price;


    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("is_phone_open")
    @Expose
    public int isPhoneOpen;

    @SerializedName("image_urls")
    @Expose
    public String urls;

    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("type")
    @Expose
    public int type;




    public MarketItem() {
    }

    public MarketItem(boolean grantEdit) {
    }

    public MarketItem( String title, String content, int state
            , int price, String phone, int isPhoneOpen,String urls) {
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

//    public int getImageURLIndex() {
//        return urls.size();
//    }


}


