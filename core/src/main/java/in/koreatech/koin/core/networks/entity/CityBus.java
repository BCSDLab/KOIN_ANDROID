package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 8. 13....
 */
public class CityBus {
    @SerializedName("_400")
    @Expose
    public String bus400Time;

    @SerializedName("_401")
    @Expose
    public String bus401Time;

    @SerializedName("_402")
    @Expose
    public String bus402Time;

    @SerializedName("_493")
    @Expose
    public String bus493Time;

    public CityBus(String bus400Time, String bus401Time, String bus402Time, String bus493Time) {
        this.bus400Time = bus400Time;
        this.bus401Time = bus401Time;
        this.bus402Time = bus402Time;
        this.bus493Time = bus493Time;
    }
}
