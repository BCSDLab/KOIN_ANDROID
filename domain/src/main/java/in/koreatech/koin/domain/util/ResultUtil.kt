package `in`.koreatech.koin.domain.util

import kotlin.Result as KotlinResult
import kotlinx.coroutines.CancellationException

suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): KotlinResult<T> {
    return try {
        KotlinResult.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        KotlinResult.failure(e)
    }
}
