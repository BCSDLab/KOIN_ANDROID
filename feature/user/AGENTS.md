# FEATURE User Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the FEATURE USER module of the KOIN_ANDROID repository.

## Module Overview

The `feature:user` module handles all user-related functionality including authentication, registration, profile management, and account settings. It follows Clean Architecture with Orbit MVI state management.

## Core Responsibilities

1. **Authentication**: Sign in, sign out, token management
2. **User Registration**: Student and general user registration flows  
3. **Profile Management**: View and edit user profile information
4. **Account Recovery**: Find ID, reset password
5. **Email/SMS Verification**: Verify user identity
6. **Session Management**: Maintain user session state

## Package Structure

```
feature/user/src/main/java/in/koreatech/koin/feature/user/
├── ui/
│   ├── signin/
│   │   ├── SignInScreen.kt
│   │   └── SignInViewModel.kt
│   ├── signup/
│   │   ├── SignUpActivity.kt              # Navigation host activity
│   │   ├── complete/
│   │   │   └── SignUpCompleteScreen.kt    # Registration complete screen
│   │   ├── navigation/
│   │   │   ├── SignUpNavigation.kt        # Compose navigation graph
│   │   │   └── SignUpNavType.kt           # Navigation arguments
│   │   ├── term/                          # Step 1: Terms acceptance
│   │   │   ├── SignUpTermScreen.kt
│   │   │   ├── SignUpTermViewModel.kt
│   │   │   ├── SignUpTermState.kt
│   │   │   └── SignUpTermSideEffect.kt
│   │   ├── verification/                  # Step 2: Phone verification
│   │   │   ├── SignUpVerification.kt
│   │   │   ├── SignUpVerificationViewModel.kt
│   │   │   ├── SignUpVerificationState.kt
│   │   │   └── SignUpVerificationSideEffect.kt
│   │   ├── usertype/
│   │   │   └── SignUpUserType.kt          # User type selection screen
│   │   └── userinfo/                      # Step 3: User information
│   │       ├── student/                   # Student registration
│   │       │   ├── SignUpStudentUserInfo.kt
│   │       │   ├── SignUpStudentViewModel.kt
│   │       │   ├── SignUpStudentState.kt
│   │       │   └── SignUpStudentSideEffect.kt
│   │       └── general/                   # General user registration
│   │           ├── SignUpGeneralUserInfo.kt
│   │           ├── SignUpGeneralViewModel.kt
│   │           ├── SignUpGeneralState.kt
│   │           └── SignUpGeneralSideEffect.kt
│   ├── findid/
│   │   ├── FindIdScreen.kt
│   │   └── FindIdViewModel.kt
│   ├── profile/
│   │   ├── ProfileScreen.kt
│   │   ├── ProfileEditScreen.kt
│   │   └── ProfileViewModel.kt
│   └── component/
│       ├── PasswordField.kt
│       ├── EmailVerificationField.kt
│       └── StudentIdField.kt
├── navigation/
│   └── UserNavigation.kt
└── model/
    ├── UserUiModel.kt
    ├── VerificationCodeState.kt
    └── VerificationMethodState.kt
```

## Implementation Patterns

### ⚠️ IMPORTANT: Legacy Error Handling Pattern

The user module uses the **legacy `Pair<T?, ErrorHandler?>` pattern** for error handling, NOT the modern `Result<T>` pattern. This requires:

1. Importing custom `onSuccess`/`onFailure` extensions from `in.koreatech.koin.domain.util`
2. In `onFailure`, the receiver `it` is `ErrorHandler`, NOT `Exception`
3. Password hashing is done **inside** `UserLoginUseCase`, NOT in the ViewModel

### Sign In ViewModel Pattern (ACTUAL IMPLEMENTATION)

**MUST** follow the existing pattern exactly:

