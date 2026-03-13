package `in`.koreatech.koin.feature.callvan.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostDetailUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetNotificationsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CallvanDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeCallvanRepository
    private lateinit var getCallvanPostDetailUseCase: GetCallvanPostDetailUseCase
    private lateinit var getNotificationsUseCase: GetNotificationsUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeCallvanRepository()
        getCallvanPostDetailUseCase = GetCallvanPostDetailUseCase(fakeRepository)
        getNotificationsUseCase = GetNotificationsUseCase(fakeRepository)
    }

    private fun createViewModel(postId: Int = 1) = CallvanDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("postId" to postId)),
        getCallvanPostDetailUseCase = getCallvanPostDetailUseCase,
        getNotificationsUseCase = getNotificationsUseCase
    )

    @Test
    fun `상세 조회 성공 시 state가 올바르게 업데이트된다`() = runTest {
        fakeRepository.postDetailResult = Result.success(fakePostDetail)
        fakeRepository.notificationsResult = Result.success(emptyList())

        val viewModel = createViewModel()

        viewModel.container.stateFlow.test {
            var state = awaitItem()
            while (state.participants.isEmpty()) {
                state = awaitItem()
            }
            assertEquals("테니스장", state.departure)
            assertEquals("천안 시외터미널", state.destination)
            assertEquals("03.02(월) 14:00", state.dateTime)
            assertEquals(3, state.currentParticipants)
            assertEquals(8, state.maxParticipants)
            assertEquals(2, state.participants.size)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `상세 조회 성공 시 isMe가 true인 참여자가 맨 앞에 온다`() = runTest {
        fakeRepository.postDetailResult = Result.success(fakePostDetail)
        fakeRepository.notificationsResult = Result.success(emptyList())

        val viewModel = createViewModel()

        viewModel.container.stateFlow.test {
            var state = awaitItem()
            while (state.participants.isEmpty()) {
                state = awaitItem()
            }
            assertTrue(state.participants.first().isMe)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `상세 조회 실패 시 isLoading이 false가 된다`() = runTest {
        fakeRepository.postDetailResult = Result.failure(Exception("network error"))
        fakeRepository.notificationsResult = Result.success(
            listOf(fakeNotification(isRead = false))
        )

        val viewModel = createViewModel()
        viewModel.fetchHasNewNotification()

        viewModel.container.stateFlow.test {
            var loadingState = awaitItem()
            assertFalse(loadingState.isLoading)

            while (!loadingState.hasNewNotification) {
                loadingState = awaitItem()
            }
            assertFalse(loadingState.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `읽지 않은 알림이 있으면 hasNewNotification이 true다`() = runTest {
        fakeRepository.postDetailResult = Result.success(fakePostDetail)
        fakeRepository.notificationsResult = Result.success(
            listOf(fakeNotification(isRead = false))
        )

        val viewModel = createViewModel()
        viewModel.fetchHasNewNotification()

        viewModel.container.stateFlow.test {
            var state = awaitItem()
            while (!state.hasNewNotification) {
                state = awaitItem()
            }
            assertTrue(state.hasNewNotification)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `모든 알림이 읽힌 상태면 hasNewNotification이 false다`() = runTest {
        fakeRepository.postDetailResult = Result.success(fakePostDetail)
        fakeRepository.notificationsResult = Result.success(
            listOf(fakeNotification(isRead = true))
        )

        val viewModel = createViewModel()
        viewModel.fetchHasNewNotification()

        viewModel.container.stateFlow.test {
            var state = awaitItem()
            while (state.hasNewNotification) {
                state = awaitItem()
            }
            assertFalse(state.hasNewNotification)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private val fakePostDetail = CallvanPostDetail(
        id = 1,
        title = "테니스장 → 천안",
        departure = "테니스장",
        arrival = "천안 시외터미널",
        departureDate = "2026-03-02",
        departureTime = "14:00",
        currentParticipants = 3,
        maxParticipants = 8,
        status = "OPEN",
        participants = listOf(
            CallvanPostDetail.CallvanParticipant(userId = 2, nickname = "신짱구", isMe = false, isReported = false),
            CallvanPostDetail.CallvanParticipant(userId = 1, nickname = "홍길동", isMe = true, isReported = false)
        )
    )

    private fun fakeNotification(isRead: Boolean) = CallvanNotification(
        id = 1,
        type = "RECRUITMENT_COMPLETE",
        messagePreview = "인원이 모집되었어요.",
        isRead = isRead,
        createdAt = "2026-03-02T10:00:00",
        postId = 1,
        departure = "테니스장",
        arrival = "천안 시외터미널",
        departureDate = "2026-03-02",
        departureTime = "14:00",
        currentParticipants = 3,
        maxParticipants = 8,
        senderNickname = null,
        joinedMemberNickname = null
    )
}
