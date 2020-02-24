package in.koreatech.koin.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public int getBusNumber() {
        return busNumber;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public int getNextBusNumber() {
        return nextBusNumber;
    }

    public int getNextRemainTime() {
        return nextRemainTime;
    }
}
