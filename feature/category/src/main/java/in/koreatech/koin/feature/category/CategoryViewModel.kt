package `in`.koreatech.koin.feature.category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.category.component.CategoryMenuId
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel(), ContainerHost<CategoryState, CategorySideEffect> {
    override val container = container<CategoryState, CategorySideEffect>(CategoryState()) { }

    fun onMenuClick(id: CategoryMenuId) = intent {
        postSideEffect(CategorySideEffect.NavigateToMenu(id))
    }
}
