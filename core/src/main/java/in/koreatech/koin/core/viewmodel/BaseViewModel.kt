package `in`.koreatech.koin.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel() : ViewModel() {
    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected val _errorToast = SingleLiveEvent<String>()
    val errorToast : LiveData<String> get() = _errorToast

    fun CoroutineScope.launchWithLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        ignoreCancellationException: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) = this.launch(
        context, start
    ) {
        _isLoading.value = true
        if(ignoreCancellationException) {
            ignoreCancellationException { block() }
        }
        else {
            block()
        }
        _isLoading.value = false
    }

    fun <T> CoroutineScope.asyncWithLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        ignoreCancellationException: Boolean = true,
        block: suspend CoroutineScope.() -> T
    ) = this.async(
        context, start
    ) {
        _isLoading.value = true
        if(ignoreCancellationException) {
            ignoreCancellationException { block() }
        }
        else {
            block()
        }
        _isLoading.value = false
    }

    fun CoroutineScope.launchIgnoreCancellation(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = this.launch(
        context, start
    ) {
        ignoreCancellationException { block() }
    }

    fun <T> CoroutineScope.asyncIgnoreCancellation(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ) = this.async(
        context, start
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