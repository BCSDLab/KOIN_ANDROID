package `in`.koreatech.business.feature.insertstore.selectcategory

import `in`.koreatech.business.feature_changepassword.passwordauthentication.ErrorType
import `in`.koreatech.business.feature_changepassword.passwordauthentication.PasswordAuthenticationSideEffect

sealed  class SelectCategoryScreenSideEffect {
    data class GotoChangePasswordScreen(val email: String): SelectCategoryScreenSideEffect()
    object SendAuthCode: SelectCategoryScreenSideEffect()
    data class ShowMessage(val type: ErrorType): SelectCategoryScreenSideEffect()
}