package `in`.koreatech.koin.data.error

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.withUnknown
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

class OwnerErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OwnerErrorHandler {
    override fun handleGetOwnerInfoError(throwable: Throwable): ErrorHandler {
        return throwable.handleCommonError(context) {
            when(it) {
                is HttpException -> {
                    when(it.code()) {
                        401 -> ErrorHandler("회원 정보가 없습니다.")
                        422 -> ErrorHandler("데이터 제약조건이 지켜지지 않았습니다.")
                        else -> ErrorHandler(context.getString(R.string.error_network))
                    }
                }
                else -> ErrorHandler(context.getString(R.string.error_network_unknown))
            }.withUnknown(context)
        }
    }
}