package `in`.koreatech.koin.ui.land.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.land.LandDetail
import `in`.koreatech.koin.domain.usecase.land.GetLandDetailUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.NaverMap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LandDetailViewModel @Inject constructor(
    private val landDetailUseCase: GetLandDetailUseCase
) : BaseViewModel() {
    lateinit var naverMap: NaverMap
    private val _landDetail = MutableLiveData<LandDetail?>()
    val landDetail: LiveData<LandDetail?>
        get() = _landDetail

    fun getLandDetail(id: Int) {
        viewModelScope.launchWithLoading {
            landDetailUseCase(id)
                .onSuccess {
                    _landDetail.value = it
                }
                .onFailure {
                    _landDetail.value = null
                }
        }
    }
}