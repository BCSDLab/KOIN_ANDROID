package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.PasswordFormat
import `in`.koreatech.koin.domain.util.regex.PasswordUtil
import javax.inject.Inject

class VerifyPasswordFormatUseCase @Inject constructor() {
    operator fun invoke(password: String): PasswordFormat {
        return PasswordFormat(
            isIncludeEnglish = PasswordUtil().isContainAlphabet(password),
            isIncludeNumber = PasswordUtil().isContainNumber(password),
            isIncludeSymbol = PasswordUtil().isContainSymbol(password),
            isValidLength = password.length in 6..18
        )
    }
}