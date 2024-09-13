package `in`.koreatech.koin.data.error

import android.content.Context
import retrofit2.HttpException
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.unknownErrorHandler
import `in`.koreatech.koin.data.util.withUnknown
import `in`.koreatech.koin.domain.error.coopshop.CoopShopErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import javax.inject.Inject

class CoopShopErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): CoopShopErrorHandler {
    override fun handleGetCoopShopAllError(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }

    override fun handleGetCoopShopError(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            when (it) {
                is HttpException -> {
                    when (it.code()) {
                        404 -> ErrorHandler(it.message())
                        else -> ErrorHandler(context.getString(R.string.error_network))
                    }
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
}