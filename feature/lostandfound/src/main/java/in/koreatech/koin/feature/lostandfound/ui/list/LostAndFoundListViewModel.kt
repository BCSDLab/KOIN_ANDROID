package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class LostAndFoundListViewModel @Inject constructor() : ViewModel(), ContainerHost<LostAndFoundListState, Nothing> {
    override val container = container<LostAndFoundListState, Nothing>(
        initialState = LostAndFoundListState()
    )

    fun setShowFilterBottomSheet(value: Boolean) = intent {
        reduce {
            state.copy(
                showFilterBottomSheet = value
            )
        }
    }

    fun setShowWriteBottomSheet(value: Boolean) = intent {
        reduce {
            state.copy(
                showWriteBottomSheet = value
            )
        }
    }
}
