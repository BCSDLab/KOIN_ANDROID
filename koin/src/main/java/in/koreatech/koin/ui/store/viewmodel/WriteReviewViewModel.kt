package `in`.koreatech.koin.ui.store.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.usecase.store.WriteReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val writeReviewUseCase: WriteReviewUseCase,
): BaseViewModel() {
    private val _review = MutableStateFlow<Review?>(null)
    val review = _review.asStateFlow()


    fun writeReview(storeId: Int, content: Review) {
        viewModelScope.launch {
            writeReviewUseCase(storeId, content).also {
                _review.value = content
                Log.e("WriteReviewViewModel", "writeReview: $content")
            }
        }

    }

}
