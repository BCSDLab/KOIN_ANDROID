package `in`.koreatech.koin.ui.userinfo.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNameFromStudentIdUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckNicknameValidationUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateUserInfoUseCase
import `in`.koreatech.koin.ui.userinfo.state.NicknameState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoEditViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deptNameFromStudentIdUseCase: GetDeptNameFromStudentIdUseCase,
    private val checkNicknameValidationUseCase: CheckNicknameValidationUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
) : BaseViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _dept = MutableLiveData<String>()
    val dept: LiveData<String> get() = _dept

    private val _getDeptErrorMessage = MutableLiveData<String>()
    val getDeptErrorMessage: LiveData<String> get() = _getDeptErrorMessage

    private val _nicknameState = MutableLiveData<NicknameState>()
    val nicknameState: LiveData<NicknameState> get() = _nicknameState

    private val _nicknameDuplicatedEvent = SingleLiveEvent<Boolean>()
    val nicknameDuplicatedEvent: LiveData<Boolean> get() = _nicknameDuplicatedEvent

    private val _toastErrorMessage = SingleLiveEvent<String>()
    val toastErrorMessage: LiveData<String> get() = _toastErrorMessage

    private val _userInfoEditedEvent = SingleLiveEvent<Unit>()
    val userInfoEditedEvent: LiveData<Unit> get() = _userInfoEditedEvent

    fun getUserInfo() = viewModelScope.launchWithLoading {
        getUserInfoUseCase()
            .onSuccess { user ->
                _user.value = user
                _nicknameState.value = user?.nickname?.let {
                    NicknameState(it, false)
                } ?: NicknameState.newNickname("")
            }.onFailure {
                _toastErrorMessage.value = it
            }
    }

    fun getDept(studentId: String) = viewModelScope.launchIgnoreCancellation {
        deptNameFromStudentIdUseCase(studentId).onSuccess {
            _dept.value = it
        }.onFailure {
            _getDeptErrorMessage.value = it
        }
    }

    fun checkNickname(nickname: String) = viewModelScope.launchWithLoading {
        _nicknameState.value = NicknameState.newNickname(nickname)

        checkNicknameValidationUseCase(nickname)
            .onSuccess {
                _nicknameState.value = _nicknameState.value?.copy(isNicknameDuplicated = it)
                _nicknameDuplicatedEvent.value = it
            }.onFailure {
                _toastErrorMessage.value = it
            }
    }

    fun setNickname(nickname: String) {
        _nicknameState.value = NicknameState.newNickname(nickname)
    }

    fun updateUserInfo(
        name: String,
        nickname: String,
        separatedPhoneNumber: List<String>?,
        gender: Gender?,
        studentId: String
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                if (user.value != null) {
                    updateUserInfoUseCase(
                        beforeUser = user.value!!,
                        name = name,
                        nickname = nickname,
                        separatedPhoneNumber = separatedPhoneNumber,
                        gender = gender,
                        studentId = studentId,
                        checkedEmailValidation = nicknameState.value?.let {
                            it.nickname == nickname && it.isNicknameDuplicated == false
                        } ?: false
                    ).onSuccess {
                        _userInfoEditedEvent.call()
                    }.onFailure {
                        _toastErrorMessage.value = it
                    }
                }
            }
        }

    }
}