package `in`.koreatech.koin.data.error

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.unknownErrorHandler
import `in`.koreatech.koin.domain.error.dept.DeptErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeptErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeptErrorHandler {
    override fun getDeptNameFromDeptCodeError(throwable: Throwable) =
        throwable.handleCommonError(context) {
            ErrorHandler(context.getString(R.string.error_invalid_student_number))
        }


    override fun getDeptsError(throwable: Throwable) =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
}
