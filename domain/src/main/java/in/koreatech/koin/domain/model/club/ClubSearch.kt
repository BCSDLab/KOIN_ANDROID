package `in`.koreatech.koin.domain.model.club

data class ClubSearch(
    val keywords: List<ClubSearchItem>
) {
    data class ClubSearchItem(
        val clubId: Int,
        val clubName: String
    )
}