```kotlin
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {
    override val container = container<SignInState, SignInSideEffect>(SignInState())

    private val _sessionId = MutableStateFlow("")
    val sessionId: StateFlow<String> = _sessionId

    fun setLoginId(loginId: String) {
        blockingIntent {
            reduce {
                state.copy(loginId = loginId, loginError = state.loginError.copy(isError = false))
            }
        }
    }

    fun setPassword(password: String) {
        blockingIntent {
            reduce {
                state.copy(password = password, loginError = state.loginError.copy(isError = false))
            }
        }
    }

    fun setShowPassword(showPassword: Boolean) {
        blockingIntent {
            reduce {
                state.copy(showPassword = showPassword)
            }
        }
    }

    // IMPORTANT: Uses LEGACY Pair<Unit?, ErrorHandler?> pattern
    // - onSuccess/onFailure are from domain.util.ErrorHandlerUtil
    // - In onFailure, 'it' is ErrorHandler, NOT Exception
    // - Password hashing is done inside UserLoginUseCase
    fun signIn() = intent {
        userLoginUseCase(state.loginId, state.password).onSuccess {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                "로그인 완료"
            )
            postSideEffect(SignInSideEffect.SignInSuccess)
        }.onFailure {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                "로그인 실패"
            )
            reduce {
                // 'it' is ErrorHandler, use it.message
                state.copy(loginError = SignInState.LoginError(true, it.message))
            }
        }
    }

    fun getSignUpSessionId() {
        _sessionId.value = getSessionIdUseCase(
            sessionName = "sign_up",
            isLoggedIn = false,
            shouldExpireOtherSessions = true
        )
    }
}
```

### SignInState (ACTUAL IMPLEMENTATION)

```kotlin
data class SignInState(
    val loginId: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginError: LoginError = LoginError()
) {
    data class LoginError(
        val isError: Boolean = false,
        val message: String = ""
    )
}
```

### SignInSideEffect (ACTUAL IMPLEMENTATION)

```kotlin
sealed class SignInSideEffect {
    data object SignInSuccess : SignInSideEffect()
}
```

**Key Differences from Modern Pattern**:
| Aspect | Modern `Result<T>` Pattern | Legacy `Pair<T?, ErrorHandler?>` Pattern |
|--------|---------------------------|------------------------------------------|
| Import | `kotlin.Result` | `in.koreatech.koin.domain.util.onSuccess/onFailure` |
| Error type | `Throwable` | `ErrorHandler` |
| Error access | `exception.message` | `it.message` (ErrorHandler) |
| Password hash | ViewModel hashes before calling | UseCase hashes internally |
| Token handling | ViewModel saves tokens | UseCase saves tokens internally |

**Rules**:
- **MUST** import `onSuccess`/`onFailure` from `in.koreatech.koin.domain.util`
- **MUST NOT** hash password in ViewModel - `UserLoginUseCase` does this internally
- **MUST NOT** manually save tokens - `UserLoginUseCase` does this internally
- **MUST** use `it.message` in `onFailure` block (ErrorHandler, not Exception)
- **MUST** log authentication events for analytics

### Sign Up Architecture Overview

The signup flow uses **4 separate ViewModels**, NOT a single monolithic ViewModel. Each ViewModel handles one step of the multi-screen registration process:

```
┌─────────────────┐    ┌──────────────────────┐    ┌─────────────────┐    ┌─────────────────────┐
│  SignUpTerm     │───▶│  SignUpVerification  │───▶│  SignUpUserType │───▶│  SignUpStudent/     │
│  ViewModel      │    │  ViewModel           │    │  (no ViewModel) │    │  GeneralViewModel   │
└─────────────────┘    └──────────────────────┘    └─────────────────┘    └─────────────────────┘
   Terms acceptance       Phone verification        Choose user type       Complete registration
```

**Key Design Decisions**:
- Each screen has its own ViewModel for single responsibility
- Data flows between screens via Compose Navigation arguments
- `SavedStateHandle` preserves state across process death

### SignUpTermViewModel (Step 1: Terms Acceptance)

Fetches and displays terms of service. Tracks checkbox state for required agreements.

