package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreMenu {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("shop_id")
    @Expose
    public int shopId;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("price_type")
    @Expose
    public ArrayList<PriceType> priceType;

    @SerializedName("created_at")
    @Expose
    public String creatAt;

    @SerializedName("update_at")
    @Expose
    public String updateAt;

    public String size;

    public String price;
}
