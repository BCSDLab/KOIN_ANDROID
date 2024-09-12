package `in`.koreatech.koin.data.error

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.unknownErrorHandler
import `in`.koreatech.koin.data.util.withUnknown
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import retrofit2.HttpException
import javax.inject.Inject

class OwnerErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OwnerErrorHandler {

    override fun handleGetTokenError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        401 -> ErrorHandler(context.getString(R.string.error_login_incorrect))
                        else -> ErrorHandler(context.getString(R.string.error_network))
                    }
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }

    override fun handleGetOwnerInfoError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    ErrorHandler(context.getString(R.string.error_owner_info))
                }
                is IllegalArgumentException -> {
                    ErrorHandler(context.getString(R.string.error_empty_owner_info))
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }

    override fun handleGetOwnerShopListError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    ErrorHandler(context.getString(R.string.error_owner_info))
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }

    override fun handleDeleteOwnerShopEventError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
    }

    override fun handleModifyOwnerShopInfoError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
    }

    override fun handleVerifySmsCodeError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        409 -> ErrorHandler(context.getString(R.string.error_sms_code_not_match))
                        else -> ErrorHandler(context.getString(R.string.error_network_unknown))
                    }
                }

                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }


    override fun handleOwnerRegisterError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        409 -> ErrorHandler(context.getString(R.string.error_company_number_duplicated))
                        else -> ErrorHandler(context.getString(R.string.error_network_unknown))
                    }
                }

                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }

    override fun handleSendSmsError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
    }

    override fun handleExistsAccountError(throwable: Throwable): Pair<Boolean?, ErrorHandler> {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    400 -> null to ErrorHandler(context.getString(R.string.error_invalid_phone_number))
                    409 -> true to ErrorHandler(context.getString(R.string.error_account_duplicated))
                    else -> null to ErrorHandler(context.getString(R.string.error_network_unknown))
                }
            }

            else -> {
                null to unknownErrorHandler(context)
            }
        }
    }

    override fun handleFindPasswordError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        400 -> ErrorHandler(context.getString(R.string.error_invalid_phone_number))
                        404 -> ErrorHandler(context.getString(R.string.error_account_not_exist))
                        else -> ErrorHandler(context.getString(R.string.error_network_unknown))
                    }
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }
}
