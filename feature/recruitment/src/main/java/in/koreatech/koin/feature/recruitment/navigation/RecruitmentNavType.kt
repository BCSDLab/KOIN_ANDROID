package `in`.koreatech.koin.feature.recruitment.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data class RecruitmentGroupChat(
        val postId: Int,
        val title: String,
        val currentMemberCount: Int,
        val maxMemberCount: Int,
        val date: String
    ) : RecruitmentNavType()

    @Serializable
    data class RecruitmentDirectChat(
        val postId: Int,
        val partnerNickname: String,
        val date: String
    ) : RecruitmentNavType()
}
