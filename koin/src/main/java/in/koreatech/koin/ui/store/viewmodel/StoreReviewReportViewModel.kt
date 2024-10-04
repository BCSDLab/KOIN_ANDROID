package `in`.koreatech.koin.ui.store.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.StoreReport
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

    val storeReport: LiveData<ArrayList<StoreReport>> get() = _storeReport
    private val _storeReport = MutableLiveData<ArrayList<StoreReport>>()

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

        val reportList:ArrayList<StoreReport> = ArrayList<StoreReport>()

        if(_isNotRelation.value == true){
            reportList.add(
                StoreReport(
                    title = "주제에 맞지 않음",
                    content = "해당 음식점과 관련 없는 리뷰입니다."
                ))
        }

        if(_isSpam.value == true){

            reportList.add(
                StoreReport(
                    title = "스팸",
                    content = "광고가 포함된 리뷰입니다"
                ))
        }

        if(_isAbuse.value == true){

            reportList.add(
                StoreReport(
                    title = "욕설",
                    content = "욕설, 성적인 언어, 비방하는 글이 포함된 리뷰입니다."
                ))
        }

        if(_isPrivate.value == true){

            reportList.add(
                StoreReport(
                    title = "개인정보",
                    content = "개인정보가 포함된 리뷰입니다."
                ))
        }

        if(_isEtc.value == true){
            reportList.add(
                StoreReport(
                    title = "기타",
                    content = etcReason
                ))
        }

        _storeReport.value = reportList

        viewModelScope.launch {
            reportStoreReviewUseCase(
                storeId = storeId,
                reviewId = reviewId,
                reportList = _storeReport.value,
                isNotRelation = _isNotRelation.value,
                isSpam = _isSpam.value,
                isAbuse = _isAbuse.value,
                isPrivate = _isPrivate.value,
                isEtc = _isEtc.value,
                etcReason = etcReason
            ).onSuccess {
                _storeReviewState.emit(it)
            }.onFailure{
                _storeReviewExceptionState.emit(it)
            }
        }
    }

}