```kotlin
@HiltViewModel
class SignUpTermViewModel @Inject constructor(
    private val getPrivacyTermTextUseCase: GetPrivacyTermTextUseCase,
    private val getKoinTermTextUseCase: GetKoinTermTextUseCase,
    private val getMarketingTermTextUseCase: GetMarketingTermTextUseCase
) : ViewModel(), ContainerHost<SignUpTermState, SignUpTermSideEffect> {
    override val container = container<SignUpTermState, SignUpTermSideEffect>(SignUpTermState())

    init {
        getPrivacyTerm()
        getKoinTerm()
        getMarketingTerm()
    }

    private fun getPrivacyTerm() = intent {
        getPrivacyTermTextUseCase().onSuccess {
            reduce { state.copy(privacyTerm = it) }
        }.onFailure {
            postSideEffect(SignUpTermSideEffect.FailedToFetchTerm)
        }
    }

    private fun getKoinTerm() = intent {
        getKoinTermTextUseCase().onSuccess {
            reduce { state.copy(koinTerm = it) }
        }.onFailure {
            postSideEffect(SignUpTermSideEffect.FailedToFetchTerm)
        }
    }

    private fun getMarketingTerm() = intent {
        getMarketingTermTextUseCase().onSuccess {
            reduce { state.copy(marketingTerm = it) }
        }.onFailure {
            postSideEffect(SignUpTermSideEffect.FailedToFetchTerm)
        }
    }

    fun setPrivacyTermState(isChecked: Boolean) = intent {
        reduce { state.copy(isPrivacyTermChecked = isChecked) }
    }

    fun setKoinTermState(isChecked: Boolean) = intent {
        reduce { state.copy(isKoinTermChecked = isChecked) }
    }

    fun setMarketingTermState(isChecked: Boolean) = intent {
        reduce { state.copy(isMarketingTermChecked = isChecked) }
    }
}

data class SignUpTermState(
    val privacyTerm: String = "",
    val koinTerm: String = "",
    val marketingTerm: String = "",
    val isPrivacyTermChecked: Boolean = false,
    val isKoinTermChecked: Boolean = false,
    val isMarketingTermChecked: Boolean = false
)

sealed class SignUpTermSideEffect {
    data object FailedToFetchTerm : SignUpTermSideEffect()
}
```

### SignUpVerificationViewModel (Step 2: Phone Verification)

Handles phone number verification via SMS. Uses `AccountTimer` for countdown.

