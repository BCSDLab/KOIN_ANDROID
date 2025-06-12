package `in`.koreatech.koin.domain.model.club

data class ClubCategories(
    val clubCategories: List<Categories>
) {
    data class Categories(
        val id: Int,
        val name: String
    )
}
