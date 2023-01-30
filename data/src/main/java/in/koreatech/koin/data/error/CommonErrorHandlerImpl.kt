package `in`.koreatech.koin.data.error

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.domain.error.common.CommonErrorHandler
import `in`.koreatech.koin.domain.model.Result
import javax.inject.Inject

class CommonErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CommonErrorHandler {
    override fun handleError(throwable: Throwable): Result.Error =
        throwable.handleCommonError(context)
}