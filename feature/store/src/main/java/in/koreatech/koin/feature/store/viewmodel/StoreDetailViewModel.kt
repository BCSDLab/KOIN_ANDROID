package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.feature.store.view.StoreDetailSideEffect
import `in`.koreatech.koin.feature.store.view.StoreDetailState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getStoreReviewUseCase: GetStoreReviewUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase,
) : ViewModel(), ContainerHost<StoreDetailState, StoreDetailSideEffect> {
    override val container =
        container<StoreDetailState, StoreDetailSideEffect>(StoreDetailState()) {
            val storeId = savedStateHandle.get<Int>(STORE_ID)
            checkNotNull(storeId)
            fetchStore(storeId)
            fetchMenus(storeId)
            fetchReview(storeId)
            checkToken()
        }

    private fun fetchStore(id: Int) = intent {
        getStoreWithMenuUseCase(id).also { result ->
            reduce {
                state.copy(
                    store = result,
                    isLoading = false,
                )
            }
        }
    }

    private fun fetchMenus(id: Int) = intent {
        getShopMenusUseCase(id).also { shop ->
            reduce {
                state.copy(
                    categories = if(shop.menuCategories?.isNotEmpty() == true) {
                        state.categories.copy(
                            menuCategories = shop.menuCategories?.map { category ->
                                if(shop.menuCategories?.indexOf(category) == 0) {
                                    category.copy(isChecked = true)
                                } else
                                category.copy(isChecked = false)
                            }
                        )
                    } else {
                        state.categories.copy(menuCategories = emptyList())
                    },
                )
            }
        }
    }

    private fun checkToken() = intent {
        reduce { state.copy(isLoading = true) }
        val hasToken = isTokenSavedInDeviceUseCase()
        if (hasToken) {
            val (user, error) = getUserInfoUseCase()
            reduce {
                state.copy(
                    isLogin = user != null && error == null,
                    isLoading = false
                )
            }
        } else {
            reduce {
                state.copy(
                    isLogin = false,
                    isLoading = false
                )
            }
        }
    }

    private fun fetchReview(storeId: Int) = intent {
        getStoreReviewUseCase(storeId).also { reviews ->
            reduce {
                state.copy(
                    storeReview = reviews,
                )
            }
        }
    }

    fun clickMenuCategory(categoryId: Int) = blockingIntent {
        val newCategories = state.categories.menuCategories?.map { category ->
            if (category.id == categoryId) {
                category.copy(isChecked = true)
            } else {
                category.copy(isChecked = false)
            }
        } ?: emptyList()

        reduce {
            state.copy(
                categories = state.categories.copy(menuCategories = newCategories)
            )
        }
    }

    companion object {
        const val STORE_ID = "storeId"
    }
}
