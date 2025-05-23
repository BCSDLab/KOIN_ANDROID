package `in`.koreatech.koin.domain.model.club

data class ClubQnasInfo(
    val rootCount: Int,
    val totalCount: Int,
    val qnas: List<Qna>
) {
    data class Qna(
        val id: Int,
        val authorId: Int,
        val nickname: String,
        val content: String,
        val createdAt: String,
        val children: List<String>
    )
}
