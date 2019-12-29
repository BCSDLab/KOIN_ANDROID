package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Land {
    @SerializedName("opt_electronic_door_lock")
    @Expose
    public boolean optElectronicDoorLock;

    @SerializedName("opt_tv")
    @Expose
    public boolean optTv;

    @SerializedName("monthly_fee")
    @Expose
    public String monthlyFee;

    @SerializedName("opt_elevator")
    @Expose
    public boolean optElevator;

    @SerializedName("opt_water_purifier")
    @Expose
    public boolean optWaterPurifier;

    @SerializedName("opt_washer")
    @Expose
    public boolean optWasher;

    @SerializedName("latitude")
    @Expose
    public String latitude;

    @SerializedName("charter_fee")
    @Expose
    public String charterFee;

    @SerializedName("opt_veranda")
    @Expose
    public boolean optVeranda;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("image_urls")
    @Expose
    public ArrayList<String> imageUrls;

    @SerializedName("opt_gas_range")
    @Expose
    public boolean optGasRange;

    @SerializedName("opt_induction")
    @Expose
    public boolean optInduction;

    @SerializedName("internal_name")
    @Expose
    public String internalName;

    @SerializedName("is_deleted")
    @Expose
    public String isDeleted;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    @SerializedName("opt_bidet")
    @Expose
    public boolean optBidet;

    @SerializedName("opt_shoe_closet")
    @Expose
    public boolean optShoeCloset;

    @SerializedName("opt_refrigerator")
    @Expose
    public boolean optRefrigerator;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("floor")
    @Expose
    public int floor;

    @SerializedName("management_fee")
    @Expose
    public String managementFee;

    @SerializedName("opt_desk")
    @Expose
    public boolean optDesk;

    @SerializedName("opt_closet")
    @Expose
    public boolean optCloset;

    @SerializedName("longitude")
    @Expose
    public String longitude;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("opt_bed")
    @Expose
    public boolean optBed;

    @SerializedName("size")
    @Expose
    public String size;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("opt_air_conditioner")
    @Expose
    public boolean optAirConditioner;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("deposit")
    @Expose
    public String deposit;

    @SerializedName("opt_microwave")
    @Expose
    public boolean optMicrowave;

    @SerializedName("room_type")
    @Expose
    public String roomType;

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

    public String getLatitude() {
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

    public String getLongitude() {
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

    public void setLatitude(String latitude) {
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

    public void setLongitude(String longitude) {
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



