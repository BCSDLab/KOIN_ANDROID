package `in`.koreatech.koin.data.error

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.domain.error.dept.DeptErrorHandler
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result

class DeptErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeptErrorHandler {
    override fun getDeptNameFromDeptCodeError(throwable: Throwable) =
        throwable.handleCommonError(context) {
            Result.Error(context.getString(R.string.error_invalid_student_number))
        }


    override fun getDeptsError(throwable: Throwable) =
        throwable.handleCommonError(context)
}
