package `in`.koreatech.koin.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class BaseViewModel() : ViewModel() {
    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected val _errorToast = SingleLiveEvent<String>()
    val errorToast: LiveData<String> get() = _errorToast

    /**
     * Sets [isLoading] to `true` while [block] runs and always resets it to `false` afterwards,
     * including when [block] throws or the coroutine is cancelled.
     *
     * When [ignoreCancellationException] is `true`, a [CancellationException] thrown by [block]
     * is swallowed as before and the job completes successfully. When it is `false`, the
     * exception is rethrown after the loading state is reset.
     */
    fun CoroutineScope.launchWithLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        ignoreCancellationException: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) = this.launch(
        context,
        start
    ) {
        _isLoading.value = true
        try {
            if (ignoreCancellationException) {
                ignoreCancellationException { block() }
            } else {
                block()
            }
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * [asyncWithLoading] counterpart of [launchWithLoading]: the loading state is reset in a
     * `finally` block on success, failure, and cancellation. The resulting [Deferred] behaves
     * exactly as before: it completes with [Unit], while failures from [block] (other than a
     * swallowed [CancellationException]) still fail the coroutine.
     */
    fun <T> CoroutineScope.asyncWithLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        ignoreCancellationException: Boolean = true,
        block: suspend CoroutineScope.() -> T
    ): Deferred<Unit> = this.async(
        context,
        start
    ) {
        _isLoading.value = true
        try {
            if (ignoreCancellationException) {
                ignoreCancellationException { block() }
            } else {
                block()
            }
        } finally {
            _isLoading.value = false
        }
    }

    fun CoroutineScope.launchIgnoreCancellation(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = this.launch(
        context,
        start
    ) {
        ignoreCancellationException { block() }
    }

    fun <T> CoroutineScope.asyncIgnoreCancellation(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ) = this.async(
        context,
        start
    ) {
        ignoreCancellationException { block() }
    }

    fun updateErrorMessage(errorMessage: String) {
        _errorToast.value = errorMessage
    }
}

private inline fun <T> ignoreCancellationException(block: () -> T) {
    try {
        block()
    } catch (e: CancellationException) {
    }
}
