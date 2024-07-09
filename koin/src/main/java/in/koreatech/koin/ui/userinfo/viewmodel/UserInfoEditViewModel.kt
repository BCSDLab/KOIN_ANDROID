package `in`.koreatech.koin.ui.userinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNamesUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckNicknameValidationUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateStudentUserInfoUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.userinfo.state.NicknameCheckState
import `in`.koreatech.koin.ui.userinfo.state.NicknameState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserInfoEditViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val checkNicknameValidationUseCase: CheckNicknameValidationUseCase,
    private val updateStudentUserInfoUseCase: UpdateStudentUserInfoUseCase,
    private val getDeptNamesUseCase: GetDeptNamesUseCase,
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _getDeptErrorMessage = MutableLiveData<String>()
    val getDeptErrorMessage: LiveData<String> get() = _getDeptErrorMessage

    private val _nicknameState = MutableLiveData<NicknameState>()
    val nicknameState: LiveData<NicknameState> get() = _nicknameState

    private val _nicknameDuplicatedEvent = SingleLiveEvent<NicknameCheckState>()
    val nicknameDuplicatedEvent: LiveData<NicknameCheckState> get() = _nicknameDuplicatedEvent

    private val _toastErrorMessage = SingleLiveEvent<String>()
    val toastErrorMessage: LiveData<String> get() = _toastErrorMessage

    private val _userInfoEditedEvent = SingleLiveEvent<Unit>()
    val userInfoEditedEvent: LiveData<Unit> get() = _userInfoEditedEvent

    val depts: StateFlow<Pair<List<String>, String?>> = flow { emit(getDeptNamesUseCase()) }
        .combine(user.asFlow()) { depts, user -> depts to user }
        .filter { it.second.isStudent }
        .map { it.first to (it.second as User.Student).major }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf<String>() to null)

    fun getUserInfo() = viewModelScope.launchWithLoading {
        getUserInfoUseCase()
            .onSuccess { user ->
                _user.value = user
                _nicknameState.value = when (user) {
                    User.Anonymous -> NicknameState.newNickname("")
                    is User.Student -> {
                        user.nickname?.let {
                            NicknameState(it, false)
                        } ?: NicknameState.newNickname("")
                    }
                }
            }.onFailure {
                _toastErrorMessage.value = it.message
            }
    }

    fun checkNickname(nickname: String) = viewModelScope.launchWithLoading {
        _nicknameState.value = NicknameState.newNickname(nickname)

        if (nickname == (user.value as? User.Student)?.nickname) {
            _nicknameState.value = _nicknameState.value?.copy(isNicknameDuplicated = false)
            _nicknameDuplicatedEvent.value = NicknameCheckState.SAME_AS_BEFORE
            return@launchWithLoading
        }

        checkNicknameValidationUseCase(nickname).let { (isDuplicated, error) ->
            isDuplicated?.let {
                _nicknameState.value = _nicknameState.value?.copy(isNicknameDuplicated = it)
                _nicknameDuplicatedEvent.value = if (it) NicknameCheckState.EXIST else NicknameCheckState.POSSIBLE
            }
            error?.let { _toastErrorMessage.value = it.message }
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
        studentId: String,
        major: String
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                user.value?.let { user ->
                    updateStudentUserInfoUseCase(
                        beforeUser = user,
                        name = name,
                        nickname = nickname,
                        separatedPhoneNumber = separatedPhoneNumber,
                        gender = gender,
                        studentId = studentId,
                        major = major,
                        checkedEmailValidation = nicknameState.value?.let {
                            it.nickname == nickname && it.isNicknameDuplicated == false
                        } ?: false
                    )?.let { errorHandler ->
                        _toastErrorMessage.value = errorHandler.message
                    } ?: _userInfoEditedEvent.call()
                }
            }
        }

    }
}