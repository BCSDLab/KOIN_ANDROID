package `in`.koreatech.koin.data.error

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.unknownErrorHandler
import `in`.koreatech.koin.data.util.withUnknown
import `in`.koreatech.koin.domain.constant.ERROR_INVALID_STUDENT_ID
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_GENDER_NOT_SET
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import retrofit2.HttpException
import javax.inject.Inject

class UserErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserErrorHandler {
    override fun handleGetTokenError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        400 -> ErrorHandler(it.getErrorResponse()?.message ?: context.getString(R.string.error_login_incorrect))
                        404 -> ErrorHandler(it.getErrorResponse()?.message ?: context.getString(R.string.error_login_user_not_found))
                        else -> ErrorHandler(context.getString(R.string.error_network))
                    }
                }

                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }

    override fun handleRequestPasswordResetEmailError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        404 -> ErrorHandler(context.getString(R.string.error_forgotpassword_no_user))
                        422 -> ErrorHandler(context.getString(R.string.error_forgotpassword_invalid_id))
                        else -> ErrorHandler(context.getString(R.string.error_network))
                    }
                }

                is IllegalArgumentException -> ErrorHandler(context.getString(R.string.error_forgotpassword_no_input))
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }

    override fun handleGetUserInfoError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
    }

    override fun handleDeleteUserError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
    }

    override fun handleUsernameDuplicatedError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is IllegalArgumentException -> {
                    ErrorHandler(context.getString(R.string.error_nickname_error))
                }
            }
            unknownErrorHandler(context)
        }
    }

    override fun handleUpdateUserError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when {
                it is IndexOutOfBoundsException || it.message == ERROR_INVALID_STUDENT_ID -> {
                    ErrorHandler(context.getString(R.string.error_invalid_student_number))
                }

                it.message == ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK -> {
                    ErrorHandler(context.getString(R.string.error_nickname_validation_not_performed))
                }

                it.message == ERROR_USERINFO_GENDER_NOT_SET -> {
                    ErrorHandler(context.getString(R.string.error_gender_not_checked))
                }

                else -> unknownErrorHandler(context)
            }
        }
    }

    override fun handleVerifyUserPasswordError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        400 -> ErrorHandler(it.getErrorResponse()?.message ?: context.getString(R.string.error_verify_password))
                        else -> ErrorHandler(context.getString(R.string.error_network))
                    }
                }

                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }
        }
    }
}