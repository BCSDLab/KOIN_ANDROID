package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Land {
    @SerializedName("lands")
    @Expose
    private ArrayList<Land> lands;

    @SerializedName("opt_electronic_door_lock")
    @Expose
    private boolean optElectronicDoorLock;

    @SerializedName("opt_tv")
    @Expose
    private boolean optTv;

    @SerializedName("monthly_fee")
    @Expose
    private String monthlyFee;

    @SerializedName("opt_elevator")
    @Expose
    private boolean optElevator;

    @SerializedName("opt_water_purifier")
    @Expose
    private boolean optWaterPurifier;

    @SerializedName("opt_washer")
    @Expose
    private boolean optWasher;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("charter_fee")
    @Expose
    private String charterFee;

    @SerializedName("opt_veranda")
    @Expose
    private boolean optVeranda;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image_urls")
    @Expose
    private ArrayList<String> imageUrls;

    @SerializedName("opt_gas_range")
    @Expose
    private boolean optGasRange;

    @SerializedName("opt_induction")
    @Expose
    private boolean optInduction;

    @SerializedName("internal_name")
    @Expose
    private String internalName;

    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("opt_bidet")
    @Expose
    private boolean optBidet;

    @SerializedName("opt_shoe_closet")
    @Expose
    private boolean optShoeCloset;

    @SerializedName("opt_refrigerator")
    @Expose
    private boolean optRefrigerator;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("floor")
    @Expose
    private int floor;

    @SerializedName("management_fee")
    @Expose
    private String managementFee;

    @SerializedName("opt_desk")
    @Expose
    private boolean optDesk;

    @SerializedName("opt_closet")
    @Expose
    private boolean optCloset;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("opt_bed")
    @Expose
    private boolean optBed;

    @SerializedName("size")
    @Expose
    private String size;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("opt_air_conditioner")
    @Expose
    private boolean optAirConditioner;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("deposit")
    @Expose
    private String deposit;

    @SerializedName("opt_microwave")
    @Expose
    private boolean optMicrowave;

    @SerializedName("room_type")
    @Expose
    private String roomType;

    public ArrayList<Land> getLands() {
        return lands;
    }

    public boolean getOptElectronicDoorLock() {
        return optElectronicDoorLock;
    }

    public boolean getOptTv() {
        return optTv;
    }

    public String getMonthlyFee() {
        return monthlyFee;
    }

    public boolean getOptElevator() {
        return optElevator;
    }

    public boolean getOptWaterPurifier() {
        return optWaterPurifier;
    }

    public boolean getOptWasher() {
        return optWasher;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getCharterFee() {
        return charterFee;
    }

    public boolean getOptVeranda() {
        return optVeranda;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public boolean getOptGasRange() {
        return optGasRange;
    }

    public boolean getOptInduction() {
        return optInduction;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean getOptBidet() {
        return optBidet;
    }

    public boolean getOptShoeCloset() {
        return optShoeCloset;
    }

    public boolean getOptRefrigerator() {
        return optRefrigerator;
    }

    public int getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public String getManagementFee() {
        return managementFee;
    }

    public boolean getOptDesk() {
        return optDesk;
    }

    public boolean getOptCloset() {
        return optCloset;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public boolean getOptBed() {
        return optBed;
    }

    public String getSize() {
        return size;
    }

    public String getPhone() {
        return phone;
    }

    public boolean getOptAirConditioner() {
        return optAirConditioner;
    }

    public String getName() {
        return name;
    }

    public String getDeposit() {
        return deposit;
    }

    public boolean getOptMicrowave() {
        return optMicrowave;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setLands(ArrayList<Land> lands) {
        this.lands = lands;
    }

    public void setOptElectronicDoorLock(boolean optElectronicDoorLock) {
        this.optElectronicDoorLock = optElectronicDoorLock;
    }

    public void setOptTv(boolean optTv) {
        this.optTv = optTv;
    }

    public void setMonthlyFee(String monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public void setOptElevator(boolean optElevator) {
        this.optElevator = optElevator;
    }

    public void setOptWaterPurifier(boolean optWaterPurifier) {
        this.optWaterPurifier = optWaterPurifier;
    }

    public void setOptWasher(boolean optWasher) {
        this.optWasher = optWasher;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setCharterFee(String charterFee) {
        this.charterFee = charterFee;
    }

    public void setOptVeranda(boolean optVeranda) {
        this.optVeranda = optVeranda;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setOptGasRange(boolean optGasRange) {
        this.optGasRange = optGasRange;
    }

    public void setOptInduction(boolean optInduction) {
        this.optInduction = optInduction;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setOptBidet(boolean optBidet) {
        this.optBidet = optBidet;
    }

    public void setOptShoeCloset(boolean optShoeCloset) {
        this.optShoeCloset = optShoeCloset;
    }

    public void setOptRefrigerator(boolean optRefrigerator) {
        this.optRefrigerator = optRefrigerator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setManagementFee(String managementFee) {
        this.managementFee = managementFee;
    }

    public void setOptDesk(boolean optDesk) {
        this.optDesk = optDesk;
    }

    public void setOptCloset(boolean optCloset) {
        this.optCloset = optCloset;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOptBed(boolean optBed) {
        this.optBed = optBed;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOptAirConditioner(boolean optAirConditioner) {
        this.optAirConditioner = optAirConditioner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public void setOptMicrowave(boolean optMicrowave) {
        this.optMicrowave = optMicrowave;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}



