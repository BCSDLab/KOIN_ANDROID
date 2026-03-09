package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.error.callvan.KoinCallvanException
import `in`.koreatech.koin.domain.repository.FakeCallvanRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ReportCallvanUserUseCaseTest {

    private lateinit var callvanRepository: FakeCallvanRepository
    private lateinit var reportCallvanUserUseCase: ReportCallvanUserUseCase

    @Before
    fun setUp() {
        callvanRepository = FakeCallvanRepository()
        reportCallvanUserUseCase = ReportCallvanUserUseCase(callvanRepository)
    }

    @Test
    fun `신고가 성공하면 Result_success를 반환한다`() = runTest {
        callvanRepository.reportResult = Result.success(Unit)

        val result = reportCallvanUserUseCase(
            postId = 1,
            reportedUserId = 2,
            description = "신고 사유",
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(result.isSuccess)
    }

    @Test
    fun `자기 자신을 신고하면 CallvanReportSelfException이 반환된다`() = runTest {
        callvanRepository.reportResult = Result.failure(KoinCallvanException.CallvanReportSelfException())

        val result = reportCallvanUserUseCase(
            postId = 1,
            reportedUserId = 1,
            description = null,
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(result.isFailure)
        assert(result.exceptionOrNull() is KoinCallvanException.CallvanReportSelfException)
    }

    @Test
    fun `참여자가 아닌 경우 CallvanReportOnlyParticipantException이 반환된다`() = runTest {
        callvanRepository.reportResult = Result.failure(KoinCallvanException.CallvanReportOnlyParticipantException())

        val result = reportCallvanUserUseCase(
            postId = 1,
            reportedUserId = 2,
            description = null,
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(result.isFailure)
        assert(result.exceptionOrNull() is KoinCallvanException.CallvanReportOnlyParticipantException)
    }

    @Test
    fun `이미 대기 중인 신고가 있으면 CallvanReportAlreadyPendingException이 반환된다`() = runTest {
        callvanRepository.reportResult = Result.failure(KoinCallvanException.CallvanReportAlreadyPendingException())

        val result = reportCallvanUserUseCase(
            postId = 1,
            reportedUserId = 2,
            description = null,
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(result.isFailure)
        assert(result.exceptionOrNull() is KoinCallvanException.CallvanReportAlreadyPendingException)
    }

    @Test
    fun `첨부 이미지와 기타 사유를 포함한 신고가 성공한다`() = runTest {
        callvanRepository.reportResult = Result.success(Unit)

        val result = reportCallvanUserUseCase(
            postId = 1,
            reportedUserId = 2,
            description = "상세 신고 내용",
            reasons = listOf("ABUSE" to null, "OTHER" to "직접 입력한 기타 사유"),
            attachmentUrls = listOf("https://example.com/image1.jpg")
        )

        assert(result.isSuccess)
    }

    @Test
    fun `useCase가 repository에 올바른 파라미터를 전달한다`() = runTest {
        callvanRepository.reportResult = Result.success(Unit)

        val testPostId = 42
        val testReportedUserId = 7
        val testDescription = "테스트 설명"
        val testReasons = listOf("ABUSE" to null, "OTHER" to "기타")
        val testAttachmentUrls = listOf("https://example.com/img.jpg")

        reportCallvanUserUseCase(
            postId = testPostId,
            reportedUserId = testReportedUserId,
            description = testDescription,
            reasons = testReasons,
            attachmentUrls = testAttachmentUrls
        )

        assertEquals(testPostId, callvanRepository.capturedPostId)
        assertEquals(testReportedUserId, callvanRepository.capturedReportedUserId)
        assertEquals(testDescription, callvanRepository.capturedDescription)
        assertEquals(testReasons, callvanRepository.capturedReasons)
        assertEquals(testAttachmentUrls, callvanRepository.capturedAttachmentUrls)
    }
}
