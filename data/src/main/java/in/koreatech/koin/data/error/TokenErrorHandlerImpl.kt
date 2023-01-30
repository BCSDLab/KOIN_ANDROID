package `in`.koreatech.koin.data.error

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.error.token.TokenErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.domain.model.Result
import javax.inject.Inject

class TokenErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenErrorHandler {
    override fun handleLogoutError(throwable: Throwable): Result.Error {
        return Result.Error(context.getString(R.string.error_failed_logout))
    }
}