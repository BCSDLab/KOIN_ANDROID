package `in`.koreatech.business.feature.findpassword.changepassword

sealed class ChangePasswordSideEffect {

    object NotCoincidePassword: ChangePasswordSideEffect()

    object GotoFinishScreen: ChangePasswordSideEffect()

}