```kotlin
@HiltViewModel
class SignUpVerificationViewModel @Inject constructor(
    private val checkPhoneNumberDuplicateUseCase: CheckPhoneNumberDuplicateUseCase,
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase
) : ViewModel(), ContainerHost<SignUpVerificationState, SignUpVerificationSideEffect> {
    override val container = container<SignUpVerificationState, SignUpVerificationSideEffect>(
        SignUpVerificationState()
    )

    fun setName(name: String) = blockingIntent {
        reduce { state.copy(name = name) }
    }

    fun setGender(gender: Int) = intent {
        reduce { 
            state.copy(gender = when (gender) {
                0 -> Gender.Man
                1 -> Gender.Woman
                else -> Gender.Unknown
            })
        }
    }

    fun setPhoneNumber(phoneNumber: String) = blockingIntent {
        if (phoneNumber == state.phoneNumber) return@blockingIntent
        reduce {
            state.copy(
                phoneNumber = phoneNumber,
                phoneNumberState = VerificationMethodState.None,
                verificationCode = "",
                verificationCodeState = VerificationCodeState.None,
                verificationTimeLeft = 180
            )
        }
        AccountTimer.cancel()
    }

    fun checkPhoneNumber() = intent {
        checkPhoneNumberDuplicateUseCase(state.phoneNumber).onSuccess {
            reduce { state.copy(phoneNumberState = VerificationMethodState.Available) }
            sendVerificationCode()
        }.onFailure {
            reduce {
                state.copy(
                    phoneNumberState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException -> VerificationMethodState.WrongFormat
                        is KoinUserException.PhoneNumberConflictException -> VerificationMethodState.AlreadySignedUp
                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
    }

    private fun sendVerificationCode() = intent {
        postSideEffect(SignUpVerificationSideEffect.StartTimer)
        requestSmsVerificationUseCase(state.phoneNumber).onSuccess {
            reduce {
                state.copy(
                    phoneNumberState = VerificationMethodState.Sent(
                        remainingCount = it.remainingCount,
                        totalCount = it.totalCount,
                        currentCount = it.currentCount
                    ),
                    verificationCodeState = VerificationCodeState.None
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    phoneNumberState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException -> VerificationMethodState.WrongFormat
                        is KoinUserException.PhoneNumberNotFoundException -> VerificationMethodState.NotFound
                        is KoinUserException.VerificationCodeRequestCountExceededException -> VerificationMethodState.CountExceeded
                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
    }

    fun checkVerificationCode() = intent {
        verifySmsCodeUseCase(state.phoneNumber, state.verificationCode).onSuccess {
            reduce { state.copy(verificationCodeState = VerificationCodeState.Valid) }
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.IDENTITY_VERIFICATION,
                "인증완료"
            )
            postSideEffect(SignUpVerificationSideEffect.StopTimer)
        }.onFailure {
            reduce {
                state.copy(
                    verificationCodeState = when (it) {
                        is KoinUserException.VerificationCodeInvalidException -> VerificationCodeState.NotValid
                        is KoinUserException.VerificationCodeExpiredException -> VerificationCodeState.Expired
                        else -> VerificationCodeState.None
                    }
                )
            }
        }
    }

    fun startTimer() {
        AccountTimer.start { secondsRemaining ->
            intent {
                reduce { state.copy(verificationTimeLeft = secondsRemaining) }
            }
            if (secondsRemaining <= 0) {
                stopTimer()
                intent { reduce { state.copy(verificationCodeState = VerificationCodeState.Expired) } }
            }
        }
    }

    fun stopTimer() = AccountTimer.cancel()
}

data class SignUpVerificationState(
    val name: String = "",
    val gender: Gender = Gender.Unknown,
    val phoneNumber: String = "",
    val phoneNumberState: VerificationMethodState = VerificationMethodState.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val verificationTimeLeft: Int = 180
)

// Computed properties
val SignUpVerificationState.isNameValid: Boolean
    get() = name.isValidName()

val SignUpVerificationState.currentStep: SignUpVerificationStep
    get() = when {
        phoneNumber.isNotEmpty() && phoneNumberState is VerificationMethodState.Sent -> SignUpVerificationStep.VERIFICATION_CODE
        name.isNotEmpty() && isNameValid && gender != Gender.Unknown -> SignUpVerificationStep.PHONE_NUMBER
        else -> SignUpVerificationStep.INITIAL
    }

val SignUpVerificationState.enabled: Boolean
    get() = verificationCodeState is VerificationCodeState.Valid && 
            name.isNotBlank() && isNameValid && 
            gender != Gender.Unknown && phoneNumber.isNotBlank()

enum class SignUpVerificationStep { INITIAL, PHONE_NUMBER, VERIFICATION_CODE }

sealed class SignUpVerificationSideEffect {
    data object StartTimer : SignUpVerificationSideEffect()
    data object StopTimer : SignUpVerificationSideEffect()
}
```

### SignUpStudentViewModel (Step 3a: Student Registration)

Handles student-specific registration with department, student number, and KOREATECH email.

