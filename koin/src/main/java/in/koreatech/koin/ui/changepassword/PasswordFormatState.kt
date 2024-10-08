package `in`.koreatech.koin.ui.changepassword

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.user.PasswordFormat
import kotlinx.parcelize.Parcelize


@Parcelize
data class PasswordFormatState(
    val isIncludeEnglish: Boolean = false,
    val isIncludeNumber: Boolean = false,
    val isIncludeSymbol: Boolean = false,
    val isValidLength: Boolean = false
) : Parcelable

fun PasswordFormat.toPasswordFormatState() = PasswordFormatState(
    isIncludeEnglish = isIncludeEnglish,
    isIncludeNumber = isIncludeNumber,
    isIncludeSymbol = isIncludeSymbol,
    isValidLength = isValidLength
)

val PasswordFormatState.isValidFormat: Boolean
    get() = isIncludeEnglish && isIncludeNumber && isIncludeSymbol && isValidLength