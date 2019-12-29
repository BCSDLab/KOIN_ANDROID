package in.koreatech.koin.core.networks.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.koreatech.koin.core.networks.entity.CityBus;

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
