package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.menu.GetMenuCategoryUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RegisterMenuViewModel @Inject constructor(
    private val getMenuCategoryUseCase : GetMenuCategoryUseCase
): ViewModel(), ContainerHost<RegisterMenuState, RegisterMenuSideEffect> {
    override val container = container<RegisterMenuState,RegisterMenuSideEffect>(RegisterMenuState())


    init {
        getStoreMenuCategory()
        addPrice()
        addImage()
    }

    private fun getStoreMenuCategory(){
        intent {
            viewModelScope.launch {
                val menuCategory = getMenuCategoryUseCase(197)
                reduce {
                    state.copy(
                        menuCategory = menuCategory
                    )
                }
            }
        }
    }

    private fun addImage(){
        intent{
            reduce {
                val newMenuUriList = state.imageUriList.toMutableList()
                newMenuUriList.add("")
                state.copy(
                    imageUriList = newMenuUriList
                )
            }
        }
    }

    fun changeMenuName(menuName: String) = intent{
        reduce {
            state.copy(
                menuName = menuName
            )
        }
    }

    fun changeMenuPrice(index: Int, price: String){
        intent{
            if (index in state.menuPrice.indices) {
                reduce {
                    val newMenuPrice = state.menuPrice.toMutableList()
                    newMenuPrice[index] = price
                    state.copy(menuPrice = newMenuPrice)
                }
            }
        }
    }

    fun addPrice(){
        intent{
            reduce {
                val newMenuPrice = state.menuPrice.toMutableList()
                newMenuPrice.add("")
                state.copy(
                    menuPrice = newMenuPrice
                )
            }
        }
    }

    fun recommendMenuIsClicked(){
        intent{
            reduce {
                state.copy(
                    isRecommendMenu = !state.isRecommendMenu
                )
            }
        }
    }

    fun mainMenuIsClicked(){
        intent{
            reduce {
                state.copy(
                    isMainMenu = !state.isMainMenu
                )
            }
        }
    }

    fun setMenuIsClicked(){
        intent{
            reduce {
                state.copy(
                    isSetMenu = !state.isSetMenu
                )
            }
        }
    }

    fun sideMenuIsClicked(){
        intent{
            reduce {
                state.copy(
                    isSideMenu = !state.isSideMenu
                )
            }
        }
    }

    fun changeMenuDetail(menuDetail: String) = intent{
        reduce {
            state.copy(
                description = menuDetail
            )
        }
    }
}