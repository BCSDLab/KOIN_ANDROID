package `in`.koreatech.business.feature.signup.businessauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetStoresUseCase
import kotlinx.coroutines.Dispatchers
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
) : ContainerHost<SearchStoreState, SearchStoreSideEffect>, ViewModel() {
    override val container =
        container<SearchStoreState, SearchStoreSideEffect>(SearchStoreState())

    init {
        searchStore()
    }
    fun onItemIndexChange(index: Int) = intent {
        reduce {
            state.copy(itemIndex = index)
        }
    }

    fun onSearchChanged(search: String) = intent {
        reduce {
            state.copy(search = search)
        }
    }


    fun searchStore() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val newSearchJob = launch {
                getStoresUseCase(null, state.search).let { stores ->
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

    fun onSearchStore()= intent {
        postSideEffect(SearchStoreSideEffect.SearchStore(state.search))
    }

    fun onNavigateToBackScreen() = intent {
        postSideEffect(SearchStoreSideEffect.NavigateToBackScreen)
    }

}