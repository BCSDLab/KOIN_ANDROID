package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BokdukRoom {
    @SerializedName("lands")
    @Expose
    public ArrayList<BokdukRoom> lands;


    @SerializedName("monthly_fee")
    @Expose
    public String monthlyFee;


    @SerializedName("latitude")
    @Expose
    public Double latitude;

    @SerializedName("charter_fee")
    @Expose
    public String charterFee;


    @SerializedName("internal_name")
    @Expose
    public String internalName;


    @SerializedName("id")
    @Expose
    public int id;

    public ArrayList<BokdukRoom> getLands() {
        return lands;
    }

    public String getMonthlyFee() {
        return monthlyFee;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getCharterFee() {
        return charterFee;
    }

    public String getInternalName() {
        return internalName;
    }

    public int getId() {
        return id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getRoomType() {
        return roomType;
    }

    @SerializedName("longitude")
    @Expose
    public Double longitude;


    @SerializedName("name")
    @Expose
    public String name;


    @SerializedName("room_type")
    @Expose
    public String roomType;
}