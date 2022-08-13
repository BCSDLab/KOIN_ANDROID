package `in`.koreatech.koin.core.util

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> ignoreCancelledResult(block: () -> Result<R>) = block().let { result ->
    if(result.exceptionOrNull() is CancellationException) null
    else result
}