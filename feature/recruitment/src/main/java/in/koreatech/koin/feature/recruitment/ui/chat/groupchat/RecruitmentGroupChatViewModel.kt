package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessage
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@HiltViewModel
class RecruitmentGroupChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<RecruitmentGroupChatState, RecruitmentGroupChatSideEffect> {

    override val container = container<RecruitmentGroupChatState, RecruitmentGroupChatSideEffect>(RecruitmentGroupChatState()) {
        val route = savedStateHandle.toRoute<RecruitmentNavType.RecruitmentGroupChat>()
        reduce {
            state.copy(
                postId = route.postId,
                title = route.title,
                currentMemberCount = route.currentMemberCount,
                maxMemberCount = route.maxMemberCount,
                date = route.date
            )
        }
    }

    fun onChatInputValueChange(value: String) = blockingIntent {
        reduce {
            state.copy(chatInputValue = value)
        }
    }

    fun sendMessage() = intent {
        val content = state.chatInputValue.trim()
        if (content.isEmpty()) return@intent

        val newMessage = RecruitmentChatMessage(
            id = UUID.randomUUID().toString(),
            authorNickname = "",
            content = content,
            timestamp = LocalTime.now().format(TimeFormatter),
            isSentByMe = true
        )

        reduce {
            state.copy(
                messages = state.messages.appendToLastGroup(state.date, newMessage),
                chatInputValue = ""
            )
        }
    }
}

private fun List<RecruitmentChatMessageGroup>.appendToLastGroup(
    date: String,
    message: RecruitmentChatMessage
) = if (isEmpty() || last().date != date) {
    this + RecruitmentChatMessageGroup(date = date, messages = listOf(message).toImmutableList())
} else {
    dropLast(1) + last().copy(messages = (last().messages + message).toImmutableList())
}.toPersistentList()
