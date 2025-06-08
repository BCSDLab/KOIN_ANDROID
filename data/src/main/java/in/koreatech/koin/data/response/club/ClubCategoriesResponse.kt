package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubCategoriesResponse(
    @SerializedName("club_categories")
    val clubCategories: List<Categories>
) {
    data class Categories(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}
