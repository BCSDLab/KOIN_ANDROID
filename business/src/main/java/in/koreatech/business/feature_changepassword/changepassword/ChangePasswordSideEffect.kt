package `in`.koreatech.business.feature_changepassword.changepassword
sealed class ChangePasswordSideEffect {

    object NotCoincidePassword: ChangePasswordSideEffect()

    object GotoFinishScreen: ChangePasswordSideEffect()

}