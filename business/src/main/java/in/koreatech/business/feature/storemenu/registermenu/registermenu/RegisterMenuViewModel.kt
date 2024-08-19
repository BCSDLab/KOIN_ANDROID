package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.menu.GetMenuCategoryUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
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
        addDefaultImage()
    }

    private fun getStoreMenuCategory(){
        intent {
            viewModelScope.launch {
                val storeId = 139
                val menuCategory = getMenuCategoryUseCase(storeId)
                reduce {
                    state.copy(
                        menuCategory = menuCategory
                    )
                }
            }
        }
    }

    private fun addDefaultImage(){
        intent{
            reduce {
                val newMenuUriList = state.imageUriList.toMutableList()
                newMenuUriList.add(Uri.EMPTY)
                state.copy(
                    imageUriList = newMenuUriList
                )
            }
        }
    }

    fun changeMenuImageUri(uriList: List<Uri>){
        intent {
            reduce {
                if(uriList.size < 3){
                    val newMenuUriList = state.imageUriList.toMutableList()

                    newMenuUriList.removeAt(newMenuUriList.lastIndex)

                    for(imageUri in uriList)
                        newMenuUriList.add(imageUri)

                    if(newMenuUriList.size != 3)newMenuUriList.add(Uri.EMPTY)

                    state.copy(
                        imageUriList = newMenuUriList
                    )
                }
                else{
                    state.copy(
                        imageUriList = uriList
                    )
                }
            }
        }
    }

    fun deleteMenuImageUri(index: Int) {
        intent {
            val newMenuUriList = state.imageUriList.toMutableList()
            newMenuUriList.removeAt(index)
            if(newMenuUriList.last() != Uri.EMPTY) {
                newMenuUriList.add(Uri.EMPTY)
            }
            reduce {
                    state.copy(
                        imageUriList = newMenuUriList
                    )
            }
        }
    }

    fun modifyMenuImageUri(modifyUri: Uri) {
        intent {
            val newMenuUriList = state.imageUriList.toMutableList()
            newMenuUriList[state.imageIndex] = modifyUri
            reduce {
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

    fun changeMenuPrice(price: String){
        intent{
            reduce {
                state.copy(menuPrice = price)
            }
        }
    }

    fun changeDetailMenuServing(index: Int, serving: String){
        intent{
            if (index in state.menuDetailPrice.indices) {
                reduce {
                    val newMenuPrice = state.menuDetailPrice.toMutableList()
                    newMenuPrice[index] = Pair(PriceHolder.PriceString(serving).priceString, newMenuPrice[index].second)
                    state.copy(menuDetailPrice = newMenuPrice)
                }
            }
        }
    }

    fun changeDetailMenuPrice(index: Int, price: String){
        intent{
            if (index in state.menuDetailPrice.indices) {
                reduce {
                    val newMenuPrice = state.menuDetailPrice.toMutableList()
                    newMenuPrice[index] = Pair(newMenuPrice[index].first ,PriceHolder.PriceString(price).priceString)
                    state.copy(menuDetailPrice = newMenuPrice)
                }
            }
        }
    }

    fun addPrice(){
        intent{
            reduce {
                val newMenuPrice = state.menuDetailPrice.toMutableList()
                newMenuPrice.add(Pair(PriceHolder.TempPrice.toString(), PriceHolder.TempPrice.toString()))
                state.copy(
                    menuDetailPrice = newMenuPrice
                )
            }
        }
    }

    fun deleteMenuPrice(index: Int){
        intent {
            val newMenuPrice = state.menuDetailPrice.toMutableList()
            newMenuPrice.removeAt(index)
            reduce {
                state.copy(
                    menuDetailPrice = newMenuPrice
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

    fun isImageModify(isModify: Boolean)=intent{
        reduce {
            state.copy(
                isModify = isModify
            )
        }
    }

    fun setImageIndex(index: Int)= intent{
        reduce {
            state.copy(
                imageIndex = index
            )
        }
    }

    fun changeMenuDetail(menuDetail: String) = intent{
        reduce {
            state.copy(
                description = menuDetail
            )
        }
    }

    fun onNextButtonClick(){
        intent{
            when {
                state.menuName.isBlank()-> postSideEffect(RegisterMenuSideEffect.ShowMessage(RegisterMenuErrorType.NullMenuName))
                state.menuPrice.isBlank() && state.menuDetailPrice.isEmpty()-> postSideEffect(RegisterMenuSideEffect.ShowMessage(RegisterMenuErrorType.NullMenuPrice))
                !(state.isMainMenu || state.isRecommendMenu || state.isSideMenu || state.isSetMenu)
                -> postSideEffect(RegisterMenuSideEffect.ShowMessage(RegisterMenuErrorType.NullMenuCategory))
                state.description.isBlank() -> postSideEffect(RegisterMenuSideEffect.ShowMessage(RegisterMenuErrorType.NullMenuDescription))
                state.imageUriList.size == 1 && state.imageUriList[0] == Uri.EMPTY -> postSideEffect(RegisterMenuSideEffect.ShowMessage(RegisterMenuErrorType.NullMenuImage))
            }
        }
    }
}