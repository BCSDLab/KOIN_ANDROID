package `in`.koreatech.koin.feature.category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.feature.category.component.CategoryMenuId
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel(), ContainerHost<CategoryState, CategorySideEffect> {
    override val container = container<CategoryState, CategorySideEffect>(CategoryState()) {
        getUserInfo()
    }

    private fun getUserInfo() = intent {
        getUserInfoUseCase().onSuccess { user ->
            reduce { state.copy(isAnonymous = user.isAnonymous) }
        }.onFailure {
            Timber.e(it)
        }
    }

    fun onMenuClick(id: CategoryMenuId) = intent {
        postSideEffect(CategorySideEffect.NavigateToMenu(id))
    }
}
