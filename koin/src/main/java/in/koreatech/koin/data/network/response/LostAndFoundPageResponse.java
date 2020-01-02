package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.koreatech.koin.data.network.entity.LostItem;

public class LostAndFoundPageResponse {

    @SerializedName("totalPage")
    @Expose
    public int totalPage;

    @SerializedName("lostItems")
    @Expose
    public ArrayList<LostItem> lostItemArrayList;

    @SerializedName("totalItemCount")
    @Expose
    public int totalItemCount;



    public LostAndFoundPageResponse(int totalPage, ArrayList<LostItem> lostItemArrayList, int totalItemCount) {
        this.totalPage = totalPage;
        this.lostItemArrayList = lostItemArrayList;
        this.totalItemCount = totalItemCount;
    }
}
