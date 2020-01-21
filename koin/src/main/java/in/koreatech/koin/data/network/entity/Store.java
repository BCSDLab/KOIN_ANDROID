package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Store {
    //Unique ID
    @SerializedName("id")
    @Expose
    public Integer uid;

    //가게 이름
    @SerializedName("name")
    @Expose
    public String name;

    //chosung, 가게 이름 앞자리 1글자의 초성
    @SerializedName("chosung")
    @Expose
    public String chosung;

    //종류
    //기타(S000), 콜벤(S001), 정식(S002), 족발(S003), 중국집(S004), 치킨(S005), 피자(S006), 탕수육(S007), 일반(S008), 미용실(S009)
    @SerializedName("category")
    @Expose
    public String category;

    //전화번호 010-0000-0000
    @SerializedName("phone")
    @Expose
    public String phone;

    //오픈 시간 ex) 11:00
    @SerializedName("open_time")
    @Expose
    public String openTime;

    //마감 시간 ex) 11:00
    @SerializedName("close_time")
    @Expose
    public String closeTime;

    //주소
    @SerializedName("address")
    @Expose
    public String address;

    //세부사항
    @SerializedName("description")
    @Expose
    public String description;

    //배달 여부
    @SerializedName("delivery")
    @Expose
    public boolean isDeliveryOk;

    //배달 금액
    @SerializedName("delivery_price")
    @Expose
    public int deliveryPrice;

    //카드 여부
    @SerializedName("pay_card")
    @Expose
    public boolean isCardOk;

    //계좌이체 여부
    @SerializedName("pay_bank")
    @Expose
    public boolean isBankOk;

    //이미지 링크  '[ A : B ]'
    @SerializedName("image_urls")
    @Expose
    public ArrayList<String> imageUrls;

    //생성 날짜 example: 2018-03-21 16:40:57
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

    @SerializedName("menus")
    @Expose
    public ArrayList<StoreMenu> menus;

    public int shopId;

    public String size;

    public String price;

    public String detail;


    public Store(Integer uid, String name, String chosung, String category, String phone, String openTime, String closeTime, String address, String description, boolean isDeliveryOk, int deliveryPrice, boolean isCardOk, boolean isBankOk, ArrayList<String> imageUrls, String createDate, String updateDate, String error) {
        this.uid = uid;
        this.name = name;
        this.chosung = chosung;
        this.category = category;
        this.phone = phone;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.address = address;
        this.description = description;
        this.isDeliveryOk = isDeliveryOk;
        this.deliveryPrice = deliveryPrice;
        this.isCardOk = isCardOk;
        this.isBankOk = isBankOk;
        this.imageUrls = imageUrls;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.error = error;
    }

    public Store(Integer uid, String name, String chosung, String category, String phone, String openTime, String closeTime, String address, String description, boolean isDeliveryOk, int deliveryPrice, boolean isCardOk, boolean isBankOk, ArrayList<String> imageUrls, String createDate, String updateDate) {
        this.uid = uid;
        this.name = name;
        this.chosung = chosung;
        this.category = category;
        this.phone = phone;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.address = address;
        this.description = description;
        this.isDeliveryOk = isDeliveryOk;
        this.deliveryPrice = deliveryPrice;
        this.isCardOk = isCardOk;
        this.isBankOk = isBankOk;
        this.imageUrls = imageUrls;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Store() {

    }
}
