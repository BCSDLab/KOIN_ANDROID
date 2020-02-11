package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceType {
    @SerializedName("size")
    @Expose
    public String size;

    @SerializedName("price")
    @Expose
    public String price;
}
