package `in`.koreatech.koin.domain.util

import kotlin.Result as KotlinResult
import kotlinx.coroutines.CancellationException

/**
 * Cancellation-safe version of [runCatching] for suspend functions in the domain layer.
 *
 * Unlike the stdlib [runCatching], this helper re-throws [CancellationException] so that
 * structured concurrency is not broken when the enclosing coroutine is cancelled.
 */
suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): KotlinResult<T> {
    return try {
        KotlinResult.success(block())
    } catch (t: CancellationException) {
        throw t
    } catch (t: Throwable) {
        KotlinResult.failure(t)
    }
}