```kotlin
@HiltViewModel
class SignUpStudentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val postStudentRegisterUseCase: PostStudentRegisterUseCase,
    private val checkLoginIdDuplicateUseCase: CheckLoginIdDuplicateUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val getDeptNamesUseCase: GetDeptNamesUseCase
) : ViewModel(), ContainerHost<SignUpStudentState, SignUpStudentSideEffect> {
    
    // Receives data from previous screen via SavedStateHandle
    override val container = container<SignUpStudentState, SignUpStudentSideEffect>(
        SignUpStudentState(), 
        savedStateHandle
    ) {
        val phoneNumber = savedStateHandle.get<String>(PHONE_NUMBER)
        val name = savedStateHandle.get<String>(NAME)
        val gender = savedStateHandle.get<String>(GENDER)
        checkNotNull(phoneNumber)
        checkNotNull(name)
        checkNotNull(gender)
        setInitData(phoneNumber, name, gender)
    }

    init {
        getDeptNames()
    }

    private fun getDeptNames() = intent {
        getDeptNamesUseCase().let {
            reduce { state.copy(majorList = it) }
        }
    }

    fun setLoginId(loginId: String) = blockingIntent {
        reduce {
            state.copy(
                loginId = loginId,
                isLoginIdValid = loginId.isValidLoginId(),
                isLoginIdAvailable = null
            )
        }
    }

    fun checkLoginIdDuplicate() = intent {
        checkLoginIdDuplicateUseCase(state.loginId).onSuccess {
            reduce { state.copy(isLoginIdAvailable = true, isLoginIdValid = true) }
            EventLogger.logClickEvent(EventAction.USER, AnalyticsConstant.Label.CREATE_ACCOUNT, "아이디생성")
        }.onFailure {
            when (it) {
                is KoinUserException.LoginIdInvalidException -> 
                    reduce { state.copy(isLoginIdAvailable = null, isLoginIdValid = false) }
                is KoinUserException.LoginIdConflictException -> 
                    reduce { state.copy(isLoginIdAvailable = false, isLoginIdValid = true) }
                else -> reduce { state.copy(isLoginIdAvailable = null, isLoginIdValid = false) }
            }
        }
    }

    fun checkNicknameDuplicate() = intent {
        checkNicknameDuplicateUseCase(state.nickname).onSuccess {
            reduce { state.copy(isNicknameAvailable = true) }
            EventLogger.logClickEvent(EventAction.USER, AnalyticsConstant.Label.CREATE_ACCOUNT, "닉네임생성")
        }.onFailure {
            reduce { state.copy(isNicknameAvailable = false) }
        }
    }

    fun signUp() = intent {
        checkEmailDuplicate()
        if (state.isEmailAvailable == false) return@intent
        postStudentRegisterUseCase(
            name = state.name,
            phoneNumber = state.phoneNumber,
            loginId = state.loginId,
            password = state.password,  // Password hashing done in UseCase
            gender = state.gender,
            email = if (state.email.isBlank()) "" else "${state.email}@$KOREATECH_EMAIL_DOMAIN",
            nickname = state.nickname,
            studentNumber = state.studentNumber,
            department = state.department
        ).onSuccess {
            postSideEffect(SignUpStudentSideEffect.SignUpSuccess)
        }.onFailure {
            postSideEffect(SignUpStudentSideEffect.SignUpFailure)
        }
    }
}

@Parcelize
data class SignUpStudentState(
    val phoneNumber: String = "",
    val name: String = "",
    val gender: String = "",
    val loginId: String = "",
    val isLoginIdAvailable: Boolean? = null,
    val isLoginIdValid: Boolean = false,
    val password: String = "",
    val passwordConfirm: String = "",
    val showPassword: Boolean = false,
    val department: String = "",
    val studentNumber: String = "",
    val isDropdownExpanded: Boolean = false,
    val isDepartmentSelected: Boolean = false,
    val nickname: String = "",
    val isNicknameAvailable: Boolean? = null,
    val email: String = "",
    val isEmailAvailable: Boolean? = null,
    val isSignUpSuccess: Boolean = false,
    val majorList: List<String> = emptyList()
) : Parcelable

// Computed properties
val SignUpStudentState.isPasswordValid
    get() = password.isValidPassword() && !password.containsKorean()

val SignUpStudentState.isPasswordEqual
    get() = password == passwordConfirm

val SignUpStudentState.currentStep: SignUpStudentStep
    get() = if (isPasswordValid && isPasswordEqual) SignUpStudentStep.NICKNAME_AND_EMAIL 
            else SignUpStudentStep.INITIAL

val SignUpStudentState.isEnabled
    get() = isNicknameValid && isPasswordValid && isPasswordEqual && 
            department.isNotEmpty() && isStudentNumberValid && isLoginIdAvailable == true

enum class SignUpStudentStep { INITIAL, NICKNAME_AND_EMAIL }

sealed class SignUpStudentSideEffect {
    data object SignUpSuccess : SignUpStudentSideEffect()
    data object SignUpFailure : SignUpStudentSideEffect()
}
```

### SignUpGeneralViewModel (Step 3b: General User Registration)

Handles non-student registration with simpler requirements (no department/student number).

