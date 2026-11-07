package `in`.koreatech.koin.data.source.remote.firebase.messaging

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseMessagingDataSourceImplTest {
    @Test
    fun `성공한 Task는 토큰 값을 반환한다`() = runTest {
        val token = Tasks.forResult("fcm-token").awaitResult()

        assertEquals("fcm-token", token)
    }

    @Test
    fun `실패한 Task는 원인 타입을 보존하고 후속 처리를 실행하지 않는다`() = runTest {
        val cause = IllegalStateException("token unavailable")
        var updateRequested = false

        val thrown = runCatching {
            Tasks.forException<String>(cause).awaitResult()
            updateRequested = true
        }.exceptionOrNull()

        assertEquals(cause.javaClass, thrown?.javaClass)
        assertEquals(cause.message, thrown?.message)
        assertFalse(updateRequested)
    }

    @Test
    fun `취소된 Task는 CancellationException을 전달한다`() = runTest {
        val thrown = runCatching {
            Tasks.forCanceled<String>().awaitResult()
        }.exceptionOrNull()

        assertTrue(thrown is CancellationException)
    }

    @Test
    fun `호출자가 취소된 뒤 완료된 Task는 결과를 전달하지 않는다`() = runTest {
        val taskSource = TaskCompletionSource<String>()
        val received = AtomicReference<String?>()

        val job = launch {
            received.set(taskSource.task.awaitResult())
        }
        runCurrent()
        job.cancelAndJoin()

        taskSource.setResult("late-token")
        runCurrent()

        assertTrue(job.isCancelled)
        assertNull(received.get())
    }

    @Test
    fun `대기 중인 Task가 완료되면 실제 토큰을 전달한다`() = runTest {
        val taskSource = TaskCompletionSource<String>()
        val pending = async { taskSource.task.awaitResult() }
        runCurrent()
        assertFalse(pending.isCompleted)

        taskSource.setResult("completed-token")

        assertEquals("completed-token", pending.await())
    }
}
