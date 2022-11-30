package `in`.koreatech.koin.data.error

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.error.token.TokenErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenErrorHandler {
    override fun handleLogoutError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(context.getString(R.string.error_failed_logout))
    }
}