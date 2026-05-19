package `in`.koreatech.koin.feature.user.ui.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.user.ui.signup.complete.SignUpCompleteScreen
import `in`.koreatech.koin.feature.user.ui.signup.term.SignUpTermScreen
import `in`.koreatech.koin.feature.user.ui.signup.userinfo.general.SignUpGeneralUserInfo
import `in`.koreatech.koin.feature.user.ui.signup.userinfo.student.SignUpStudentUserInfo
import `in`.koreatech.koin.feature.user.ui.signup.usertype.SignUpUserType
import `in`.koreatech.koin.feature.user.ui.signup.verification.SignUpVerification

fun NavGraphBuilder.koinUserGraph(
    navController: NavController,
    finish: () -> Unit = {}
) {
    composable<SignUpNavType.Term> {
        SignUpTermScreen {
            navController.navigate(SignUpNavType.Verification)
        }
    }
    composable<SignUpNavType.Verification> {
        SignUpVerification { name, phoneNumber, gender ->
            navController.navigate(SignUpNavType.UserType(name = name, phoneNumber = phoneNumber, gender = gender))
        }
    }
    composable<SignUpNavType.UserType> { entry ->
        val args = entry.toRoute<SignUpNavType.UserType>()
        SignUpUserType(
            navigateToStudentScreen = {
                navController.navigate(SignUpNavType.StudentUserInfo(name = args.name, phoneNumber = args.phoneNumber, gender = args.gender))
            },
            navigateToGeneralScreen = {
                navController.navigate(SignUpNavType.GeneralUserInfo(name = args.name, phoneNumber = args.phoneNumber, gender = args.gender))
            }
        )
    }
    composable<SignUpNavType.GeneralUserInfo> {
        SignUpGeneralUserInfo {
            navController.navigate(SignUpNavType.SignUpComplete)
        }
    }

    composable<SignUpNavType.StudentUserInfo> {
        SignUpStudentUserInfo {
            navController.navigate(SignUpNavType.SignUpComplete)
        }
    }

    composable<SignUpNavType.SignUpComplete> {
        SignUpCompleteScreen {
            finish()
        }
    }
}

const val PHONE_NUMBER = "phoneNumber"
const val NAME = "name"
const val GENDER = "gender"
