package `in`.koreatech.koin.data.response.land

import com.google.gson.annotations.SerializedName

data class LandDetailResponse(
    @SerializedName("opt_electronic_door_locks")
    val optElectronicDoorLock: Boolean,
    @SerializedName("opt_tv")
    val optTv: Boolean,
    @SerializedName("monthly_fee")
    val monthlyFee: String?,
    @SerializedName("opt_elevator")
    val optElevator: Boolean,
    @SerializedName("opt_water_purifier")
    val optWaterPurifier: Boolean,
    @SerializedName("opt_washer")
    val optWasher: Boolean,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("charter_fee")
    val charterFee: String?,
    @SerializedName("opt_veranda")
    val optVeranda: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image_urls")
    val imageUrls: List<String>?,
    @SerializedName("opt_gas_range")
    val optGasRange: Boolean,
    @SerializedName("opt_induction")
    val optInduction: Boolean,
    @SerializedName("internal_name")
    val internalName: String?,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("opt_bidet")
    val optBidet: Boolean,
    @SerializedName("opt_shoe_closet")
    val optShoeCloset: Boolean,
    @SerializedName("opt_refrigerator")
    val optRefrigerator: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("floor")
    val floor: Int?,
    @SerializedName("management_fee")
    val managementFee: String?,
    @SerializedName("opt_desk")
    val optDesk: Boolean,
    @SerializedName("opt_closet")
    val optCloset: Boolean,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("address")
    val address: String?,
    @SerializedName("opt_bed")
    val optBed: Boolean,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("opt_air_conditioner")
    val optAirConditioner: Boolean,
    @SerializedName("name")
    val name: String?,
    @SerializedName("deposit")
    val deposit: String?,
    @SerializedName("opt_microwave")
    val optMicrowave: Boolean,
    @SerializedName("room_type")
    val roomType: String?
)
