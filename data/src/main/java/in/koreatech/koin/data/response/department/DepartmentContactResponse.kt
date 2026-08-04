package `in`.koreatech.koin.data.response.department

import com.google.gson.annotations.SerializedName

data class DepartmentContactResponse(
    @SerializedName("task") val task: String,
    @SerializedName("phone_number") val phoneNumber: String
)

data class DepartmentResponse(
    @SerializedName("name") val name: String,
    @SerializedName("is_single_contact") val isSingleContact: Boolean,
    @SerializedName("contacts") val contacts: List<DepartmentContactResponse>
)

data class DepartmentCategoryContactsResponse(
    @SerializedName("category") val category: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("departments") val departments: List<DepartmentResponse>
)

data class DepartmentContactsResponse(
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("categories") val categories: List<DepartmentCategoryContactsResponse>
)

data class DepartmentContactsByCategoryResponse(
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("category") val category: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("departments") val departments: List<DepartmentResponse>
)
