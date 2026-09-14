package `in`.koreatech.koin.feature.recruitment.ui.chat.directchat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentDirectChatState(
    val recruitmentId: Int = 0,
    val applicationId: Int = 0,
    val chatRoomId: Int? = null,
    val partnerNickname: String = "",
    val isLoading: Boolean = true,
    val currentUserId: Int = 0,
    val messages: ImmutableList<RecruitmentChatMessageGroup> = persistentListOf(),
    val chatInputValue: String = "",
    val uploadingImageCount: Int = 0
) {
    val isUploadingImage: Boolean get() = uploadingImageCount > 0
}
