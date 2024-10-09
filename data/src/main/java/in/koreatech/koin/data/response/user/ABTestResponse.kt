package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class ABTestResponse(
    @SerializedName("variable_name") val variableName: String,
    @SerializedName("access_history_id") val accessHistoryId: String,
)