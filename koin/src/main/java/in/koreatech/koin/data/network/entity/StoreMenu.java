package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreMenu {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("shop_id")
    @Expose
    private int shopId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price_type")
    @Expose
    private ArrayList<PriceType> priceType;

    @SerializedName("created_at")
    @Expose
    private String creatAt;

    @SerializedName("update_at")
    @Expose
    private String updateAt;

    private String size;

    private String price;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PriceType> getPriceType() {
        return priceType;
    }

    public void setPriceType(ArrayList<PriceType> priceType) {
        this.priceType = priceType;
    }

    public String getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(String creatAt) {
        this.creatAt = creatAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
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
}
