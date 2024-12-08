package `in`.koreatech.bus.util

import `in`.koreatech.koin.core.onboarding.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

internal fun<T> Flow<T>.withMock(mock: T): Flow<T> {
    return this.catch {
        Timber.tag("Flow.withMock").e(it)
        if (BuildConfig.DEBUG) emit(mock)
        else throw it
    }
}