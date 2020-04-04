package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Store {
    //Unique ID
    @SerializedName("id")
    @Expose
    private Integer uid;

    //가게 이름
    @SerializedName("name")
    @Expose
    private String name;

    //chosung, 가게 이름 앞자리 1글자의 초성
    @SerializedName("chosung")
    @Expose
    private String chosung;

    //종류
    //기타(S000), 콜벤(S001), 정식(S002), 족발(S003), 중국집(S004), 치킨(S005), 피자(S006), 탕수육(S007), 일반(S008), 미용실(S009)
    @SerializedName("category")
    @Expose
    private String category;

    //전화번호 010-0000-0000
    @SerializedName("phone")
    @Expose
    private String phone;

    //오픈 시간 ex) 11:00
    @SerializedName("open_time")
    @Expose
    private String openTime;

    //마감 시간 ex) 11:00
    @SerializedName("close_time")
    @Expose
    private String closeTime;

    //주소
    @SerializedName("address")
    @Expose
    private String address;

    //세부사항
    @SerializedName("description")
    @Expose
    private String description;

    //배달 여부
    @SerializedName("delivery")
    @Expose
    private boolean isDeliveryOk;

    //배달 금액
    @SerializedName("delivery_price")
    @Expose
    private int deliveryPrice;

    //카드 여부
    @SerializedName("pay_card")
    @Expose
    private boolean isCardOk;

    //계좌이체 여부
    @SerializedName("pay_bank")
    @Expose
    private boolean isBankOk;

    //이미지 링크  '[ A : B ]'
    @SerializedName("image_urls")
    @Expose
    private ArrayList<String> imageUrls;

    //생성 날짜 example: 2018-03-21 16:40:57
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

    @SerializedName("menus")
    @Expose
    private ArrayList<StoreMenu> menus;

    @SerializedName("event_articles")
    @Expose
    private ArrayList<Event> events;

    private int shopId;

    private String size;

    private String price;

    private String detail;


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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChosung() {
        return chosung;
    }

    public void setChosung(String chosung) {
        this.chosung = chosung;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeliveryOk() {
        return isDeliveryOk;
    }

    public void setDeliveryOk(boolean deliveryOk) {
        isDeliveryOk = deliveryOk;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public boolean isCardOk() {
        return isCardOk;
    }

    public void setCardOk(boolean cardOk) {
        isCardOk = cardOk;
    }

    public boolean isBankOk() {
        return isBankOk;
    }

    public void setBankOk(boolean bankOk) {
        isBankOk = bankOk;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
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

    public ArrayList<StoreMenu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<StoreMenu> menus) {
        this.menus = menus;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
