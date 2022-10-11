package `in`.koreatech.koin.ui.land.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.usecase.land.GetLandListUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LandViewModel @Inject constructor(
    private val landListUseCase: GetLandListUseCase
) : BaseViewModel() {
    lateinit var naverMap: NaverMap
    val markerList = mutableListOf<Marker>()
    private val _landData = MutableLiveData<List<Land>>()
    val landData: LiveData<List<Land>> get() = _landData

    fun getLandList() {
        viewModelScope.launchWithLoading {
            landListUseCase()
                .onSuccess {
                    _landData.value = it
                }
                .onFailure {
                    _landData.value = listOf()
                }
        }
    }
}