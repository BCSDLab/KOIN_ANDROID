package in.koreatech.koin.core.networks.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Item;

import java.util.ArrayList;

public class MarketPageResponse {

    @SerializedName("items")
    @Expose
    public ArrayList<Item> marketArrayList;

    @SerializedName("totalPage")
    @Expose
    public int totalPage;

    @SerializedName("totalItemCount")
    @Expose
    public int totalItemCount;

    public MarketPageResponse(ArrayList<Item> marketArrayList, int totalPage,int totalItemCount) {
        this.marketArrayList = marketArrayList;
        this.totalPage = totalPage;
        this.totalItemCount = totalItemCount;
    }

}
