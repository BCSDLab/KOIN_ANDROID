package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Dining {
    //Unique ID
    @SerializedName("id")
    @Expose
    public Integer uid;

    //식단 날짜
    //YYYY-MM-DD
    @SerializedName("date")
    @Expose
    public String date;

    //type - 아침 점심 저녁
    //BREAKFAST, LUNCH, DINNER
    @SerializedName("type")
    @Expose
    public String type;

    //종류
    @SerializedName("place")
    @Expose
    public String place;

    //카드 금액
    @SerializedName("price_card")
    @Expose
    public Integer priceCard;

    //현금 금액
    @SerializedName("price_cash")
    @Expose
    public Integer priceCash;

    //칼로리
    @SerializedName("kcal")
    @Expose
    public Integer kcal;

    //menu 리스트
    @SerializedName("menu")
    @Expose
    public ArrayList<String> menu;

    //생성 날짜
    @SerializedName("created_at")
    @Expose
    public String createDate;

    //업데이트 날짜
    @SerializedName("updated_at")
    @Expose
    public String updateDate;

    @SerializedName("error")
    @Expose
    public String error;

    public Dining() {
    }

    public Dining(String date, String type, String place, Integer priceCard, Integer priceCash, Integer kcal, ArrayList<String> menu) {
        this.date = date;
        this.type = type;
        this.place = place;
        this.priceCard = priceCard;
        this.priceCash = priceCash;
        this.kcal = kcal;
        this.menu = new ArrayList<>();
        this.menu.addAll(menu);

    }
}
