package `in`.koreatech.koin.ui.businesssignup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BusinessSignUpBaseViewModel: BaseViewModel() {
    private val _toBeShownFragment = MutableLiveData<String>()
    val toBeShownFragment: LiveData<String> get() = _toBeShownFragment

    private val _saveFileUrls = MutableLiveData<List<String>>()
    val saveFileUrls get() = _saveFileUrls

    private val _saveCompanyNumber = MutableLiveData<String>()
    val saveCompanyNumber get() = _saveCompanyNumber

    private val _saveEmail = MutableLiveData<String>()
    val saveEmail get() = _saveEmail

    private val _savePassword = MutableLiveData<String>()
    val savePassword get() = _savePassword

    private val _saveName = MutableLiveData<String>()
    val saveName get() = _saveName

    private val _savePhoneNumber = MutableLiveData<String>()
    val savePhoneNumber get() = _savePhoneNumber

    private val _saveShopId = MutableLiveData<Int>()
    val saveShopId get() = _saveShopId

    private val _saveShopName = MutableLiveData<String>()
    val saveShopName get() = _saveShopName

    fun setFragmentTag(tag: String) {
        viewModelScope.launch {
            _toBeShownFragment.value = tag
        }
    }

    fun setFileUrls(url: String) {
        viewModelScope.launch {
            val list = saveFileUrls.value?.toMutableList() ?: mutableListOf()
            list.add(url)

            _saveFileUrls.value = list
        }
    }

    fun setCompanyNumber(number: String) {
        viewModelScope.launch {
            _saveCompanyNumber.value = number
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch {
            _saveEmail.value = email
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            _saveName.value = name
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _savePassword.value = password
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _savePhoneNumber.value = phoneNumber
        }
    }

    fun setShopId(shopId: Int) {
        viewModelScope.launch {
            _saveShopId.value = shopId
        }
    }

    fun setShopName(shopName: String) {
        viewModelScope.launch {
            _saveShopName.value = shopName
        }
    }
}