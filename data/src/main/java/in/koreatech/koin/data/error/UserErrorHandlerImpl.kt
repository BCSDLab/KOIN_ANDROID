package `in`.koreatech.koin.data.error

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.domain.constant.ERROR_INVALID_STUDENT_ID
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_GENDER_NOT_SET
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.Result
import retrofit2.HttpException
import javax.inject.Inject

class UserErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserErrorHandler {
    override fun handleGetTokenError(throwable: Throwable): Result.Error {
        return throwable.handleCommonError(context) {
            when(it) {
                is HttpException -> {
                    when(it.code()) {
                        401 -> Result.Error(context.getString(R.string.error_login_incorrect))
                        else -> Result.Error(context.getString(R.string.error_network))
                    }
                }
                else -> null
            }
        }
    }

    override fun handleRequestPasswordResetEmailError(throwable: Throwable): Result.Error {
        return throwable.handleCommonError(context) {
            when(it) {
                is HttpException -> {
                    when(it.code()) {
                        404 -> Result.Error(context.getString(R.string.error_forgotpassword_no_user))
                        422 -> Result.Error(context.getString(R.string.error_forgotpassword_invalid_id))
                        else -> Result.Error(context.getString(R.string.error_network))
                    }
                }
                is IllegalArgumentException -> Result.Error(context.getString(R.string.error_forgotpassword_no_input))
                else -> Result.Error(context.getString(R.string.error_unknown))
            }
        }
    }

    override fun handleGetUserInfoError(throwable: Throwable): Result.Error {
        return throwable.handleCommonError(context)
    }

    override fun handleDeleteUserError(throwable: Throwable): Result.Error {
        return throwable.handleCommonError(context)
    }

    override fun handleUsernameDuplicatedError(throwable: Throwable): Result.Error {
        return throwable.handleCommonError(context) {
            when(it) {
                is IllegalArgumentException -> {
                    Result.Error(context.getString(R.string.error_nickname_error))
                }
                else -> null
            }
        }
    }

    override fun handleUpdateUserError(throwable: Throwable): Result.Error {
        return throwable.handleCommonError(context) {
            when {
                it is IndexOutOfBoundsException || it.message == ERROR_INVALID_STUDENT_ID -> {
                    Result.Error(context.getString(R.string.error_invalid_student_number))
                }
                it.message == ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK -> {
                    Result.Error(context.getString(R.string.error_nickname_validation_not_performed))
                }
                it.message == ERROR_USERINFO_GENDER_NOT_SET -> {
                    Result.Error(context.getString(R.string.error_gender_not_checked))
                }
                else -> null
            }
        }
    }
}
