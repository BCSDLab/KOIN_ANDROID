package `in`.koreatech.koin.data.error

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.unknownErrorHandler
import `in`.koreatech.koin.data.util.withUnknown
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

class UserErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserErrorHandler {
    override fun handleRequestPasswordResetEmailError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when(it) {
                is HttpException -> {
                    when(it.code()) {
                        404 -> ErrorHandler(context.getString(R.string.error_forgotpassword_no_user))
                        422 -> ErrorHandler(context.getString(R.string.error_forgotpassword_invalid_id))
                        else -> null
                    }
                }
                is IllegalArgumentException -> ErrorHandler(context.getString(R.string.error_forgotpassword_no_input))
                else -> null
            }.withUnknown(context)
        }
    }
}