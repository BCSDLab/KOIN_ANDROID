package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.api.FakeCallvanAuthApi
import `in`.koreatech.koin.data.source.remote.CallvanRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ReportCallvanUserRepositoryImplTest {

    private lateinit var fakeCallvanAuthApi: FakeCallvanAuthApi
    private lateinit var callvanRepositoryImpl: CallvanRepositoryImpl

    @Before
    fun setUp() {
        fakeCallvanAuthApi = FakeCallvanAuthApi()
        callvanRepositoryImpl = CallvanRepositoryImpl(
            callvanRemoteDataSource = CallvanRemoteDataSource(fakeCallvanAuthApi)
        )
    }

    @Test
    fun `신고 API 호출이 성공하면 Result_success를 반환한다`() = runTest {
        val result = callvanRepositoryImpl.reportCallvanUser(
            postId = 1,
            reportedUserId = 2,
            description = null,
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(result.isSuccess)
    }

    @Test
    fun `첨부 이미지와 기타 사유를 포함한 신고 API 호출이 성공한다`() = runTest {
        val result = callvanRepositoryImpl.reportCallvanUser(
            postId = 1,
            reportedUserId = 2,
            description = "상세 신고 내용",
            reasons = listOf("ABUSE" to null, "OTHER" to "기타 사유"),
            attachmentUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        )

        assert(result.isSuccess)
    }

    @Test
    fun `API 호출 중 예외가 발생하면 Result_failure를 반환한다`() = runTest {
        fakeCallvanAuthApi.reportException = RuntimeException("네트워크 오류")

        val result = callvanRepositoryImpl.reportCallvanUser(
            postId = 1,
            reportedUserId = 2,
            description = null,
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(result.isFailure)
        assert(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `attachmentUrls가 null이면 attachments도 null로 전달된다`() = runTest {
        callvanRepositoryImpl.reportCallvanUser(
            postId = 1,
            reportedUserId = 2,
            description = null,
            reasons = listOf("ABUSE" to null),
            attachmentUrls = null
        )

        assert(fakeCallvanAuthApi.capturedRequest?.attachments == null)
    }
}