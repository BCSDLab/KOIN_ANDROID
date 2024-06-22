package `in`.koreatech.business.feature.store


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyStoreDetailViewModel @Inject constructor(
) : ContainerHost<MyStoreDetailState, MyStoreDetailSideEffect>, ViewModel() {
    override val container =
        container<MyStoreDetailState, MyStoreDetailSideEffect>(MyStoreDetailState())

    fun initEventItem() = intent {
        reduce {
            state.copy(isEventExpanded = state.isEventExpanded.mapIndexed { _, _ -> false })
        }
    }

    fun toggleEventItem(index: Int) = intent {
        reduce {
            state.copy(
                isEventExpanded = state.isEventExpanded.mapIndexed { i, isExpanded ->
                    if (i == index) !isExpanded else isExpanded
                }
            )
        }
    }
}