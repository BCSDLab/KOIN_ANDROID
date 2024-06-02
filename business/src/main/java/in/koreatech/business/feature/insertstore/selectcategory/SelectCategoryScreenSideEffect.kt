package `in`.koreatech.business.feature.insertstore.selectcategory

import `in`.koreatech.business.feature_changepassword.passwordauthentication.ErrorType
import `in`.koreatech.business.feature_changepassword.passwordauthentication.PasswordAuthenticationSideEffect

sealed class SelectCategoryScreenSideEffect {
    data class NavigateToInsertBasicInfoScreen(val categoryId: Int): SelectCategoryScreenSideEffect()
    object NotSelectCategory: SelectCategoryScreenSideEffect()
}