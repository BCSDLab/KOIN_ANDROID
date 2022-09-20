package `in`.koreatech.koin.data.response.dept

import com.google.gson.annotations.SerializedName

data class DetailDeptResponse(
    @SerializedName("dept_nums") val deptNums: List<String>,
    @SerializedName("curriculum_link") val curriculumLinkUrl: String,
    @SerializedName("name") val name: String
)
