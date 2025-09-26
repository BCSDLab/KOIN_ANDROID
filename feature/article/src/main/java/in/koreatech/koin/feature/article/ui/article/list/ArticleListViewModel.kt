package `in`.koreatech.koin.feature.article.ui.article.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val selectedTabIndex = savedStateHandle.getStateFlow(SELECTED_TAB, 0)

    fun setSelectedTabIndex(index: Int) {
        if (selectedTabIndex.value == index) return
        savedStateHandle[SELECTED_TAB] = index
    }

    companion object {
        private const val SELECTED_TAB = "selected_tab"
    }
}
