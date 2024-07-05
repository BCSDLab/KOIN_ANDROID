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
            when(it) {
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
            when(it) {
                is HttpException -> {
                    ErrorHandler(context.getString(R.string.error_owner_info))
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }
}
