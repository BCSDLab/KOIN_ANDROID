package `in`.koreatech.koin.ui.business.mystore.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.constant.MyStoreEnum
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class MyStoreViewModel @Inject constructor(

): BaseViewModel() {
    private val _myStoreState = SingleLiveEvent<MyStoreEnum>()
    val myStoreState: SingleLiveEvent<MyStoreEnum> get() =_myStoreState

    fun changeMyStoreState(state: MyStoreEnum){
        _myStoreState.value = state
    }
}

