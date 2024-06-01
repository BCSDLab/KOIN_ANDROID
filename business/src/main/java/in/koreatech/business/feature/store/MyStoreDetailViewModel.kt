package `in`.koreatech.business.feature.store


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class MyStoreDetailViewModel @Inject constructor(
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase
) : ContainerHost<MyStoreDetailState, MyStoreDetailSideEffect>, ViewModel() {
    override val container = container<MyStoreDetailState, MyStoreDetailSideEffect>(MyStoreDetailState())
    fun getStoreWithMenu(storeId: Int) {
        val test=11
        viewModelScope.launch {
            getStoreWithMenuUseCase(test).also { store ->
                intent {
                    reduce { state.copy(storeInfo = store)}
                }
            }
        }
    }
}