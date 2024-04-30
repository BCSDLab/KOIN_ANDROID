package `in`.koreatech.business.feature.signup.businessauth

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.store.GetStoresUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchStoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
) : ContainerHost<SearchStoreState, SearchStoreSideEffect>, BaseViewModel() {
    override val container =
        container<SearchStoreState, SearchStoreSideEffect>(SearchStoreState())

    fun onItemIndexChange(index: Int) = intent {
        reduce {
            state.copy(itemIndex = index)
        }
    }

    fun onSearchChanged(search: String) = intent {
       postSideEffect(SearchStoreSideEffect.SearchStore(search))
    }

    fun onNavigateToBackScreen() = intent {
        postSideEffect(SearchStoreSideEffect.NavigateToBackScreen)
    }

    fun onNavigateToNextScreen(shopId: Int, shopName: String) = intent {
        postSideEffect(SearchStoreSideEffect.NavigateToNextScreen(shopId, shopName))
    }

    init {
        intent {
            viewModelScope.launch(Dispatchers.IO) {
                state.searchJob?.cancel()
                val newSearchJob = launch {

                    getStoresUseCase(null, "").let { stores ->
                        reduce {
                            state.copy(stores = stores)
                        }
                    }
                }
                reduce {
                    state.copy(searchJob = newSearchJob)
                }

            }
        }
    }
}