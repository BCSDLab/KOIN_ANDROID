package `in`.koreatech.koin.data.response.club

data class ClubCategoriesResponse(
    val clubCategories: List<Categories>
) {
    data class Categories(
        val id: Int,
        val name: String
    )
}
