package `in`.koreatech.business.feature.insertstore.selectcategory

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.OwnerChangePasswordUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class SelectCategoryScreenViewModel @Inject constructor(
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase
) : ViewModel(), ContainerHost<SelectCategoryScreenState, SelectCategoryScreenSideEffect>{
    override val container = container<SelectCategoryScreenState,SelectCategoryScreenSideEffect>(SelectCategoryScreenState())


    init {
        getCategory()
    }

    fun chooseCategory(categoryId: Int) = intent{
            reduce{
                state.copy(
                    categoryId = categoryId
                )
            }
        categoryIdIsValid()
    }

    fun goToInsertBasicInfoScreen(){
        intent {
            if(state.categoryIdIsValid){
                postSideEffect(SelectCategoryScreenSideEffect.NavigateToInsertBasicInfoScreen(state.categoryId))
            }
            else{
                postSideEffect(SelectCategoryScreenSideEffect.NotSelectCategory)
            }
        }
    }
    private fun categoryIdIsValid(){
        intent{
            reduce{
                state.copy(
                    categoryIdIsValid = (state.categoryId != -1)
                )
            }
        }
    }
    private fun getCategory() {
        intent {
            viewModelScope.launch {
                val categories = getStoreCategoriesUseCase().drop(1)
                reduce {
                    state.copy(
                        categories = categories
                    )
                }
            }
        }
    }
}