```kotlin
@HiltViewModel
class SignUpGeneralViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val postGeneralRegisterUseCase: PostGeneralRegisterUseCase,
    private val checkLoginIdDuplicateUseCase: CheckLoginIdDuplicateUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase
) : ViewModel(), ContainerHost<SignUpGeneralState, SignUpGeneralSideEffect> {
    
    override val container = container<SignUpGeneralState, SignUpGeneralSideEffect>(
        SignUpGeneralState(), 
        savedStateHandle
    ) {
        val phoneNumber = savedStateHandle.get<String>(PHONE_NUMBER)
        val name = savedStateHandle.get<String>(NAME)
        val gender = savedStateHandle.get<String>(GENDER)
        checkNotNull(phoneNumber)
        checkNotNull(name)
        checkNotNull(gender)
        setInitData(phoneNumber, name, gender)
    }

    fun setLoginId(loginId: String) = blockingIntent {
        reduce {
            state.copy(
                loginId = loginId,
                isLoginIdValid = loginId.isValidLoginId(),
                isLoginIdAvailable = null
            )
        }
    }

    fun checkLoginIdDuplicate() = intent {
        checkLoginIdDuplicateUseCase(state.loginId).onSuccess {
            reduce { state.copy(isLoginIdAvailable = true, isLoginIdValid = true) }
            EventLogger.logClickEvent(EventAction.USER, AnalyticsConstant.Label.CREATE_ACCOUNT, "아이디생성")
        }.onFailure {
            when (it) {
                is KoinUserException.LoginIdInvalidException -> 
                    reduce { state.copy(isLoginIdAvailable = null, isLoginIdValid = false) }
                is KoinUserException.LoginIdConflictException -> 
                    reduce { state.copy(isLoginIdAvailable = false, isLoginIdValid = true) }
                else -> reduce { state.copy(isLoginIdAvailable = null, isLoginIdValid = false) }
            }
        }
    }

    fun checkNicknameDuplicate() = intent {
        checkNicknameDuplicateUseCase(state.nickname).onSuccess {
            reduce { state.copy(isNicknameAvailable = true) }
            EventLogger.logClickEvent(EventAction.USER, AnalyticsConstant.Label.CREATE_ACCOUNT, "닉네임생성")
        }.onFailure {
            reduce { state.copy(isNicknameAvailable = false) }
        }
    }

    fun signUp() = intent {
        checkEmailDuplicate()
        if (state.isEmailAvailable == false) return@intent
        postGeneralRegisterUseCase(
            name = state.name,
            phoneNumber = state.phoneNumber,
            loginId = state.loginId,
            password = state.password,  // Password hashing done in UseCase
            gender = state.gender,
            email = state.email,
            nickname = state.nickname
        ).onSuccess {
            postSideEffect(SignUpGeneralSideEffect.SignUpSuccess)
        }.onFailure {
            postSideEffect(SignUpGeneralSideEffect.SignUpFailure)
        }
    }
}

@Parcelize
data class SignUpGeneralState(
    val phoneNumber: String = "",
    val name: String = "",
    val gender: String = "",
    val loginId: String = "",
    val isLoginIdAvailable: Boolean? = null,
    val isLoginIdValid: Boolean = false,
    val password: String = "",
    val passwordConfirm: String = "",
    val showPassword: Boolean = false,
    val nickname: String = "",
    val isNicknameAvailable: Boolean? = null,
    val email: String = "",
    val isEmailAvailable: Boolean? = null,
    val isSignUpSuccess: Boolean = false
) : Parcelable

// Computed properties
val SignUpGeneralState.isPasswordValid
    get() = password.isValidPassword() && !password.containsKorean()

val SignUpGeneralState.isPasswordEqual
    get() = password == passwordConfirm

val SignUpGeneralState.currentStep: SignUpGeneralStep
    get() = if (isPasswordValid && isPasswordEqual) SignUpGeneralStep.NICKNAME_AND_EMAIL 
            else SignUpGeneralStep.INITIAL

val SignUpGeneralState.isEnabled
    get() = isNicknameValid && isEmailValid && isPasswordValid && 
            isPasswordEqual && isLoginIdAvailable == true

enum class SignUpGeneralStep { INITIAL, NICKNAME_AND_EMAIL }

sealed class SignUpGeneralSideEffect {
    data object SignUpSuccess : SignUpGeneralSideEffect()
    data object SignUpFailure : SignUpGeneralSideEffect()
}
```

**Sign Up Architecture Rules**:
- **MUST** use 4 separate ViewModels (NOT a single monolithic ViewModel)
- **MUST** pass data between screens via Navigation arguments
- **MUST** use `SavedStateHandle` to receive navigation arguments
- **MUST** use `Parcelable` for State classes (for SavedStateHandle compatibility)
- **MUST NOT** hash password in ViewModel - `PostStudentRegisterUseCase`/`PostGeneralRegisterUseCase` handle this internally
- **MUST** use `KOREATECH_EMAIL_DOMAIN` constant for student email suffix

