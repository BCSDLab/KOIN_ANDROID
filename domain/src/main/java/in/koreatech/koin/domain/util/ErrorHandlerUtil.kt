package `in`.koreatech.koin.domain.util

import `in`.koreatech.koin.domain.model.error.ErrorHandler

inline fun <T> Pair<T?, ErrorHandler?>.onSuccess(action: (T) -> Unit): Pair<T?, ErrorHandler?> =
    this.also {
        it.first?.let(action)
    }

inline fun <T> Pair<T?, ErrorHandler?>.onFailure(action: (ErrorHandler) -> Unit): Pair<T?, ErrorHandler?> =
    this.also {
        it.second?.let(action)
    }

