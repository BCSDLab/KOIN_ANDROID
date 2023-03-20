package `in`.koreatech.core.networking.response.dept

import com.google.gson.annotations.SerializedName

data class DeptResponse(
    @SerializedName("dept_num") val deptNum: String,
    @SerializedName("name") val name: String
)