### Profile Management Pattern

**MUST** handle profile viewing and editing:

```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel(), ContainerHost<ProfileState, ProfileSideEffect> {
    
    override val container = container<ProfileState, ProfileSideEffect>(ProfileState())
    
    init {
        loadProfile()
    }
    
    fun loadProfile() = intent {
        reduce { state.copy(isLoading = true) }
        
        getUserProfileUseCase()
            .onSuccess { user ->
                reduce { 
                    state.copy(
                        isLoading = false,
                        user = user,
                        nickname = user.nickname,
                        phoneNumber = user.phoneNumber
                    )
                }
            }
            .onFailure { error ->
                reduce { 
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
    }
    
    fun updateProfile() = intent {
        reduce { state.copy(isUpdating = true) }
        
        updateUserProfileUseCase(
            nickname = state.nickname,
            phoneNumber = state.phoneNumber
        )
            .onSuccess { updatedUser ->
                reduce { 
                    state.copy(
                        isUpdating = false,
                        user = updatedUser
                    )
                }
                postSideEffect(ProfileSideEffect.ProfileUpdated)
            }
            .onFailure { error ->
                reduce { 
                    state.copy(
                        isUpdating = false,
                        error = error.message
                    )
                }
            }
    }
    
    fun uploadProfileImage(imageUri: Uri) = intent {
        reduce { state.copy(isUploadingImage = true) }
        
        uploadProfileImageUseCase(imageUri)
            .onSuccess { imageUrl ->
                reduce { 
                    state.copy(
                        isUploadingImage = false,
                        user = state.user?.copy(profileImageUrl = imageUrl)
                    )
                }
            }
            .onFailure { error ->
                reduce { 
                    state.copy(
                        isUploadingImage = false,
                        error = error.message
                    )
                }
            }
    }
    
    fun logout() = intent {
        logoutUseCase()
            .onSuccess {
                postSideEffect(ProfileSideEffect.LogoutSuccess)
            }
            .onFailure { error ->
                postSideEffect(ProfileSideEffect.ShowError(error.message ?: "Logout failed"))
            }
    }
}
```

## Critical Rules

These rules are **non-negotiable**:

1. **Legacy Pattern**: **MUST** use `Pair<T?, ErrorHandler?>` pattern for user/auth UseCases (NOT `Result<T>`)
2. **Import Extensions**: **MUST** import `onSuccess`/`onFailure` from `in.koreatech.koin.domain.util`
3. **Password Hashing**: **NEVER** hash passwords in ViewModel - `UserLoginUseCase` handles this internally
4. **Token Management**: **NEVER** manually save tokens - `UserLoginUseCase` handles this internally
5. **Error Access**: **MUST** use `it.message` in `onFailure` block (`it` is `ErrorHandler`, not `Exception`)
6. **Analytics**: **MUST** log authentication events using `EventLogger`
7. **Input Validation**: **MUST** validate all user inputs client-side

## Extension Functions for Validation

**MUST** use these validation utilities:

```kotlin
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.toSHA256(): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

fun String.isValidPassword(): Boolean {
    return length >= 8 && 
           any { it.isUpperCase() } && 
           any { it.isLowerCase() } && 
           any { it.isDigit() }
}

fun String.isValidStudentId(): Boolean {
    return matches(Regex("^\\d{10}$")) // Example: 10 digits
}

fun String.isValidPhoneNumber(): Boolean {
    return matches(Regex("^01[016789]-?\\d{3,4}-?\\d{4}$"))
}
```

## Build Commands

```bash
# Build user module
./gradlew :feature:user:build

# Run user tests
./gradlew :feature:user:test

# Run UI tests  
./gradlew :feature:user:connectedAndroidTest
```

## User Module Best Practices

1. **Security First**: Never log sensitive data (passwords, tokens)
2. **Strong Validation**: Validate all inputs client-side and server-side
3. **Clear Error Messages**: Provide specific, actionable error messages
4. **Session Management**: Handle token expiration gracefully
5. **Analytics**: Track user flows for optimization

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE USER module  
**Maintainers**: BCSD Android Track