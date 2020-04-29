package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Dining {
    //Unique ID
    @SerializedName("id")
    @Expose
    private Integer uid;

    //식단 날짜
    //YYYY-MM-DD
    @SerializedName("date")
    @Expose
    private String date;

    //type - 아침 점심 저녁
    //BREAKFAST, LUNCH, DINNER
    @SerializedName("type")
    @Expose
    private String type;

    //종류
    @SerializedName("place")
    @Expose
    private String place;

    //카드 금액
    @SerializedName("price_card")
    @Expose
    private Integer priceCard;

    //현금 금액
    @SerializedName("price_cash")
    @Expose
    private Integer priceCash;

    //칼로리
    @SerializedName("kcal")
    @Expose
    private Integer kcal;

    //menu 리스트
    @SerializedName("menu")
    @Expose
    private ArrayList<String> menu;

    //생성 날짜
    @SerializedName("created_at")
    @Expose
    private String createDate;

    //업데이트 날짜
    @SerializedName("updated_at")
    @Expose
    private String updateDate;

    @SerializedName("error")
    @Expose
    private String error;

    public Dining() {}

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getPriceCard() {
        return priceCard;
    }

    public void setPriceCard(Integer priceCard) {
        this.priceCard = priceCard;
    }

    public Integer getPriceCash() {
        return priceCash;
    }

    public void setPriceCash(Integer priceCash) {
        this.priceCash = priceCash;
    }

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public ArrayList<String> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<String> menu) {
        this.menu = menu;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
