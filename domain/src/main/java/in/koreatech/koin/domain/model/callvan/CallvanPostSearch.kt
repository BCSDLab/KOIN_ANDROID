package `in`.koreatech.koin.domain.model.callvan

data class CallvanPostSearch(
    val posts: List<CallvanPost>,
    val totalCount: Long,
    val currentPage: Int,
    val totalPage: Int
) {
    data class CallvanPost(
        val id: Int,
        val title: String,
        val departure: String,
        val arrival: String,
        val departureDate: String,
        val departureTime: String,
        val authorNickname: String?,
        val currentParticipants: Int,
        val maxParticipants: Int,
        val status: String,
        val isJoined: Boolean,
        val isAuthor: Boolean
    )
}
