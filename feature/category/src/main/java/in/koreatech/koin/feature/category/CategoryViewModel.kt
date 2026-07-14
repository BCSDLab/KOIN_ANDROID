package `in`.koreatech.koin.feature.category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.feature.category.component.CategoryMenuId
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : ViewModel(), ContainerHost<CategoryState, CategorySideEffect> {
    override val container = container<CategoryState, CategorySideEffect>(CategoryState()) {
        checkLoginStatus()
    }

    private fun checkLoginStatus() = intent {
        val isTokenSaved = isTokenSavedInDeviceUseCase()
        reduce { state.copy(isAnonymous = !isTokenSaved) }
    }

    fun onMenuClick(id: CategoryMenuId) = intent {
        postSideEffect(CategorySideEffect.NavigateToMenu(id))
    }
}
