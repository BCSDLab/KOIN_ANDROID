package `in`.koreatech.business.feature.signup.accountauth

data class EmailAuthState(
    val authCode: String = "",
    val isLoading: Boolean = false,
    val signupContinuationState: Unit = Unit,
    val signUpContinuationError: Throwable? = null,
    val timeLeft: Int = 300,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val formattedTimeLeft: String = "05:00"
)
