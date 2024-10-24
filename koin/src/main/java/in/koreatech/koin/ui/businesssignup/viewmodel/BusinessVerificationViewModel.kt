package `in`.koreatech.koin.ui.businesssignup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.owner.OwnerVerificationCodeUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BusinessVerificationViewModel @Inject constructor(
    private val ownerVerificationCodeUseCase: OwnerVerificationCodeUseCase
): BaseViewModel() {
    private val _businessVerificationContinuationState = SingleLiveEvent<Unit>()
    val businessVerificationContinuationState: LiveData<Unit>
        get() = _businessVerificationContinuationState

    private val _businessVerificationContinuationError = SingleLiveEvent<Throwable>()
    val businessVerificationContinuationError: LiveData<Throwable>
        get() = _businessVerificationContinuationError

    private val _signUpInfo = MutableLiveData<Triple<String, String, String>>()
    val signUpInfo: LiveData<Triple<String, String, String>> get() = _signUpInfo

    private lateinit var timer: Job

    private val _curTime = MutableLiveData<String>()
    val curTime: LiveData<String> get() = _curTime

    init {
        _curTime.value = "05:00"
    }

    fun continueVerificationEmail(
        email: String,
        verificationCode: String
    ) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                ownerVerificationCodeUseCase(
                    email, verificationCode
                ).onSuccess {
                    _businessVerificationContinuationState.value = it
                }.onFailure {
                    _businessVerificationContinuationError.value = it
                }
            }
        }
    }

    fun setSignUpInfo(email: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            _signUpInfo.value = Triple(email, password, passwordConfirm)
        }
    }

    fun startTimer() {
        if(::timer.isInitialized && !timer.isCancelled) timer.cancel()

        var delayTime = 10L
        var prevSystemMillis: Long
        var curSystemMillis: Long
        var timeError: Long
        var timeRemaining = TimeUnit.MINUTES.toMillis(5)

        timer = viewModelScope.launch {
            prevSystemMillis = System.currentTimeMillis()

            while(timeRemaining > 0L) {
                delay(delayTime)

                curSystemMillis = System.currentTimeMillis()
                timeError = curSystemMillis - 10L - prevSystemMillis
                delayTime -= timeError
                prevSystemMillis = curSystemMillis

                val curMinutes = TimeUnit.MILLISECONDS.toMinutes(timeRemaining)
                val curSeconds = TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - TimeUnit.MINUTES.toSeconds(curMinutes)
                _curTime.value = String.format("%02d:%02d", curMinutes, curSeconds)

                timeRemaining -= 10
            }
        }
    }
}