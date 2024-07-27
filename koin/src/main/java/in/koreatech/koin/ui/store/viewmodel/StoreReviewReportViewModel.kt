package `in`.koreatech.koin.ui.store.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.state.store.StoreReviewExceptionState
import `in`.koreatech.koin.domain.state.store.StoreReviewState
import `in`.koreatech.koin.domain.usecase.store.ReportStoreReviewUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreReviewReportViewModel @Inject constructor(
    private val reportStoreReviewUseCase: ReportStoreReviewUseCase
) : BaseViewModel() {
    private val _isNotRelation = MutableLiveData<Boolean>(false)
    val isNotRelation: LiveData<Boolean> get() = _isNotRelation

    private val _isSpam = MutableLiveData<Boolean>(false)
    val isSpam : LiveData<Boolean> get() = _isSpam

    private val _isAbuse = MutableLiveData<Boolean>(false)
    val isAbuse: LiveData<Boolean> get() = _isAbuse

    private val _isPrivate = MutableLiveData<Boolean>(false)
    val isPrivate: LiveData<Boolean> get() = _isPrivate

    private val _isEtc = MutableLiveData<Boolean>(false)
    val isEtc: LiveData<Boolean> get() = _isEtc

    private val _storeReviewState = MutableSharedFlow<StoreReviewState>()
    val storeReviewState: SharedFlow<StoreReviewState> = _storeReviewState.asSharedFlow()

    private val _storeReviewExceptionState = MutableSharedFlow<Throwable>()
    val storeReviewExceptionState: SharedFlow<Throwable> = _storeReviewExceptionState.asSharedFlow()

    fun reportReasonClicked(id: Int){
        when(id){
            0 ->{
                _isNotRelation.value = _isNotRelation.value != true
            }
            1 ->{
                _isSpam.value = _isSpam.value != true
            }
            2 ->{
                _isAbuse.value = _isAbuse.value != true
            }
            3 ->{
                _isPrivate.value =  _isPrivate.value != true
            }
            4 ->{
                _isEtc.value = _isEtc.value != true
            }
        }
    }

    fun reportReviewButtonClicked(storeId: Int?, reviewId: Int?, etcReason: String){
        var reportTitle: String = ""
        var reportContent: String = ""

        if(_isNotRelation.value == true){
            reportTitle += "주제에 맞지 않음 "
            reportContent += "해당 음식점과 관련 없는 리뷰입니다.\n"
        }

        if(_isSpam.value == true){
            reportTitle += "스팸 "
            reportContent += "광고가 포함된 리뷰입니다\n"
        }

        if(_isAbuse.value == true){
            reportTitle += "욕설 "
            reportContent += "욕설, 성적인 언어, 비방하는 글이 포함된 리뷰입니다.\n"
        }

        if(_isPrivate.value == true){
            reportTitle += "개인정보 "
            reportContent += "개인정보가 포함된 리뷰입니다.\n"
        }

        if(_isEtc.value == true){
            reportTitle += "주제에 맞지 않음 "
            reportContent += etcReason
        }

        viewModelScope.launch {
            reportStoreReviewUseCase(
                storeId = storeId,
                reviewId = reviewId,
                reportTitle = reportTitle,
                reportReason = reportContent,
                isNotRelation = _isNotRelation.value,
                isSpam = _isSpam.value,
                isAbuse = _isAbuse.value,
                isPrivate = _isPrivate.value,
                isEtc = _isEtc.value
            ).onSuccess {
                _storeReviewState.emit(it)
            }.onFailure{
                _storeReviewExceptionState.emit(it)
            }
        }
    }

}