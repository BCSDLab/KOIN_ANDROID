package `in`.koreatech.koin.ui.business.registerstore.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.business.mystore.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalTime
import javax.inject.Inject

class RegisterStoreViewModel @Inject constructor(): BaseViewModel() {


    private val _basicInfo = MutableLiveData<RegisterStoreBasic?>(null)
    val basicInfo: LiveData<RegisterStoreBasic?> get() = _basicInfo

    private val _detailInfo = MutableLiveData<RegisterStoreDetail?>(null)
    val detailInfo: LiveData<RegisterStoreDetail?> get() = _detailInfo

    private val _storeTime = MutableLiveData<RegisterStoreTime?>(null)
    val storeTime: LiveData<RegisterStoreTime?> get() = _storeTime

    private val _category = MutableLiveData<String?>(null)
    val category: LiveData<String?> get() = _category

    private val _registerStore = MutableLiveData<RegisterStore>(null)
    val registerStore: LiveData<RegisterStore> get() = _registerStore

    init{
        _storeTime.value = RegisterStoreTime("00:00", "00:00")
    }

    fun setCategory(storeCategory: String?) {
      _category.value = storeCategory
    }

    // 카테고리

    fun setBasicInfo(basicInfo: RegisterStoreBasic){
        _basicInfo.value = basicInfo
    }

    //기본 정보
    fun setDetailInfo(detailInfo: RegisterStoreDetail?){
        _detailInfo.value = detailInfo
    }
    // 세부정보(시간 제외)

    fun setTimeInfo(time: RegisterStoreTime){
        _storeTime.value = time
    }

    fun getTimeString(): String {
        return _storeTime.value!!.openTime.toString() + " ~ " + _storeTime.value!!.closeTime.toString()
    }

    fun getHoliday():String{
        var holidayMessage: String = "매주 "
        var checkHoliday: Boolean = false

        for (status in Holiday.values()) {
            if(status.isHoliday){
                holidayMessage += status.day + " "
                checkHoliday = true
            }
        }

        return if(checkHoliday){
            holidayMessage += " 정기 휴무"
            holidayMessage
        } else{
            "일주일 내내 운영합니다."
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setRegisterStore(){
        _registerStore.value = RegisterStore(_basicInfo.value!!.name, _category.value, _basicInfo.value!!.address, _basicInfo.value!!.imageUri,
        _detailInfo.value!!.phoneNumber, _detailInfo.value!!.deliveryPrice, _detailInfo.value!!.description, getHoliday(),
            LocalTime.parse(_storeTime.value!!.openTime), LocalTime.parse(_storeTime.value!!.closeTime),
            _detailInfo.value!!.isDeliveryOk, _detailInfo.value!!.isCardOk, _detailInfo.value!!.isBankOk)
    }
}