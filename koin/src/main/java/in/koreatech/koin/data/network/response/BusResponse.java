package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 8. 13....
 */
public class BusResponse {
    @SerializedName("bus_number")
    @Expose
    public int busNumber;

    @SerializedName("remain_time")
    @Expose
    public int remainTime;

    @SerializedName("next_bus_number")
    @Expose
    public int nextBusNumber;

    @SerializedName("next_remain_time")
    @Expose
    public int nextRemainTime;


}
