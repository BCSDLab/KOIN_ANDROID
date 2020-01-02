package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.koreatech.koin.data.network.entity.Store;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class StoresResponse {
    @SerializedName("shops")
    @Expose
    public ArrayList<Store> storeArrayList;

    @SerializedName("code")
    @Expose
    private String code;

    public StoresResponse(ArrayList<Store> storeArrayList) {
        this.storeArrayList = storeArrayList;
    }
}
